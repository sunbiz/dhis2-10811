package org.hisp.dhis.coldchain.catalog.action;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Blob;

import org.hibernate.Hibernate;
import org.hisp.dhis.coldchain.catalog.Catalog;
import org.hisp.dhis.coldchain.catalog.CatalogService;
import org.hisp.dhis.system.util.StreamUtils;

import com.opensymphony.xwork2.Action;

public class UploadCatalogImageAction implements Action
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
    // Input/Output
    // -------------------------------------------------------------------------
    
    private String message;

    public String getMessage()
    {
        return message;
    }

    public void setMessage( String message )
    {
        this.message = message;
    }
    
    private int catalogID;
    
    public void setCatalogID( int catalogID )
    {
        this.catalogID = catalogID;
    }

    private String name;
    
    public void setName( String name )
    {
        this.name = name;
    }
    
    
    
    /*
    private File file;
    
    public File getFile()
    {
        return file;
    }

    public void setFile( File file )
    {
        this.file = file;
    }
    
    private String fileName;
    
    public String getFileName()
    {
        return fileName;
    }

    public void setFileName( String fileName )
    {
        this.fileName = fileName;
    }
    
    
    private File file;

    public void setUpload( File file )
    {
        this.file = file;
    }
    
    private File upload;

    public File getUpload()
    {
        return upload;
    }
    
    
    private String fileName;
    
    public void setUploadFileName( String fileName )
    {
        this.fileName = fileName;
    }
    /*
     * 
     * 
     * 
     */
    /*
    private String tempFileName;
    
    public void setTempFileName( String tempFileName )
    {
        this.tempFileName = tempFileName;
    }

    private File upload;
    
    public void setUpload( File upload )
    {
        this.upload = upload;
    }
    
    private File file;

    public void setUpload( File file )
    {
        this.file = file;
    }
    */
    private String fileName;

    private File upload;
    
    public void setUploadFileName( String fileName )
    {
        this.fileName = fileName;
    }
    
   
    public void setUpload( File upload )
    {
        this.upload = upload;
    }
    
    private File outputFile;
    
    String imageName = "";
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------


    public String execute() throws Exception
    {
        outputFile = null;
        
        Catalog catalog = catalogService.getCatalog( catalogID );
        
        
        //File directory = null;
        
        String fileType = fileName.substring(fileName.indexOf( '.' )+1, fileName.length());
        
        
        System.out.println( "File Name is : " + fileName +" File Type is : " +  fileType );
        
        System.out.println( "UPLOAD is  : " + upload );
        
        
        
        if ( ! ( fileType.equalsIgnoreCase( "jpg" ) || fileType.equalsIgnoreCase( "png" ) || fileType.equalsIgnoreCase( "gif" ) ||  fileType.equalsIgnoreCase( "jpeg" ) ||  fileType.equalsIgnoreCase( "tiff" ) || fileType.equalsIgnoreCase( "bmp" ) ) )
        //if ( !fileType.equalsIgnoreCase( "jpg" ) || !fileType.equalsIgnoreCase( "png" ) || !fileType.equalsIgnoreCase( "gif" ) || !fileType.equalsIgnoreCase( "bmp" ) )
        {
            message = "The file you are trying to import is not an image file";
            
            return SUCCESS;
        }
        
        imageName = catalog.getName()+"."+ fileType;
        
        catalog.setCatalogImage( imageName );
        //catalog.setCatalogImage( fileName );
        catalogService.updateCatalog( catalog );
        
        
       
        
        String outputFilePath = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER;
        
        File newdir = new File( outputFilePath );
        if( !newdir.exists() )
        {
            newdir.mkdirs();
        }
        
        outputFile = new File( newdir, imageName );
        try
        {        
            StreamUtils.write( upload, outputFile );
            
            message = "Image successfully uploaded";
        }
        
        catch( Exception e )
        {
            /*
            e.printStackTrace();
            message += "<br><font color=red><strong>Some problem occured while Importing the file : "+ upload.getName() + "<br>Error Message: "+e.getMessage()+"</font></strong>";
            */
        } 

        //String  cataLogImage = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER + File.separator + catalog.getCatalogImage();
        

        //FileInputStream fis;
        
        /*
        
        File file = new File( cataLogImage );
        byte[] bFile = new byte[(int) file.length()];
      
        
        try 
        {
            FileInputStream fileInputStream = new FileInputStream(file);
            //convert file into array of bytes
            fileInputStream.read(bFile);
            fileInputStream.close();
       } 
       catch (Exception e) 
       {
            e.printStackTrace();
       }
       
       catalog.setImage( bFile );
       
       catalogService.updateCatalog( catalog ); 
      
      */ 
        
        //fis = new FileInputStream(image);
        
        
       // catalog.setImage(  image );
        
        /*
        catalog.setBinaryStream( image );
        
        catalog.setBinaryStream(InputStream)fis, (int)(image.length()));
         */
        
        
        
        
        //System.out.println( "File Name is : " + upload.getName() );
        
        
        
        //outputFile = new File( directory, (Math.random() * 1000) + fileName );
        
        //String outputFilePathTest = System.getenv( "DHIS2_HOME" ) + File.separator +  Catalog.DEFAULT_CCEMFOLDER + File.separator + tempFileName;
        
       
       /*
       File newdir = new File( outputFilePath );
        if( !newdir.exists() )
        {
            newdir.mkdirs();
        }
        */
       
        //outputFile = new File( directory, (Math.random() * 1000) + fileName );
        
       // file.renameTo( outputFile );
        
        /*
        File output = null;
        output = new File( outputFilePath, outputFilePathTest );
        */
        //InputStream inputStream = null;
        
        //inputStream = new BufferedInputStream( new FileInputStream( file ) );
        
        //outputFile = new File( newdir, (Math.random() * 1000) + fileName );
        
        //outputFile = new File( newdir, fileName );
        
       /* outputFile = new File( newdir, imageName );*/
        /*try
        {  */      
            //outputFile = new File( outputFilePathTest );
            
            //outputFile = new File( newdir, fileName );
            
            //outputFile = new File( newdir, (Math.random() * 1000) + fileName );
            
            
          /*  StreamUtils.write( upload, outputFile ); */
            
            //System.out.println( " outputFile : " + outputFile );
            
           // System.out.println( "File Name is : " + upload.getName() );
            
            //StreamUtils.write( upload, outputFile );
            
            
            //System.out.println( "File Name is : " + file.getPath() );
          //  System.out.println( "File Name is : " + fileName );
            
            //System.out.println( "File Name is : " + tempFileName );
            
            //String fileType = fileName.substring(fileName.indexOf( '.' )+1, fileName.length());
            
            //String fileType = fileName.substring(fileName.indexOf( '.' )+1, upload.getName().length());
            
           // System.out.println( "File Name is : " + fileName +" File Type is : " +  fileType );
            
           /*
           message = "Image successfully uploaded";
        }
        */
        /*
       catch( Exception e )
        {
            
            e.printStackTrace();
            message += "<br><font color=red><strong>Some problem occured while Importing the file : "+ upload.getName() + "<br>Error Message: "+e.getMessage()+"</font></strong>";
            
        } 
        */
        
        /*
        String filePath = c:\images\+ "button1.gif";
        File f1 = new File(filePath);

        ImageInputStream imgStream1 = ImageIO.createImageInputStream(f1);
        long size = imgStream1.length();

        BufferedImage bufferedImage1 = ImageIO.read(f1);
        boolean success = ImageIO.write(bufferedImage1,"gif",socket.getOutputStream());

        socketWriter.close();
        socketReader.close();
        socket.close();
        socket = null;
        */
        
        
        
        
        return SUCCESS;
    }
  

}
