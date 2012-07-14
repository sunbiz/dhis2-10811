package org.hisp.dhis.reporting.reportviewer.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.system.util.StreamUtils;
import org.hisp.dhis.util.ContextUtils;
import org.springframework.core.io.ClassPathResource;

import com.opensymphony.xwork2.Action;

public class GetReportTemplateAction
    implements Action
{
    private static final String TEMPLATE_JASPER = "jasper-report-template.jrxml";
    private static final String TEMPLATE_HTML = "html-report-template.html";
    
    private static final String TYPE_JASPER = "jasper";
    private static final String TYPE_HTML = "html";
    
    private String type;
        
    public void setType( String type )
    {
        this.type = type;
    }

    @Override
    public String execute()
        throws Exception
    {
        HttpServletResponse response = ServletActionContext.getResponse();

        Map<String, String> typeTemplateMap = new HashMap<String, String>();
        typeTemplateMap.put( TYPE_JASPER, TEMPLATE_JASPER );
        typeTemplateMap.put( TYPE_HTML, TEMPLATE_HTML );
        
        Map<String, String> typeContentTypeMap = new HashMap<String, String>();
        typeContentTypeMap.put( TYPE_JASPER, ContextUtils.CONTENT_TYPE_XML );
        typeContentTypeMap.put( TYPE_HTML, ContextUtils.CONTENT_TYPE_HTML );
        
        if ( type != null & typeTemplateMap.containsKey( type ) )
        {
            String template = typeTemplateMap.get( type );
            String contentType = typeContentTypeMap.get( type );
            
            ContextUtils.configureResponse( response, contentType, false, template, true );
            
            StreamUtils.streamcopy( new BufferedInputStream( new ClassPathResource( template ).getInputStream() ), 
                new BufferedOutputStream( response.getOutputStream() ) );
        }
        
        return SUCCESS;
    }
}
