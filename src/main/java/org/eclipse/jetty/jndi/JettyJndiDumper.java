package org.eclipse.jetty.jndi;

import java.io.IOException;
import java.io.PrintWriter;

public class JettyJndiDumper
{
    private Class<?> jettyNamingContextClass;
    private Class<?> jettyJavaRootContextClass;
    private Class<?> jettyLocalContextClass;

    public JettyJndiDumper()
    {
        jettyNamingContextClass = ReflectUtils.findOptionalClass("org.eclipse.jetty.jndi.NamingContext");
        jettyJavaRootContextClass = ReflectUtils.findOptionalClass("org.eclipse.jetty.jndi.java.javaRootURLContext");
        jettyLocalContextClass = ReflectUtils.findOptionalClass("org.eclipse.jetty.jndi.local.localContextRoot");
    }

    public void dump(PrintWriter out, Object obj) throws IOException, ReflectiveOperationException
    {
        if (obj == null)
        {
            out.printf("<null object>%n");
        }
        else if (ReflectUtils.isInstanceOf(obj,jettyLocalContextClass))
        {
            out.printf("Jetty Local Context: (%s)%n",obj.getClass().getName());
            Object root = ReflectUtils.invokeMethod(obj.getClass(),"getRoot",new Object[0]);
            out.printf(" .getRoot() = %s%n",root);
            dump(out,root);
        }
        else if (ReflectUtils.isInstanceOf(obj,jettyJavaRootContextClass))
        {
            out.printf("Jetty Java Root Context: (%s)%n",obj.getClass().getName());
            Object root = ReflectUtils.invokeMethod(obj.getClass(),"getRoot",new Object[0]);
            out.printf(" .getRoot() = %s%n",root);
            dump(out,root);
        }
        else if (ReflectUtils.isInstanceOf(obj,jettyNamingContextClass))
        {
            out.printf("Jetty NamingContext: (%s)%n",obj.getClass().getName());
            dumpNamingContext(out,obj);
        }
        else
        {
            out.printf("Object: (%s) %s%n", obj.getClass().getName(), obj.toString());
        }
    }

    private void dumpNamingContext(Appendable out, Object obj) throws ReflectiveOperationException
    {
        // public void dump(Appendable out,String indent)
        Class<?> params[] = new Class[] { Appendable.class, String.class };
        Object args[] = new Object[] { out, "  " };
        ReflectUtils.invokeMethod(obj,"dump",params,args);
    }
}