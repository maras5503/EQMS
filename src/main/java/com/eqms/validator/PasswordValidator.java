package com.eqms.validator;

public class PasswordValidator {
	
	/**
	*	A description of the following regular expression:
	*		(?=.*[a-z])				-> must contain at least one lowercase character 
	*   	(?=.*[A-Z])				-> must contain at least one uppercase character
	*   	(?=.*\d)				-> must contain at least one digit from 0-9
	*   	(?=.*[^A-Za-z0-9 ])		-> must contain at least one special character
	*   	{7,}					-> must contain at least 7 characters
	*   The following regular expression can be written shorter:
	*   	"((?=.*\\w)(?=.*\\d)(?=.*[^A-Za-z0-9 ]).{7,})", where
	*   	\w		-> A word character: [a-zA-Z_0-9]
	*   	\d		-> A digit: [0-9]
    */
	private final String PASSWORD_PATTERN = "((?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9 ]).{7,})";
	public final static String PASSWORD_OK = "Password meets all requirements";
	public final static String PASSWORD_FAIL =
			  "- contain at least one lowercase character,<br />"
			+ "- contain at least one uppercase character,<br />"
			+ "- contain at least one digit,<br />"
			+ "- contain at least one special character,<br />"
			+ "- contain at least 7 characters.";
	
	public PasswordValidator() {
		super();
	}

	/**
	 * Returns true if password matches to the pattern, otherwise returns false.
	 * @param password
	 * @return
	 */
	public boolean validatePassword(final String password) {
		
		return password.matches(PASSWORD_PATTERN);
	}
	
}
