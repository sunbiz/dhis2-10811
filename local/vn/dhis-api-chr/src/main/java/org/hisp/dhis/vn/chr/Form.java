package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public class Form
    implements java.io.Serializable
{

    /**
     * The database internal identifier for this Form.
     */
    private int id;

    /**
     * The name of this Form. Required and unique.
     */
    private String name;

    /**
     * The label of this Form. Required and unique.
     */
    private String label;

    /**
     * The number of columns of this Form
     */
    private int noColumn;

    /**
     * The number of columns of this FormLink
     */
    private int noColumnLink;

    /**
     * The icon of this Form
     */
    private String icon;

    /**
     * The visible Form
     */
    private boolean visible;

    /**
     * If the form is created, created property is true
     */
    private boolean created;

    /**
     * Element Group of the Form x
     */
    private Collection<Element> elements;

    /**
     * Sort Egroup List of the Form
     */
    private Collection<Egroup> egroups;

    // private Set<FormReport> formReports;
    //	
    // public Set<FormReport> getFormReports() {
    // return formReports;
    // }
    //
    // public void setFormReports(Set<FormReport> formReports) {
    // this.formReports = formReports;
    // }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public Form()
    {
    }

    public Form( String name, String label, int noColumn, int noColumnLink, boolean visible, boolean created )
    {
        this.name = name;
        this.label = label;
        this.noColumn = noColumn;
        this.noColumnLink = noColumnLink;
        this.visible = visible;
        this.created = created;
    }

    public Form( String name, String label, int noColumn, int noColumnLink, String icon, boolean visible,
        boolean created, Collection<Element> elements, Collection<Egroup> egroups )
    {
        this.name = name;
        this.label = label;
        this.noColumn = noColumn;
        this.noColumnLink = noColumnLink;
        this.icon = icon;
        this.visible = visible;
        this.created = created;
        this.elements = elements;
        this.egroups = egroups;
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

        if ( !(o instanceof Form) )
        {
            return false;
        }

        final Form other = (Form) o;

        return name.equals( other.getName() );
    }

    @Override
    public String toString()
    {
        return "[" + name + "]";
    }

    // -------------------------------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------------------------------

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
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

    public int getNoColumn()
    {
        return noColumn;
    }

    public void setNoColumn( int noColumn )
    {
        this.noColumn = noColumn;
    }

    public int getNoColumnLink()
    {
        return noColumnLink;
    }

    public void setNoColumnLink( int noColumnLink )
    {
        this.noColumnLink = noColumnLink;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon( String icon )
    {
        this.icon = icon;
    }

    public boolean getVisible()
    {
        return visible;
    }

    public void setVisible( boolean visible )
    {
        this.visible = visible;
    }

    public boolean isCreated()
    {
        return created;
    }

    public void setCreated( boolean created )
    {
        this.created = created;
    }

    public Collection<Element> getElements()
    {
        return elements;
    }

    public void setElements( Collection<Element> elements )
    {
        this.elements = elements;
    }

    public Collection<Egroup> getEgroups()
    {
        return egroups;
    }

    public void setEgroups( Collection<Egroup> egroups )
    {
        this.egroups = egroups;
    }

}
