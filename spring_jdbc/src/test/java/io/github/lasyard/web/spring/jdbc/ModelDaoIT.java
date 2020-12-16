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

import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

public class ModelDaoIT {
    private static ModelDao dao;

    @BeforeClass
    public static void setupClass() {
        dao = DaoFactory.getModelDao();
    }

    @Test
    public void testInsertGet() {
        Model model1 = new Model();
        model1.setName("first");
        model1.setId(dao.insert(model1));

        Model model2 = new Model();
        model2.setName("second");
        model2.setId(dao.insert(model2));

        Model model;
        model = dao.get(model1.getId());
        assertEquals(model1, model);
        model = dao.get(model2.getId());
        assertEquals(model2, model);
        model = dao.get(10000);
        assertNull(model);

        assertThat(dao.getAll(), hasItems(model1, model2));
    }

    @Test
    public void testUpdate() {
        Model model = new Model();
        model.setId(1);
        model.setName("first-updated");
        int rows = dao.update(model);
        assertEquals(1, rows);
        model = dao.get(1);
        assertNotNull(model);
        assertEquals("first-updated", model.getName());
    }
}
