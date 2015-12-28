package exceptions;

public class InvalidFileException extends IllegalArgumentException {
	public InvalidFileException(){
		super();
	}
	
	public InvalidFileException(String message){
		super(message);
	}
}
