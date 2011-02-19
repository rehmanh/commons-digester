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
package org.apache.commons.digester3.plugins;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.apache.commons.digester3.AbstractRulesModule;
import org.apache.commons.digester3.Digester;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for the declaration of custom rules for a plugin using
 * xmlrules format files.
 */
public class TestXmlRuleInfo extends AbstractPluginTestCase {

    private Digester digester;

    @Before
    public void setUp() throws Exception {
        digester = newPluginDigester(new AbstractRulesModule() {

            @Override
            protected void configure() {
                forPattern("root/plugin").declarePlugin();
                forPattern("root/object").createPlugin().ofType(ObjectTestImpl.class);
            }

        });
    }

    @After
    public void tearDown() {
        this.digester = null;
    }

    @Test
    public void testXmlRuleInfoExplicitFile() throws Exception {
        // * tests that custom rules can be declared on a 
        //   separate class by explicitly declaring a file containing
        //   the rules, using a relative or absolute path name.

        StringBuffer input = new StringBuffer();
        input.append("<root>");
        input.append(" <plugin");
        input.append("  id='testobject'"); 
        input.append("  class='org.apache.commons.digester3.plugins.ObjectTestImpl'");
        input.append("  file='src/test/resources/org/apache/commons/digester3/plugins/xmlrules1.xml'");
        input.append("  />");
        input.append("  <object plugin-id='testobject' />");
        input.append("</root>");

        digester.parse(new StringReader(input.toString()));

        Object root = digester.getRoot();
        assertEquals(ObjectTestImpl.class, root.getClass());
        ObjectTestImpl testObject = (ObjectTestImpl) root;
        assertEquals("xmlrules1", testObject.getValue());
    }

    @Test
    public void testXmlRuleInfoExplicitResource() throws Exception {
        // * tests that custom rules can be declared on a 
        //   separate class by explicitly declaring the rule class.
        //   and explicitly declaring a file which is somewhere in the 
        //   classpath.

        StringBuffer input = new StringBuffer();
        input.append("<root>");
        input.append(" <plugin");
        input.append("  id='testobject'"); 
        input.append("  class='org.apache.commons.digester3.plugins.ObjectTestImpl'");
        input.append("  resource='org/apache/commons/digester3/plugins/xmlrules2.xml'");
        input.append("  />");
        input.append("  <object plugin-id='testobject' />");
        input.append("</root>");

        digester.parse(new StringReader(input.toString()));

        Object root = digester.getRoot();
        assertEquals(ObjectTestImpl.class, root.getClass());
        ObjectTestImpl testObject = (ObjectTestImpl) root;
        assertEquals("xmlrules2", testObject.getValue());
    }

    @Test
    public void testXmlRuleImplicitResource() throws Exception {
        // * tests that custom rules can be declared on a 
        //   separate class by explicitly declaring the rule class.
        //   and explicitly declaring a file which is somewhere in the 
        //   classpath.

        StringBuffer input = new StringBuffer();
        input.append("<root>");
        input.append(" <plugin");
        input.append("  id='testobject'"); 
        input.append("  class='org.apache.commons.digester3.plugins.ObjectTestImpl' />");
        input.append("  <object plugin-id='testobject' />");
        input.append("</root>");

        digester.parse(new StringReader(input.toString()));

        Object root = digester.getRoot();
        assertEquals(ObjectTestImpl.class, root.getClass());
        ObjectTestImpl testObject = (ObjectTestImpl) root;
        assertEquals("xmlrules-ruleinfo", testObject.getValue());
    }

}
