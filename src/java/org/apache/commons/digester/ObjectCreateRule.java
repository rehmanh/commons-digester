/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//digester/src/java/org/apache/commons/digester/ObjectCreateRule.java,v 1.14 2003/10/05 15:07:04 rdonkin Exp $
 * $Revision: 1.14 $
 * $Date: 2003/10/05 15:07:04 $
 *
 * ====================================================================
 * 
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2001-2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgement:  
 *       "This product includes software developed by the 
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "Apache", "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written 
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache" nor may "Apache" appear in their names without prior 
 *    written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */ 


package org.apache.commons.digester;


import org.xml.sax.Attributes;


/**
 * Rule implementation that creates a new object and pushes it
 * onto the object stack.  When the element is complete, the
 * object will be popped
 *
 * @author Craig McClanahan
 * @author Scott Sanders
 * @version $Revision: 1.14 $ $Date: 2003/10/05 15:07:04 $
 */

public class ObjectCreateRule extends Rule {


    // ----------------------------------------------------------- Constructors


    /**
     * Construct an object create rule with the specified class name.
     *
     * @param digester The associated Digester
     * @param className Java class name of the object to be created
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #ObjectCreateRule(String className)} instead.
     */
    public ObjectCreateRule(Digester digester, String className) {

        this(className);

    }


    /**
     * Construct an object create rule with the specified class.
     *
     * @param digester The associated Digester
     * @param clazz Java class name of the object to be created
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #ObjectCreateRule(Class clazz)} instead.
     */
    public ObjectCreateRule(Digester digester, Class clazz) {

        this(clazz);

    }


    /**
     * Construct an object create rule with the specified class name and an
     * optional attribute name containing an override.
     *
     * @param digester The associated Digester
     * @param className Java class name of the object to be created
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name to create
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #ObjectCreateRule(String className, String attributeName)} instead.
     */
    public ObjectCreateRule(Digester digester, String className,
                            String attributeName) {

        this (className, attributeName);

    }


    /**
     * Construct an object create rule with the specified class and an
     * optional attribute name containing an override.
     *
     * @param digester The associated Digester
     * @param attributeName Attribute name which, if present, contains an
     * @param clazz Java class name of the object to be created
     *  override of the class name to create
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #ObjectCreateRule(String attributeName, Class clazz)} instead.
     */
    public ObjectCreateRule(Digester digester,
                            String attributeName,
                            Class clazz) {

        this(attributeName, clazz);

    }

    /**
     * Construct an object create rule with the specified class name.
     *
     * @param className Java class name of the object to be created
     */
    public ObjectCreateRule(String className) {

        this(className, (String) null);

    }


    /**
     * Construct an object create rule with the specified class.
     *
     * @param clazz Java class name of the object to be created
     */
    public ObjectCreateRule(Class clazz) {

        this(clazz.getName(), (String) null);

    }


    /**
     * Construct an object create rule with the specified class name and an
     * optional attribute name containing an override.
     *
     * @param className Java class name of the object to be created
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name to create
     */
    public ObjectCreateRule(String className,
                            String attributeName) {

        this.className = className;
        this.attributeName = attributeName;

    }


    /**
     * Construct an object create rule with the specified class and an
     * optional attribute name containing an override.
     *
     * @param attributeName Attribute name which, if present, contains an
     * @param clazz Java class name of the object to be created
     *  override of the class name to create
     */
    public ObjectCreateRule(String attributeName,
                            Class clazz) {

        this(clazz.getName(), attributeName);

    }

    // ----------------------------------------------------- Instance Variables


    /**
     * The attribute containing an override class name if it is present.
     */
    protected String attributeName = null;


    /**
     * The Java class name of the object to be created.
     */
    protected String className = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     */
    public void begin(Attributes attributes) throws Exception {

        // Identify the name of the class to instantiate
        String realClassName = className;
        if (attributeName != null) {
            String value = attributes.getValue(attributeName);
            if (value != null) {
                realClassName = value;
            }
        }
        if (digester.log.isDebugEnabled()) {
            digester.log.debug("[ObjectCreateRule]{" + digester.match +
                    "}New " + realClassName);
        }

        // Instantiate the new object and push it on the context stack
        Class clazz = digester.getClassLoader().loadClass(realClassName);
        Object instance = clazz.newInstance();
        digester.push(instance);

    }


    /**
     * Process the end of this element.
     */
    public void end() throws Exception {

        Object top = digester.pop();
        if (digester.log.isDebugEnabled()) {
            digester.log.debug("[ObjectCreateRule]{" + digester.match +
                    "} Pop " + top.getClass().getName());
        }

    }


    /**
     * Render a printable version of this Rule.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("ObjectCreateRule[");
        sb.append("className=");
        sb.append(className);
        sb.append(", attributeName=");
        sb.append(attributeName);
        sb.append("]");
        return (sb.toString());

    }


}
