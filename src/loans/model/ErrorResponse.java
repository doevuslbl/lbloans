package loans.model;

import java.util.List;

public class ErrorResponse {
	
	private final List<String> errors;

	public ErrorResponse(List<String> errors) {
		super();
		this.errors = errors;
	}

	public List<String> getErrors() {
		return errors;
	}
}