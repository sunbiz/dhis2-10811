package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public class DefaultFormReportService
    implements FormReportService
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private FormReportStore formReportStore;

    // -----------------------------------------------------------------------------------------------
    // Getter && Setter
    // -----------------------------------------------------------------------------------------------

    public void setFormReportStore( FormReportStore formReportStore )
    {
        this.formReportStore = formReportStore;
    }

    // -----------------------------------------------------------------------------------------------
    // Implements
    // -----------------------------------------------------------------------------------------------

    public int addFormReport( FormReport formReport )
    {

        return formReportStore.addFormReport( formReport );
    }

    public void updateFormReport( FormReport formReport )
    {
        formReportStore.updateFormReport( formReport );

    }

    public void deleteFormReport( int id )
    {

        formReportStore.deleteFormReport( id );
    }

    public Collection<FormReport> getAllFormReports()
    {

        return formReportStore.getAllFormReports();
    }

    public Collection<FormReport> getFormReports( Form form )
    {

        return formReportStore.getFormReports( form );
    }

    public FormReport getFormReport( int id )
    {

        return formReportStore.getFormReport( id );
    }

}
