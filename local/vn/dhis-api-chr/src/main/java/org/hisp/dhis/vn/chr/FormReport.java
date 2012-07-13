package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.List;

public class FormReport
    implements java.io.Serializable
{

    private int id;

    private String name;

    private String formula;

    private List<Form> forms;

    private String operand;

    private Form mainForm;

    // ------------------------------------------------------------
    // Getters && Setters
    // ------------------------------------------------------------

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Form getMainForm()
    {
        return mainForm;
    }

    public void setMainForm( Form mainForm )
    {
        this.mainForm = mainForm;
    }

    public String getOperand()
    {
        return operand;
    }

    public void setOperand( String operand )
    {
        this.operand = operand;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getFormula()
    {
        return formula;
    }

    public void setFormula( String formula )
    {
        this.formula = formula;
    }

    public List<Form> getForms()
    {
        return forms;
    }

    public void setForms( List<Form> forms )
    {
        this.forms = forms;
    }

}
