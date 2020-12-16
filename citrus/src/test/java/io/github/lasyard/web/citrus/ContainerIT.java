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

package io.github.lasyard.web.citrus;

import com.consol.citrus.TestAction;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.container.Sequence;
import com.consol.citrus.dsl.builder.FinallySequenceBuilder;
import com.consol.citrus.dsl.builder.SequenceBuilder;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import org.junit.Test;

public class ContainerIT extends JUnit4CitrusTestDesigner {
    @Test
    @CitrusTest
    public void testSequence() {
        doFinally().actions(
            sequential().actions(
                echo("echo 1"),
                echo("echo 2")
            )
        );
    }

    @Test
    @CitrusTest
    public void testSequence2() {
        TestAction e1 = echo("echo 1");
        TestAction e2 = echo("echo 2");
        Sequence s = sequential().actions(e1, e2);
        doFinally().actions(s);
    }

    @Test
    @CitrusTest
    public void testSequence3() {
        Sequence s = sequential().actions(
            echo("echo 1"),
            echo("echo 2")
        );
        doFinally().actions(s);
    }

    @Test
    @CitrusTest
    public void testSequence4() {
        FinallySequenceBuilder f = doFinally();
        Sequence s = sequential().actions(
            echo("echo 1"),
            echo("echo 2")
        );
        f.actions(s);
    }

    @Test
    @CitrusTest
    public void testSequence5() {
        FinallySequenceBuilder f = doFinally();
        SequenceBuilder s = sequential();
        TestAction e1 = echo("echo 1");
        TestAction e2 = echo("echo 2");
        s.actions(e1, e2);
        f.actions(s);
    }
}
