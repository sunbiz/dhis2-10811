package org.hisp.dhis.coldchain.reports;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ccemReport")
public class CCEMReport
{

    public static final String CATALOGTYPE_ATTRIBUTE_VALUE = "CATALOGTYPE_ATTRIBUTE_VALUE";
    public static final String CATALOGTYPE_ATTRIBUTE_VALUE_AGE_GROUP = "CATALOGTYPE_ATTRIBUTE_VALUE_AGE_GROUP";
    public static final String ORGUNITGROUP_DATAVALUE = "ORGUNITGROUP_DATAVALUE";
    public static final String ORGUNIT_EQUIPMENT_ROUTINE_DATAVALUE = "ORGUNIT_EQUIPMENT_ROUTINE_DATAVALUE";
    public static final String VACCINE_STORAGE_CAPACITY = "VACCINE_STORAGE_CAPACITY";
    
    public static final String LAST_YEAR = "LAST_YEAR";
    public static final String CURRENT_YEAR = "CURRENT_YEAR";
    public static final String LAST_6_MONTHS = "LAST_6_MONTHS";
    public static final String LAST_3_MONTHS = "LAST_3_MONTHS";
    
    private String reportId;
    
    private String reportName;
    
    private String xmlTemplateName;
    
    private String outputType;
    
    private String reportType;
    
    private String periodRequire;
    
    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public String getReportId()
    {
        return reportId;
    }
    public void setReportId( String reportId )
    {
        this.reportId = reportId;
    }
    public String getReportName()
    {
        return reportName;
    }
    public void setReportName( String reportName )
    {
        this.reportName = reportName;
    }
    public String getXmlTemplateName()
    {
        return xmlTemplateName;
    }
    public void setXmlTemplateName( String xmlTemplateName )
    {
        this.xmlTemplateName = xmlTemplateName;
    }
    public String getOutputType()
    {
        return outputType;
    }
    public void setOutputType( String outputType )
    {
        this.outputType = outputType;
    }
    public String getReportType()
    {
        return reportType;
    }
    public void setReportType( String reportType )
    {
        this.reportType = reportType;
    }
    public String getPeriodRequire()
    {
        return periodRequire;
    }
    public void setPeriodRequire( String periodRequire )
    {
        this.periodRequire = periodRequire;
    }
    
}
