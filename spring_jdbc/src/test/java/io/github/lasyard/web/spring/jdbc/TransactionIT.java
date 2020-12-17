/*
 * Copyright 2020 lasyard@github.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.lasyard.web.spring.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@Slf4j
public class TransactionIT {
    private static ModelDao dao;
    private static PlatformTransactionManager txManager;

    private boolean updatedInTx2;
    private boolean readInTx1;

    @BeforeClass
    public static void setupClass() {
        dao = DaoFactory.getModelDao();
        txManager = DaoFactory.getTxManager();
    }

    @Before
    public void setup() {
        updatedInTx2 = false;
        readInTx1 = false;
    }

    @Nonnull
    private List<String> runTest(int isolationLevel) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("TxTest");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(isolationLevel);

        Model model = new Model();
        model.setName("One");
        final int id = dao.insert(model);

        TransactionStatus tx1 = txManager.getTransaction(def);
        log.info("Transaction 1 started.");
        model = dao.get(id);
        List<String> results = new ArrayList<>();
        assert model != null;
        results.add(model.getName());
        log.info("First read result: " + model);

        Thread th = new Thread(() -> {
            TransactionStatus tx2 = txManager.getTransaction(def);
            log.info("Transaction 2 started.");
            try {
                synchronized (this) {
                    Model model1 = dao.get(id);
                    assert model1 != null;
                    model1.setName("Two");
                    dao.update(model1);
                    log.info("Records updated in transaction 2.");
                    updatedInTx2 = true;
                    notify();
                    while (!readInTx1) {
                        wait();
                    }
                    txManager.commit(tx2);
                    log.info("Transaction 2 committed.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        th.start();

        try {
            synchronized (this) {
                while (!updatedInTx2) {
                    wait();
                }
                model = dao.get(id);
                assert model != null;
                results.add(model.getName());
                log.info("Read uncommitted result: " + model);
                readInTx1 = true;
                notify();
            }
            th.join();
            model = dao.get(id);
            assert model != null;
            results.add(model.getName());
            log.info("Read committed result:" + model);
            txManager.commit(tx1);
            log.info("Transaction 1 committed.");
            model = dao.get(id);
            assert model != null;
            results.add(model.getName());
            log.info("Read final result:" + model);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return results;
    }

    @Test
    public void testDirtyReads() {
        List<String> results = runTest(TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        assertThat(results.size(), is(4));
        assertThat(results.get(0), is("One"));
        assertThat(results.get(1), is("Two"));
        assertThat(results.get(2), is("Two"));
        assertThat(results.get(3), is("Two"));
    }

    @Test
    public void testNonRepeatableReads() {
        List<String> results = runTest(TransactionDefinition.ISOLATION_READ_COMMITTED);
        assertThat(results.size(), is(4));
        assertThat(results.get(0), is("One"));
        assertThat(results.get(1), is("One"));
        assertThat(results.get(2), is("Two"));
        assertThat(results.get(3), is("Two"));
    }

    @Test
    public void testRepeatableReads() {
        List<String> results = runTest(TransactionDefinition.ISOLATION_REPEATABLE_READ);
        assertThat(results.size(), is(4));
        assertThat(results.get(0), is("One"));
        assertThat(results.get(1), is("One"));
        assertThat(results.get(2), is("One"));
        assertThat(results.get(3), is("Two"));
    }
}
