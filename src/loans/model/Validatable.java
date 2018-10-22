package loans.model;

import java.util.List;


/**
 * Specifies that the implementing class can be validated in a standard way.
 *
 */
public interface Validatable {

	/**
	 * @param object the object to be validated
	 * @return a list of string errors describing issues with the object, or an empty list if no errors encountered
	 */
	public List<String> validate();
}
