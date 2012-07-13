package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public class DefaultFormService
    implements FormService
{

    // -----------------------------------------------------------------------------------------------
    // Dependencies
    // -----------------------------------------------------------------------------------------------

    private FormStore formStore;

    // -----------------------------------------------------------------------------------------------
    // Getter && Setter
    // -----------------------------------------------------------------------------------------------

    public void setFormStore( FormStore formStore )
    {
        this.formStore = formStore;
    }

    // -----------------------------------------------------------------------------------------------
    // Implements
    // -----------------------------------------------------------------------------------------------

    public Form getForm( int id )
    {
        return this.formStore.getForm( id );
    }

    public int addForm( Form form )
    {
        return this.formStore.addForm( form );
    }

    public void deleteForm( int id )
    {
        this.formStore.deleteForm( id );

    }

    public Collection<Form> getAllForms()
    {

        return this.formStore.getAllForms();
    }

    public Collection<Form> getVisibleForms( boolean visible )
    {

        return formStore.getVisibleForms( visible );
    }

    public void updateForm( Form form )
    {

        formStore.updateForm( form );

    }

    public Collection<Form> getFormsByName( String name )
    {
        return formStore.getFormsByName( name );
    }

    public Collection<Form> getCreatedForms()
    {
        return formStore.getCreatedForms();
    }

    public Form getFormByName( String name )
    {
        return formStore.getFormByName( name );
    }

    // //
    // --------------------------------------------------------------------------------------------
    // // Create Table from Form
    // //
    // --------------------------------------------------------------------------------------------
    // public void createFormTable(Form form ){
    // formStore.createFormTable(form);
    // }
    //	    
    // //
    // --------------------------------------------------------------------------------------------
    // // Remove Table from Form
    // //
    // --------------------------------------------------------------------------------------------
    //	
    // public void removeFormTable( Form form ){
    // formStore.removeFormTable(form);
    // }
}
