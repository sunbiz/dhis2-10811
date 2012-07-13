package org.hisp.dhis.coldchain.equipment.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.hisp.dhis.coldchain.inventory.EquipmentDataValue;
import org.hisp.dhis.coldchain.inventory.EquipmentDataValueService;
import org.hisp.dhis.coldchain.inventory.EquipmentInstance;
import org.hisp.dhis.coldchain.inventory.EquipmentInstanceService;
import org.hisp.dhis.dataelement.DataElement;
import org.hisp.dhis.dataset.DataSet;
import org.hisp.dhis.dataset.DataSetService;
import org.hisp.dhis.organisationunit.OrganisationUnit;
import org.hisp.dhis.period.Period;
import org.hisp.dhis.period.PeriodType;
import org.hisp.dhis.user.CurrentUserService;

import com.opensymphony.xwork2.Action;

public class SaveEquipmentDataValueAction implements Action
{
    private static final Log log = LogFactory.getLog( SaveEquipmentDataValueAction.class );
    
    public static final String PREFIX_DATAELEMENT = "dataelement";
    // -------------------------------------------------------------------------
    // Dependencies
    // -------------------------------------------------------------------------
    
    private EquipmentInstanceService equipmentInstanceService;
    
    public void setEquipmentInstanceService( EquipmentInstanceService equipmentInstanceService )
    {
        this.equipmentInstanceService = equipmentInstanceService;
    }
    
    private DataSetService dataSetService;
    
    public void setDataSetService( DataSetService dataSetService )
    {
        this.dataSetService = dataSetService;
    }
    
    private CurrentUserService currentUserService;

    public void setCurrentUserService( CurrentUserService currentUserService )
    {
        this.currentUserService = currentUserService;
    }
    
    private EquipmentDataValueService equipmentDataValueService;
    
    public void setEquipmentDataValueService( EquipmentDataValueService equipmentDataValueService )
    {
        this.equipmentDataValueService = equipmentDataValueService;
    }
    
    // -------------------------------------------------------------------------
    // Input
    // -------------------------------------------------------------------------
    

    private int selectedDataSetId;
    
    public void setSelectedDataSetId( int selectedDataSetId )
    {
        this.selectedDataSetId = selectedDataSetId;
    }
    
    private int equipmentInstanceId;
    
    public void setEquipmentInstanceId( int equipmentInstanceId )
    {
        this.equipmentInstanceId = equipmentInstanceId;
    }
    
    private String selectedPeriodId;
    
    public void setSelectedPeriodId( String selectedPeriodId )
    {
        this.selectedPeriodId = selectedPeriodId;
    }

    
    // -------------------------------------------------------------------------
    // Output
    // -------------------------------------------------------------------------

    private int statusCode = 0;

    public int getStatusCode()
    {
        return statusCode;
    }  
   
    
    // -------------------------------------------------------------------------
    // Action implementation
    // -------------------------------------------------------------------------

    public String execute()
    {
        Period period = PeriodType.createPeriodExternalId( selectedPeriodId );
        
        if ( period == null )
        {
            return logError( "Illegal period identifier: " + selectedPeriodId );
        }
        
        EquipmentInstance equipmentInstance = equipmentInstanceService.getEquipmentInstance( equipmentInstanceId );
        OrganisationUnit organisationUnit = equipmentInstance.getOrganisationUnit();
        
        DataSet dataSet = dataSetService.getDataSet( selectedDataSetId );
        
       
        if ( dataSetService.isLocked( dataSet, period, organisationUnit, null ) )
        {
            return logError( "Entry locked for combination: " + dataSet + ", " + period + ", " + organisationUnit, 2 );
        }

        
        
        List<DataElement> dataElements = new ArrayList<DataElement>( dataSet.getDataElements() );
        
        String storedBy = currentUserService.getCurrentUsername();
        
        Date timestamp = new Date();
        
        if ( storedBy == null )
        {
            storedBy = "[unknown]";
        }
        
        
        // ---------------------------------------------------------------------
        // Add / Update data
        // ---------------------------------------------------------------------
        HttpServletRequest request = ServletActionContext.getRequest();
        
        //String value = null;
        
        if ( dataElements != null && dataElements.size() > 0 )
        {
            for ( DataElement dataElement : dataElements )
            {
                EquipmentDataValue equipmentDataValue = equipmentDataValueService.getEquipmentDataValue( equipmentInstance, period, dataElement );
                
                String value = request.getParameter( PREFIX_DATAELEMENT + dataElement.getId() );
                
                if ( value != null && value.trim().length() == 0 )
                {
                    value = null;
                }

                if ( value != null )
                {
                    value = value.trim();
                }
                
                if ( equipmentDataValue == null && value != null )
                {
                    equipmentDataValue = new EquipmentDataValue();
                    
                    equipmentDataValue.setValue( value );
                    equipmentDataValue.setDataElement( dataElement );
                    equipmentDataValue.setEquipmentInstance( equipmentInstance );
                    equipmentDataValue.setPeriod( period );
                    equipmentDataValue.setStoredBy( storedBy );
                    equipmentDataValue.setTimestamp( timestamp );
                    equipmentDataValueService.addEquipmentDataValue( equipmentDataValue );
                }                
                else
                {
                    equipmentDataValue.setDataElement( dataElement );
                    equipmentDataValue.setValue( value );
                    equipmentDataValue.setEquipmentInstance( equipmentInstance );
                    equipmentDataValue.setPeriod( period );
                    equipmentDataValue.setStoredBy( storedBy );
                    equipmentDataValue.setTimestamp( timestamp );
                    equipmentDataValueService.updateEquipmentDataValue( equipmentDataValue );
                }
            }
                 
        }
        
        return SUCCESS;
    }
    
    // -------------------------------------------------------------------------
    // Supportive methods
    // -------------------------------------------------------------------------

    private String logError( String message )
    {
        return logError( message, 1 );
    }

    private String logError( String message, int statusCode )
    {
        log.info( message );

        this.statusCode = statusCode;

        return SUCCESS;
    }
    
    
}
