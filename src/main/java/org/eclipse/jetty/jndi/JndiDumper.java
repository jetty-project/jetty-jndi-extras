//
//  ========================================================================
//  Copyright (c) 1995-2014 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.jndi;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.naming.InitialContext;

import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.annotation.ManagedOperation;
import org.eclipse.jetty.util.annotation.Name;
import org.eclipse.jetty.util.component.AbstractLifeCycle;

@ManagedObject("JNDI Tree Dump Utility")
public class JndiDumper extends AbstractLifeCycle
{
    @ManagedOperation("Dump the jndi state to a string")
    public String dump()
    {
        return dumpContext("");
    }
    
    @ManagedOperation("Dump the jndi state of 'java:' to a string")
    public String dumpJavaContext()
    {
        return dumpContext("java:");
    }
    
    @ManagedOperation("Dump the jndi state of a specific context to a string")
    public String dumpContext(@Name(value="lookup", description="The lookup in the InitialContext") String lookup)
    {
        return lookupAndDump(lookup);
    }
    
    private String lookupAndDump(String lookup)
    {
        StringWriter s = new StringWriter();
        PrintWriter out = new PrintWriter(s);
        try
        {
            out.printf("%s - %s%n",String.valueOf(this), getState());
            InitialContext context = new InitialContext();
            Object result = context.lookup(lookup);
            
            JettyJndiDumper dumper = new JettyJndiDumper();
            dumper.dump(new PrintWriter(s),result);
        }
        catch (Throwable t)
        {
            t.printStackTrace(out);
        }
        return s.toString();
    }
}
