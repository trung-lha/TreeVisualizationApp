package exception;

public class ExceptionForProject extends Exception {

	public ExceptionForProject() {
		// TODO Auto-generated constructor stub
	}

	public ExceptionForProject(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public String notification() {
		return "The project encountered an exception !";
	}

}
