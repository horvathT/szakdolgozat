/**
 */
package DatabaseModeling;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.uml2.uml.Property;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>FK</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link DatabaseModeling.FK#getBase_Property <em>Base Property</em>}</li>
 *   <li>{@link DatabaseModeling.FK#getReferencedEntity <em>Referenced Entity</em>}</li>
 *   <li>{@link DatabaseModeling.FK#getReferencedProperty <em>Referenced Property</em>}</li>
 * </ul>
 *
 * @see DatabaseModeling.DatabaseModelingPackage#getFK()
 * @model
 * @generated
 */
public interface FK extends EObject {
	/**
	 * Returns the value of the '<em><b>Base Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Base Property</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Base Property</em>' reference.
	 * @see #setBase_Property(Property)
	 * @see DatabaseModeling.DatabaseModelingPackage#getFK_Base_Property()
	 * @model ordered="false"
	 * @generated
	 */
	Property getBase_Property();

	/**
	 * Sets the value of the '{@link DatabaseModeling.FK#getBase_Property <em>Base Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Base Property</em>' reference.
	 * @see #getBase_Property()
	 * @generated
	 */
	void setBase_Property(Property value);

	/**
	 * Returns the value of the '<em><b>Referenced Entity</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Entity</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Entity</em>' attribute.
	 * @see #setReferencedEntity(String)
	 * @see DatabaseModeling.DatabaseModelingPackage#getFK_ReferencedEntity()
	 * @model default="" dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getReferencedEntity();

	/**
	 * Sets the value of the '{@link DatabaseModeling.FK#getReferencedEntity <em>Referenced Entity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referenced Entity</em>' attribute.
	 * @see #getReferencedEntity()
	 * @generated
	 */
	void setReferencedEntity(String value);

	/**
	 * Returns the value of the '<em><b>Referenced Property</b></em>' attribute.
	 * The default value is <code>""</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Referenced Property</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Referenced Property</em>' attribute.
	 * @see #setReferencedProperty(String)
	 * @see DatabaseModeling.DatabaseModelingPackage#getFK_ReferencedProperty()
	 * @model default="" dataType="org.eclipse.uml2.types.String" ordered="false"
	 * @generated
	 */
	String getReferencedProperty();

	/**
	 * Sets the value of the '{@link DatabaseModeling.FK#getReferencedProperty <em>Referenced Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referenced Property</em>' attribute.
	 * @see #getReferencedProperty()
	 * @generated
	 */
	void setReferencedProperty(String value);

} // FK
