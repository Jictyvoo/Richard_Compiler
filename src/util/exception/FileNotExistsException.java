package util.exception;

public class FileNotExistsException extends Exception {
	private static final long serialVersionUID = -695014618725587057L;

	public FileNotExistsException(String message) {
        super(message);
    }
}
