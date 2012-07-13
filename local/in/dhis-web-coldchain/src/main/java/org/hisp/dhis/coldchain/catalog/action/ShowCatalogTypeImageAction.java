package org.hisp.dhis.coldchain.catalog.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;

import com.opensymphony.xwork2.Action;

public class ShowCatalogTypeImageAction implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------

    private CatalogTypeService catalogTypeService;
    
    public void setCatalogTypeService( CatalogTypeService catalogTypeService )
    {
        this.catalogTypeService = catalogTypeService;
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
        
        CatalogType catalogType = catalogTypeService.getCatalogType( id );
        
        if ( catalogType.getCatalogTypeImage() != null )
        {
            cataLogImage = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER + File.separator + catalogType.getCatalogTypeImage();
        }
        
        else
        {
            
        }
       
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


