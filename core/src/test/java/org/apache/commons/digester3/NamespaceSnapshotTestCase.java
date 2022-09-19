/* $Id$
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.commons.digester3;

import static org.apache.commons.digester3.binder.DigesterLoader.newLoader;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.commons.digester3.binder.AbstractRulesModule;
import org.apache.commons.digester3.binder.RuleProvider;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.xml.sax.Attributes;

import static org.mockito.Mockito.*;

/**
 * Tests namespace snapshotting.
 */

public class NamespaceSnapshotTestCase {

    /**
     * Namespace snapshot test case.
     */
    @Test
    public void testNamespaceSnapshots()
        throws Exception
    {
        final Rule rule = spy(Rule.class);

        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock args) throws Throwable {
                Digester d = rule.getDigester();
                final Map<String, String> namespaces = d.getCurrentNamespaces();
                ( (NamespacedBox) d.peek() ).setNamespaces( namespaces );
                return rule;
            }
        }).when(rule).begin(any(String.class), any(String.class), any(Attributes.class));

        final RuleProvider<Rule> ruleProvider = mock(RuleProvider.class);
        when(ruleProvider.get()).thenAnswer(new Answer<Rule>() {
            @Override
            public Rule answer(InvocationOnMock invocationOnMock) throws Throwable {
                return rule;
            }
        });

        final Digester digester = newLoader( new AbstractRulesModule()
        {

            @Override
            protected void configure()
            {
                forPattern( "box" ).createObject().ofType( NamespacedBox.class )
                    .then()
                    .setProperties()
                    .then()
                    .addRuleCreatedBy(ruleProvider);
                forPattern( "box/subBox" ).createObject().ofType( NamespacedBox.class )
                    .then()
                    .setProperties()
                    .then()
                    .addRuleCreatedBy(ruleProvider)
                    .then()
                    .setNext( "addChild" );
            }

        }).setNamespaceAware( true ).newDigester();

        final NamespacedBox root = digester.parse( getInputStream( "Test11.xml" ) );

        Map<String, String> nsmap = root.getNamespaces();
        assertEquals( 3, nsmap.size() );
        assertEquals( "", nsmap.get( "" ) );
        assertEquals( "http://commons.apache.org/digester/Foo", nsmap.get( "foo" ) );
        assertEquals( "http://commons.apache.org/digester/Bar", nsmap.get( "bar" ) );

        final List<Box> children = root.getChildren();
        assertEquals( 3, children.size() );

        final NamespacedBox child1 = (NamespacedBox) children.get( 0 );
        nsmap = child1.getNamespaces();
        assertEquals( 3, nsmap.size() );
        assertEquals( "", nsmap.get( "" ) );
        assertEquals( "http://commons.apache.org/digester/Foo1", nsmap.get( "foo" ) );
        assertEquals( "http://commons.apache.org/digester/Bar1", nsmap.get( "bar" ) );

        final NamespacedBox child2 = (NamespacedBox) children.get( 1 );
        nsmap = child2.getNamespaces();
        assertEquals( 5, nsmap.size() );
        assertEquals( "", nsmap.get( "" ) );
        assertEquals( "http://commons.apache.org/digester/Foo", nsmap.get( "foo" ) );
        assertEquals( "http://commons.apache.org/digester/Bar", nsmap.get( "bar" ) );
        assertEquals( "http://commons.apache.org/digester/Alpha", nsmap.get( "alpha" ) );
        assertEquals( "http://commons.apache.org/digester/Beta", nsmap.get( "beta" ) );

        final NamespacedBox child3 = (NamespacedBox) children.get( 2 );
        nsmap = child3.getNamespaces();
        assertEquals( 4, nsmap.size() );
        assertEquals( "", nsmap.get( "" ) );
        assertEquals( "http://commons.apache.org/digester/Foo3", nsmap.get( "foo" ) );
        assertEquals( "http://commons.apache.org/digester/Alpha", nsmap.get( "alpha" ) );
        assertEquals( "http://commons.apache.org/digester/Bar", nsmap.get( "bar" ) );

    }

    // ------------------------------------------------ Utility Support Methods

    /**
     * Return an appropriate InputStream for the specified test file (which must be inside our current package.
     *
     * @param name Name of the test file we want
     * @throws IOException if an input/output error occurs
     */
    protected InputStream getInputStream( final String name )
        throws IOException
    {

        return ( this.getClass().getResourceAsStream( "/org/apache/commons/digester3/" + name ) );

    }

}
