package org.hisp.dhis.result;

import org.apache.struts2.dispatcher.VelocityResult;

public class VelocityCacheManifestResult
    extends VelocityResult
{
    /**
     * Determines if a de-serialized file is compatible with this class.
     */
    private static final long serialVersionUID = 1038408987156030639L;

    @Override
    protected final String getContentType( String templateLocation )
    {       
        return "text/cache-manifest";
    }
}
