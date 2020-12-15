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

package io.github.lasyard.web.mybatis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@ActiveProfiles({"test"})
@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ModelMapperIT {
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testFindAll() {
        List<Model> modelList = modelMapper.findAll();
        Model model = modelList.get(0);
        assertThat(model.getId(), is(1));
        assertThat(model.getName(), is("Alice"));
    }

    @Test
    public void testFindById() {
        Model model = modelMapper.findById(1);
        assertThat(model.getId(), is(1));
        assertThat(model.getName(), is("Alice"));
    }

    @Test
    public void testInsert() {
        Model model = new Model();
        model.setName("Bob");
        Integer result = modelMapper.insert(model);
        assertThat(result, is(1));
        assertThat(model.getId(), is(2));
    }

    @Configuration
    @EnableAutoConfiguration
    static class Config {
    }
}
