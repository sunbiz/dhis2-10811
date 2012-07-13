package org.hisp.dhis.importexport.dhis14.object;

public class Dhis14CalculatedDataElementEntry
{
    private int calculatedDataElementId;
    
    private int dataElementId;
    
    private int factor;

    // ----------------------------------------------------------------------
    // Constructor
    // ----------------------------------------------------------------------
    
    public Dhis14CalculatedDataElementEntry()
    {   
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    public int getCalculatedDataElementId()
    {
        return calculatedDataElementId;
    }

    public void setCalculatedDataElementId( int calculatedDataElementId )
    {
        this.calculatedDataElementId = calculatedDataElementId;
    }

    public int getDataElementId()
    {
        return dataElementId;
    }

    public void setDataElementId( int dataElementId )
    {
        this.dataElementId = dataElementId;
    }

    public int getFactor()
    {
        return factor;
    }

    public void setFactor( int factor )
    {
        this.factor = factor;
    }
}
