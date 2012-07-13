package org.hisp.dhis.coldchain.catalog.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;

import com.opensymphony.xwork2.Action;

public class ShowImageAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogService catalogService;
    
    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }
    
    
    
    // -------------------------------------------------------------------------
    // Input/Output and Getter / Setter
    // -------------------------------------------------------------------------

    private int id;
    
    public void setId( int id )
    {
        this.id = id;
    }
    
    
    private byte[] bimage;
    
    public byte[] getBimage()
    {
        return bimage;
    }
    
    String cataLogImage = null;
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute() throws Exception
    {
        
        Catalog catalog = catalogService.getCatalog( id );
        
        if ( catalog.getCatalogImage() != null )
        {
            cataLogImage = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER + File.separator + catalog.getCatalogImage();
        }
        
        else if ( catalog.getCatalogType().getCatalogTypeImage() != null )
        {
            cataLogImage = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER + File.separator + catalog.getCatalogType().getCatalogTypeImage();
        }
       
        else
        {
            
        }
        //System.out.println( "cataLog Image is  : " + cataLogImage );
        
        /*
        HttpServletResponse response = ServletActionContext.getResponse();
        response.reset();
        response.setContentType("multipart/form-data"); 

        bimage = catalog.getImage();

        OutputStream out = response.getOutputStream();
        out.write( bimage );
        out.flush();
        out.close();
        */
        
        
        File file = new File( cataLogImage );
        bimage = new byte[(int) file.length()];
        
        try 
        {
            FileInputStream fileInputStream = new FileInputStream( file );
            //convert file into array of bytes
            fileInputStream.read( bimage );
            fileInputStream.close();
       } 
       catch (Exception e) 
       {
            //e.printStackTrace();
       }
        
       try 
       {
           HttpServletResponse response = ServletActionContext.getResponse();
           response.reset();
           response.setContentType("multipart/form-data"); 

           OutputStream out = response.getOutputStream();
           out.write( bimage );
           out.flush();
           out.close();
       }
        
       catch (IOException e) 
       {
           //e.printStackTrace();
       }
        
        
        
        
        
        
        
        return SUCCESS;
    }
    
}
