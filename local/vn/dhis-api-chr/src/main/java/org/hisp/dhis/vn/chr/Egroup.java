package org.hisp.dhis.vn.chr;

/**
 * @author Chau Thu Tran
 * 
 */

import java.util.Collection;

public class Egroup implements java.io.Serializable {

	/**
	 * The database internal identifier for this DataElement.
	 */
	private int id;

	/**
	 * The name of this DataElement. Required and unique.
	 */
	private String name;

	/**
	 * The Form to which Egroup belongs.
	 */
	private Form form;

	/**
	 * The order number of this Egroup.
	 */
	private int sortOrder;

	/**
	 * Element Group of the EGroup
	 */
	private Collection<Element> elements;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	public Egroup() {
	}

	// -------------------------------------------------------------------------
	// hashCode, equals and toString
	// -------------------------------------------------------------------------

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o == null) {
			return false;
		}

		if (!(o instanceof Egroup)) {
			return false;
		}

		final Egroup other = (Egroup) o;

		return name.equals(other.getName());
	}

	@Override
	public String toString() {
		return "[" + name + "]";
	}

	// -------------------------------------------------------------------------
	// Getters && Setters
	// -------------------------------------------------------------------------

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Form getForm() {
		return form;
	}

	public void setForm(Form form) {
		this.form = form;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(int sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Collection<Element> getElements() {
		return elements;
	}

	public void setElements(Collection<Element> elements) {
		this.elements = elements;
	}

}
