/*
 * $Header: /home/jerenkrantz/tmp/commons/commons-convert/cvs/home/cvs/jakarta-commons//digester/src/java/org/apache/commons/digester/FactoryCreateRule.java,v 1.14 2003/10/05 15:07:31 rdonkin Exp $
 * $Revision: 1.14 $
 * $Date: 2003/10/05 15:07:31 $
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

import org.apache.commons.collections.ArrayStack;
import org.xml.sax.Attributes;


/**
 * <p>Rule implementation that uses an {@link ObjectCreationFactory} to create
 * a new object which it pushes onto the object stack.  When the element is
 * complete, the object will be popped.</p>
 *
 * <p>This rule is intended in situations where the element's attributes are
 * needed before the object can be created.  A common senario is for the
 * ObjectCreationFactory implementation to use the attributes  as parameters
 * in a call to either a factory method or to a non-empty constructor.
 *
 * @author Robert Burrell Donkin
 * @version $Revision: 1.14 $ $Date: 2003/10/05 15:07:31 $
 */

public class FactoryCreateRule extends Rule {

    // ----------------------------------------------------------- Fields
    
    /** Should exceptions thrown by the factory be ignored? */
    private boolean ignoreCreateExceptions;
    /** Stock to manage */
    private ArrayStack exceptionIgnoredStack;

    // ----------------------------------------------------------- Constructors


    /**
     * Construct a factory create rule that will use the specified
     * class name to create an {@link ObjectCreationFactory} which will
     * then be used to create an object and push it on the stack.
     *
     * @param digester The associated Digester
     * @param className Java class name of the object creation factory class
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #FactoryCreateRule(String className)} instead.
     */
    public FactoryCreateRule(Digester digester, String className) {

        this(className);

    }


    /**
     * Construct a factory create rule that will use the specified
     * class to create an {@link ObjectCreationFactory} which will
     * then be used to create an object and push it on the stack.
     *
     * @param digester The associated Digester
     * @param clazz Java class name of the object creation factory class
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #FactoryCreateRule(Class clazz)} instead.
     */
    public FactoryCreateRule(Digester digester, Class clazz) {

        this(clazz);

    }


    /**
     * Construct a factory create rule that will use the specified
     * class name (possibly overridden by the specified attribute if present)
     * to create an {@link ObjectCreationFactory}, which will then be used
     * to instantiate an object instance and push it onto the stack.
     *
     * @param digester The associated Digester
     * @param className Default Java class name of the factory class
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name of the object creation factory to create.
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #FactoryCreateRule(String className, String attributeName)} instead.
     */
    public FactoryCreateRule(Digester digester,
                             String className, String attributeName) {

        this(className, attributeName);

    }


    /**
     * Construct a factory create rule that will use the specified
     * class (possibly overridden by the specified attribute if present)
     * to create an {@link ObjectCreationFactory}, which will then be used
     * to instantiate an object instance and push it onto the stack.
     *
     * @param digester The associated Digester
     * @param clazz Default Java class name of the factory class
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name of the object creation factory to create.
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #FactoryCreateRule(Class clazz, String attributeName)} instead.
     */
    public FactoryCreateRule(Digester digester,
                             Class clazz, String attributeName) {

        this(clazz, attributeName);

    }


    /**
     * Construct a factory create rule using the given, already instantiated,
     * {@link ObjectCreationFactory}.
     *
     * @param digester The associated Digester
     * @param creationFactory called on to create the object.
     *
     * @deprecated The digester instance is now set in the {@link Digester#addRule} method. 
     * Use {@link #FactoryCreateRule(ObjectCreationFactory creationFactory)} instead.
     */
    public FactoryCreateRule(Digester digester,
                             ObjectCreationFactory creationFactory) {

        this(creationFactory);

    }    

    /**
     * <p>Construct a factory create rule that will use the specified
     * class name to create an {@link ObjectCreationFactory} which will
     * then be used to create an object and push it on the stack.</p>
     *
     * <p>Exceptions thrown during the object creation process will be propagated.</p>
     *
     * @param className Java class name of the object creation factory class
     */
    public FactoryCreateRule(String className) {

        this(className, false);

    }


    /**
     * <p>Construct a factory create rule that will use the specified
     * class to create an {@link ObjectCreationFactory} which will
     * then be used to create an object and push it on the stack.</p>
     *
     * <p>Exceptions thrown during the object creation process will be propagated.</p>
     *
     * @param clazz Java class name of the object creation factory class
     */
    public FactoryCreateRule(Class clazz) {

        this(clazz, false);

    }


    /**
     * <p>Construct a factory create rule that will use the specified
     * class name (possibly overridden by the specified attribute if present)
     * to create an {@link ObjectCreationFactory}, which will then be used
     * to instantiate an object instance and push it onto the stack.</p>
     *
     * <p>Exceptions thrown during the object creation process will be propagated.</p>
     *
     * @param className Default Java class name of the factory class
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name of the object creation factory to create.
     */
    public FactoryCreateRule(String className, String attributeName) {

        this(className, attributeName, false);

    }


    /**
     * <p>Construct a factory create rule that will use the specified
     * class (possibly overridden by the specified attribute if present)
     * to create an {@link ObjectCreationFactory}, which will then be used
     * to instantiate an object instance and push it onto the stack.</p>
     *
     * <p>Exceptions thrown during the object creation process will be propagated.</p>
     *
     * @param clazz Default Java class name of the factory class
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name of the object creation factory to create.
     */
    public FactoryCreateRule(Class clazz, String attributeName) {

        this(clazz, attributeName, false);

    }


    /**
     * <p>Construct a factory create rule using the given, already instantiated,
     * {@link ObjectCreationFactory}.</p>
     *
     * <p>Exceptions thrown during the object creation process will be propagated.</p>
     *
     * @param creationFactory called on to create the object.
     */
    public FactoryCreateRule(ObjectCreationFactory creationFactory) {

        this(creationFactory, false);

    }
    
    /**
     * Construct a factory create rule that will use the specified
     * class name to create an {@link ObjectCreationFactory} which will
     * then be used to create an object and push it on the stack.
     *
     * @param className Java class name of the object creation factory class
     * @param ignoreCreateExceptions if true, exceptions thrown by the object
     *  creation factory
     * will be ignored.
     */
    public FactoryCreateRule(String className, boolean ignoreCreateExceptions) {

        this(className, null, ignoreCreateExceptions);

    }


    /**
     * Construct a factory create rule that will use the specified
     * class to create an {@link ObjectCreationFactory} which will
     * then be used to create an object and push it on the stack.
     *
     * @param clazz Java class name of the object creation factory class
     * @param ignoreCreateExceptions if true, exceptions thrown by the
     *  object creation factory
     * will be ignored.
     */
    public FactoryCreateRule(Class clazz, boolean ignoreCreateExceptions) {

        this(clazz, null, ignoreCreateExceptions);

    }


    /**
     * Construct a factory create rule that will use the specified
     * class name (possibly overridden by the specified attribute if present)
     * to create an {@link ObjectCreationFactory}, which will then be used
     * to instantiate an object instance and push it onto the stack.
     *
     * @param className Default Java class name of the factory class
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name of the object creation factory to create.
     * @param ignoreCreateExceptions if true, exceptions thrown by the object
     *  creation factory will be ignored.
     */
    public FactoryCreateRule(
                                String className, 
                                String attributeName,
                                boolean ignoreCreateExceptions) {

        this.className = className;
        this.attributeName = attributeName;
        this.ignoreCreateExceptions = ignoreCreateExceptions;

    }


    /**
     * Construct a factory create rule that will use the specified
     * class (possibly overridden by the specified attribute if present)
     * to create an {@link ObjectCreationFactory}, which will then be used
     * to instantiate an object instance and push it onto the stack.
     *
     * @param clazz Default Java class name of the factory class
     * @param attributeName Attribute name which, if present, contains an
     *  override of the class name of the object creation factory to create.
     * @param ignoreCreateExceptions if true, exceptions thrown by the object
     *  creation factory will be ignored.
     */
    public FactoryCreateRule(
                                Class clazz, 
                                String attributeName,
                                boolean ignoreCreateExceptions) {

        this(clazz.getName(), attributeName, ignoreCreateExceptions);

    }


    /**
     * Construct a factory create rule using the given, already instantiated,
     * {@link ObjectCreationFactory}.
     *
     * @param creationFactory called on to create the object.
     * @param ignoreCreateExceptions if true, exceptions thrown by the object
     *  creation factory will be ignored.
     */
    public FactoryCreateRule(
                            ObjectCreationFactory creationFactory, 
                            boolean ignoreCreateExceptions) {

        this.creationFactory = creationFactory;
        this.ignoreCreateExceptions = ignoreCreateExceptions;
    }

    // ----------------------------------------------------- Instance Variables


    /**
     * The attribute containing an override class name if it is present.
     */
    protected String attributeName = null;


    /**
     * The Java class name of the ObjectCreationFactory to be created.
     * This class must have a no-arguments constructor.
     */
    protected String className = null;


    /**
     * The object creation factory we will use to instantiate objects
     * as required based on the attributes specified in the matched XML
     * element.
     */
    protected ObjectCreationFactory creationFactory = null;


    // --------------------------------------------------------- Public Methods


    /**
     * Process the beginning of this element.
     *
     * @param attributes The attribute list of this element
     */
    public void begin(String namespace, String name, Attributes attributes) throws Exception {
        
        if (ignoreCreateExceptions) {
        
            if (exceptionIgnoredStack == null) {
                exceptionIgnoredStack = new ArrayStack();
            }
            
            try {
                Object instance = getFactory(attributes).createObject(attributes);
                
                if (digester.log.isDebugEnabled()) {
                    digester.log.debug("[FactoryCreateRule]{" + digester.match +
                            "} New " + instance.getClass().getName());
                }
                digester.push(instance);
                exceptionIgnoredStack.push(Boolean.FALSE);
                
            } catch (Exception e) {
                // log message and error
                if (digester.log.isInfoEnabled()) {
                    digester.log.info("[FactoryCreateRule] Create exception ignored: " 
                        + ((e.getMessage() == null) ? e.getClass().getName() : e.getMessage()));
                    if (digester.log.isDebugEnabled()) {
                        digester.log.debug("[FactoryCreateRule] Ignored exception:", e);
                    }
                }
                exceptionIgnoredStack.push(Boolean.TRUE);
            }
            
        } else {
            Object instance = getFactory(attributes).createObject(attributes);
            
            if (digester.log.isDebugEnabled()) {
                digester.log.debug("[FactoryCreateRule]{" + digester.match +
                        "} New " + instance.getClass().getName());
            }
            digester.push(instance);
        }
    }


    /**
     * Process the end of this element.
     */
    public void end(String namespace, String name) throws Exception {
        
        // check if object was created 
        // this only happens if an exception was thrown and we're ignoring them
        if (	
                ignoreCreateExceptions 
                && exceptionIgnoredStack != null 
                && !(exceptionIgnoredStack.empty())) {
                
            if (((Boolean) exceptionIgnoredStack.pop()).booleanValue()) {
                // creation exception was ignored
                // nothing was put onto the stack
                if (digester.log.isTraceEnabled()) {
                    digester.log.trace("[FactoryCreateRule] No creation so no push so no pop");
                }
                return;
            }
        } 

        Object top = digester.pop();
        if (digester.log.isDebugEnabled()) {
            digester.log.debug("[FactoryCreateRule]{" + digester.match +
                    "} Pop " + top.getClass().getName());
        }

    }


    /**
     * Clean up after parsing is complete.
     */
    public void finish() throws Exception {

        if (attributeName != null) {
            creationFactory = null;
        }

    }


    /**
     * Render a printable version of this Rule.
     */
    public String toString() {

        StringBuffer sb = new StringBuffer("FactoryCreateRule[");
        sb.append("className=");
        sb.append(className);
        sb.append(", attributeName=");
        sb.append(attributeName);
        if (creationFactory != null) {
            sb.append(", creationFactory=");
            sb.append(creationFactory);
        }
        sb.append("]");
        return (sb.toString());

    }


    // ------------------------------------------------------ Protected Methods


    /**
     * Return an instance of our associated object creation factory,
     * creating one if necessary.
     *
     * @param attributes Attributes passed to our factory creation element
     *
     * @exception Exception if any error occurs
     */
    protected ObjectCreationFactory getFactory(Attributes attributes)
            throws Exception {

        if (creationFactory == null) {
            String realClassName = className;
            if (attributeName != null) {
                String value = attributes.getValue(attributeName);
                if (value != null) {
                    realClassName = value;
                }
            }
            if (digester.log.isDebugEnabled()) {
                digester.log.debug("[FactoryCreateRule]{" + digester.match +
                        "} New factory " + realClassName);
            }
            Class clazz = digester.getClassLoader().loadClass(realClassName);
            creationFactory = (ObjectCreationFactory)
                    clazz.newInstance();
            creationFactory.setDigester(digester);
        }
        return (creationFactory);

    }    
}
