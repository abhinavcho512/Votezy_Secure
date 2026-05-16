package in.scalive.votezy.exception;

public class ResourceNotFoundException extends RuntimeException{ // exception by making it child=> runtimeexception =>checked na bne => na hi throws lgana pde=>or try catch se bch jaye


	public ResourceNotFoundException(String message) {
		super(message);  // parent ko call , parameterised constructor liya 
		
	}  
}
