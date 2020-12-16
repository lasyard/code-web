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

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Nonnull;

final class DaoFactory {
    private static final ClassPathXmlApplicationContext ctx;

    static {
        ctx = new ClassPathXmlApplicationContext();
        ctx.setConfigLocation("/application.xml");
        ctx.refresh();
    }

    private DaoFactory() {
    }

    @Nonnull
    static ModelDao getModelDao() {
        return (ModelDao) ctx.getBean("modelDao");
    }

    @Nonnull
    static PlatformTransactionManager getTxManager() {
        return (PlatformTransactionManager) ctx.getBean("txManager");
    }
}
