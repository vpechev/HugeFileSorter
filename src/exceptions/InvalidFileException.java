package exceptions;

public class InvalidFileException extends IllegalArgumentException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7216550049133391890L;

	public InvalidFileException(){
		super();
	}
	
	public InvalidFileException(String message){
		super(message);
	}
}
