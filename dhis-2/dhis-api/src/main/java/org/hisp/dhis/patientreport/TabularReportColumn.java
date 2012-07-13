package org.hisp.dhis.patientreport;

public class TabularReportColumn
{
    public static String PREFIX_META_DATA = "meta";    
    public static String PREFIX_IDENTIFIER_TYPE = "iden";
    public static String PREFIX_FIXED_ATTRIBUTE = "fixedAttr";    
    public static String PREFIX_PATIENT_ATTRIBUTE = "attr";
    public static String PREFIX_DATA_ELEMENT = "de";
    
    private String prefix;
    
    private String identifier;
    
    private boolean hidden;
    
    private String query;
    
    private String name;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------
    
    public TabularReportColumn()
    {
    }
    
    public TabularReportColumn( String prefix, String identifier, String name, boolean hidden, String query )
    {
        this.prefix = prefix;
        this.identifier = identifier;
        this.name = name;
        this.hidden = hidden;
        this.query = query;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------
    
    public boolean hasQuery()
    {
        return query != null;
    }

    public Integer getIdentifierAsInt()
    {
        return identifier != null ? Integer.parseInt( identifier ) : null;
    }

    public boolean isMeta()
    {
        return PREFIX_META_DATA.equals( prefix );
    }
    
    public boolean isIdentifierType()
    {
        return PREFIX_IDENTIFIER_TYPE.equals( prefix );
    }
    
    public boolean isFixedAttribute()
    {
        return PREFIX_FIXED_ATTRIBUTE.equals( prefix );
    }
    
    public boolean isDynamicAttribute()
    {
        return PREFIX_PATIENT_ATTRIBUTE.equals( prefix );
    }
    
    public boolean isDataElement()
    {
        return PREFIX_DATA_ELEMENT.equals( prefix );
    }

    // -------------------------------------------------------------------------
    // Get methods
    // -------------------------------------------------------------------------
    
    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix( String prefix )
    {
        this.prefix = prefix;
    }

    public String getIdentifier()
    {
        return identifier;
    }

    public void setIdentifier( String identifier )
    {
        this.identifier = identifier;
    }

    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden( boolean hidden )
    {
        this.hidden = hidden;
    }

    public String getQuery()
    {
        return query;
    }

    public void setQuery( String query )
    {
        this.query = query;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }
}
