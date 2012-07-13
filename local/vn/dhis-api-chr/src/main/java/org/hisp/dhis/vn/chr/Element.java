package org.hisp.dhis.vn.chr;


/**
 * @author Chau Thu Tran
 * 
 */

public class Element
    implements java.io.Serializable
{

    public static final String INTEGER = "INTEGER";

    public static final String STRING = "VARCHAR";

    public static final String DATE = "DATE";

    public static final String DATETIME = "timestamp with time zone";

    public static final String DOUBLE = "NUMERIC";

    public static final String BOOLEAN = "BOOLEAN";

    public static final String SERIAL = "SERIAL";

    /**
     * The database internal identifier for this DataElement.
     */
    private int id;

    /**
     * The Form to which Element belongs.
     */
    private Form form;

    /**
     * The name of this DataElement. Required and unique.
     */
    private String name;

    /**
     * The label of this DataElement. Required and unique.
     */
    private String label;

    /**
     * The type of this DataElement; e.g. DataElement.TYPE_INT,
     * DataElement.TYPE_BOOL, DataElement.TYPE_STRING, DataElement.NUMBER or
     * DataElement.DATE
     */
    private String type;

    /**
     * The Control Type of this DataElement.
     */
    private String controlType;

    /**
     * The Init value of this DataElement.
     */
    private String initialValue;

    /**
     * The formLink of this DataElement.
     */
    private Form formLink;

    /**
     * The DataElement is null or not.
     */
    private boolean required;

    /**
     * The order number of this DataElement.
     */
    private int sortOrder;

    /**
     * The ID group of this DataElement.
     */
    private Egroup egroup;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Element()
    {
    }

    public Element( Form form, String name, String label, String type, String controlType, String initialValue,
        Form formLink, boolean required, int sortOrder, Egroup egroup )
    {
        this.form = form;
        this.name = name;
        this.label = label;
        this.type = type;
        this.controlType = controlType;
        this.initialValue = initialValue;
        this.formLink = formLink;
        this.required = required;
        this.sortOrder = sortOrder;
        this.egroup = egroup;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals and toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }

        if ( o == null )
        {
            return false;
        }

        if ( !(o instanceof Element) )
        {
            return false;
        }

        final Element other = (Element) o;

        return name.equals( other.getName() );
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    public boolean isRequired()
    {
        return required;
    }

    public void setRequired( boolean required )
    {
        this.required = required;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public Form getForm()
    {
        return form;
    }

    public void setForm( Form form )
    {
        this.form = form;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel( String label )
    {
        this.label = label;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getControlType()
    {
        return controlType;
    }

    public void setControlType( String controlType )
    {
        this.controlType = controlType;
    }

    public String getInitialValue()
    {
        return initialValue;
    }

    public void setInitialValue( String initialValue )
    {
        this.initialValue = initialValue;
    }

    public Form getFormLink()
    {
        return formLink;
    }

    public void setFormLink( Form formLink )
    {
        this.formLink = formLink;
    }

    public int getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( int sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    public Egroup getEgroup()
    {
        return egroup;
    }

    public void setEgroup( Egroup egroup )
    {
        this.egroup = egroup;
    }

}
