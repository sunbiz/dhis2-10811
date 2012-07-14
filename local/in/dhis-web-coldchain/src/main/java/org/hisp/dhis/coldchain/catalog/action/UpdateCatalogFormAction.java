package org.hisp.dhis.coldchain.catalog.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogDataValue;
import org.hisp.dhis.coldchain.catalog.CatalogDataValueService;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.coldchain.catalog.CatalogType;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttribute;
import org.hisp.dhis.coldchain.catalog.CatalogTypeAttributeOption;
import org.hisp.dhis.coldchain.catalog.CatalogTypeService;
import org.hisp.dhis.coldchain.catalog.comparator.CatalogTypeAttributeOptionComparator;

import com.opensymphony.xwork2.Action;

public class UpdateCatalogFormAction
implements Action
{
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private CatalogService catalogService;
    
    public void setCatalogService( CatalogService catalogService )
    {
        this.catalogService = catalogService;
    }
    
    private CatalogTypeService catalogTypeService;
    
    public void setCatalogTypeService( CatalogTypeService catalogTypeService )
    {
        this.catalogTypeService = catalogTypeService;
    }
    
    private CatalogDataValueService catalogDataValueService;
    
    
    public void setCatalogDataValueService( CatalogDataValueService catalogDataValueService )
    {
        this.catalogDataValueService = catalogDataValueService;
    }


    // -------------------------------------------------------------------------
    // Input/Output and Getter / Setter
    // -------------------------------------------------------------------------

    private int id;
    
    public void setId( int id )
    {
        this.id = id;
    }

    private Catalog catalog;
    

    public Catalog getCatalog()
    {
        return catalog;
    }
    
    private Map<Integer, String> catalogTypeAttributeValueMap = new HashMap<Integer, String>();
    
    public Map<Integer, String> getCatalogTypeAttributeValueMap()
    {
        return catalogTypeAttributeValueMap;
    }

    private List<CatalogType> catalogTypes;
    
    public List<CatalogType> getCatalogTypes()
    {
        return catalogTypes;
    }
    /*
    private Collection<CatalogTypeAttribute> catalogTypeAttributes;
    
    public Collection<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
    */
    
    private List<CatalogTypeAttribute> catalogTypeAttributes = new ArrayList<CatalogTypeAttribute>();
    
    public List<CatalogTypeAttribute> getCatalogTypeAttributes()
    {
        return catalogTypeAttributes;
    }
    
    private String cataLogImage;
    
    
    private byte[] bimage;
    
    public byte[] getBimage()
    {
        return bimage;
    }
    
    private OutputStream outPutStream;
    
    public OutputStream getOutPutStream()
    {
        return outPutStream;
    }
    
    private BufferedImage bufferedImage;
    
    public BufferedImage getBufferedImage()
    {
        return bufferedImage;
    }
    
    private URL url;
    
    public URL getUrl()
    {
        return url;
    }


 
    public String getCataLogImage()
    {
        return cataLogImage;
    }
    
    private Map<Integer, List<CatalogTypeAttributeOption>> catalogTypeAttributesOptionsMap = new HashMap<Integer, List<CatalogTypeAttributeOption>>();
    
    public Map<Integer, List<CatalogTypeAttributeOption>> getCatalogTypeAttributesOptionsMap()
    {
        return catalogTypeAttributesOptionsMap;
    }
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------
    
    public String execute() throws Exception
    {
        
        catalog = catalogService.getCatalog( id );
        
        catalogTypes = new ArrayList<CatalogType>( catalogTypeService.getAllCatalogTypes());
        
        
        cataLogImage = catalog.getCatalogImage();
        
        //String outputFilePath = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER + File.separator + cataLogImage;
        
        System.out.println( "Catalog Image Name is   :" + cataLogImage );
        
        cataLogImage = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER + File.separator + cataLogImage;
        
        System.out.println( "Complete Path of Image  is   :" + cataLogImage );
        
        
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
        
        /*
        bimage = catalog.getImage();
        System.out.println( " Image  is   :" + bimage );
        System.out.println( " lenght is     :" + bimage.length );
        System.out.println( " String is     :" + bimage.toString() );
        try
        {
            
            int len = bimage.toString().length();
            byte [] rb = new byte[len];
            //InputStream readImg = rs.getBinaryStream(1);
            //int index=readImg.read(rb, 0, len);  
            outPutStream.write( rb,0,len );
            
            
            
            //outPutStream
            //FileOutputStream fos = new FileOutputStream("cataLogImage"); 
           // outPutStream = new FileOutputStream( cataLogImage ); 
            //outPutStream.write( bimage );
            //outPutStream.close();
        }
        catch(Exception e)
        {
            //e.printStackTrace();
        }
        
        */
        
        
        
        /*
        String filePath = cataLogImage;
        File f1 = new File( filePath );

        ImageInputStream imgStream1 = ImageIO.createImageInputStream( f1 );
        long size = imgStream1.length();

        
        //bufferedImage = ImageIO.read( f1 );
        
        BufferedImage bufferedImage1 = ImageIO.read( f1 );
        //boolean success = ImageIO.write( bufferedImage1,"gif",socket.getOutputStream());
        
        
        try 
        {
            url = new URL( getCodeBase(), cataLogImage );
            
            bufferedImage = ImageIO.read( url );
         } 
        catch (IOException e) 
        {
            
        }
        
        System.out.println( "IMAGE  is   :" + bufferedImage );
        System.out.println( "URL is   :" + url );
        
       
        //outPutStream.w.write( bufferedImage1 );
        
        */
        
        
        // -------------------------------------------------------------------------
        // Get catalog attribute values
        // -------------------------------------------------------------------------

        Catalog tempCatalog = catalogService.getCatalog( id );
        
        CatalogType catalogType = catalogTypeService.getCatalogType( tempCatalog.getCatalogType().getId() );
        
        //catalogTypeAttributes = catalogType.getCatalogTypeAttributes();
        
        catalogTypeAttributes = new ArrayList<CatalogTypeAttribute> ( catalogType.getCatalogTypeAttributes());
        //Collections.sort( catalogTypeAttributes, new CatalogTypeAttributeComparator() );
        
        List<CatalogDataValue> catalogDataValues = new ArrayList<CatalogDataValue>( catalogDataValueService.getAllCatalogDataValuesByCatalog( catalogService.getCatalog( id )) );
        
        
        for( CatalogDataValue catalogDataValue : catalogDataValues )
        {
            if ( CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( catalogDataValue.getCatalogTypeAttribute().getValueType() ) )
            {
                catalogTypeAttributeValueMap.put( catalogDataValue.getCatalogTypeAttribute().getId(), catalogDataValue.getCatalogTypeAttributeOption().getName() );
            }
            
            else
            {
                catalogTypeAttributeValueMap.put( catalogDataValue.getCatalogTypeAttribute().getId(), catalogDataValue.getValue() );
            }
        }
       /*
        System.out.println( "Size of catalog Data Values Map  :" + catalogTypeAttributeValueMap.size() );
        for( CatalogDataValue  tempcatalogDataValue  : catalogDataValues )
        {
            System.out.println( "Map value is ------- :" + catalogTypeAttributeValueMap.get( tempcatalogDataValue.getCatalogTypeAttribute().getId() ));
            
        }
        */
        for( CatalogTypeAttribute catalogTypeAttribute : catalogTypeAttributes )
        {
            List<CatalogTypeAttributeOption> catalogTypeAttributesOptions = new ArrayList<CatalogTypeAttributeOption>();
            if( CatalogTypeAttribute.TYPE_COMBO.equalsIgnoreCase( catalogTypeAttribute.getValueType() ) )
            {
                catalogTypeAttributesOptions = new ArrayList<CatalogTypeAttributeOption>( catalogTypeAttribute.getAttributeOptions() );
                Collections.sort( catalogTypeAttributesOptions, new CatalogTypeAttributeOptionComparator() );
                catalogTypeAttributesOptionsMap.put( catalogTypeAttribute.getId(), catalogTypeAttributesOptions );
            }
        }
        
        return SUCCESS;

    }
/*
    private URL getCodeBase()
    {
        //URL url = null;
        try 
        {
            url = new URL( "catalog.getCatalogImage()" );
        } 
        catch (IOException e) 
        {
            
        }
        System.out.println( "URL in method is   :" + url );
        return url;
    }
*/
}

