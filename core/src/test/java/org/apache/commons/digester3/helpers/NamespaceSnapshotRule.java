package org.apache.commons.digester3.helpers;

import org.apache.commons.digester3.Digester;
import org.apache.commons.digester3.NamespacedBox;
import org.apache.commons.digester3.Rule;
import org.apache.commons.digester3.binder.RuleProvider;
import org.xml.sax.Attributes;

import java.util.Map;

public class NamespaceSnapshotRule extends Rule {
    /**
     * @see Rule#begin(String, String, Attributes)
     */
    @Override
    public final void begin( final String namespace, final String name, final Attributes attributes )
    {
        final Digester d = getDigester();
        final Map<String, String> namespaces = d.getCurrentNamespaces();
        ( (NamespacedBox) d.peek() ).setNamespaces( namespaces );
    }

    public static class Provider implements RuleProvider<NamespaceSnapshotRule>
    {

        /**
         * {@inheritDoc}
         */
        @Override
        public NamespaceSnapshotRule get()
        {
            return new NamespaceSnapshotRule();
        }

    }
}
