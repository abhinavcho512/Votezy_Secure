//package in.scalive.votezy.exception;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.FieldError;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//@ControllerAdvice  // like global catch
//public class GlobalExceptionHandler {
//	
//	// different methods =>
//	
//	
//	@ExceptionHandler(ResourceNotFoundException.class)  // EH leta h .file class ka naam
//	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex)
//	{
//		ErrorResponse err=new ErrorResponse(HttpStatus.NOT_FOUND.value(),ex.getMessage());  //errorResponse ka object bnaya=> return 2 thing => statusCode an error Message 
//		
//        return new ResponseEntity<>(err,HttpStatus.NOT_FOUND);
//		
//	}
//	
//	@ExceptionHandler(DuplicateResourceException.class)  // EH leta h .file class ka naam
//	public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex)
//	{
//		ErrorResponse err=new ErrorResponse(HttpStatus.CONFLICT.value(),ex.getMessage());  //errorResponse ka object bnaya=> return 2 thing => statusCode an error Message 
//		
//        return new ResponseEntity<>(err,HttpStatus.CONFLICT);
//		
//	}
//	
//	@ExceptionHandler(VoteNotAllowedException.class)  // EH leta h .file class ka naam
//	public ResponseEntity<ErrorResponse> handleVoteNotAllowedException(VoteNotAllowedException ex)
//	{
//		ErrorResponse err=new ErrorResponse(HttpStatus.FORBIDDEN.value(),ex.getMessage());  //errorResponse ka object bnaya=> return 2 thing => statusCode an error Message 
//		
//        return new ResponseEntity<>(err,HttpStatus.FORBIDDEN);
//		
//	}
//	
//	// IF VALIDATION FAILS (difficult)
//
//	@ExceptionHandler(MethodArgumentNotValidException.class)  
//	public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
//	{
//		Map<String,String>errors=new HashMap<>();
//		BindingResult bResult=ex.getBindingResult();  // have list of errors
//		List<FieldError> errorList=bResult.getFieldErrors();
//		for(FieldError error:errorList)  // ek ek error ko lege
//		{
//			errors.put(error.getField(), error.getDefaultMessage());
//		}
//		
//        return new ResponseEntity<>(errors,HttpStatus.BAD_REQUEST);
//		
//	}
//
//	@ExceptionHandler(Exception.class)  // EH leta h .file class ka naam
//	public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex)
//	{
//		ErrorResponse err=new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getMessage());  //errorResponse ka object bnaya=> return 2 thing => statusCode an error Message 
//		
//        return new ResponseEntity<>(err,HttpStatus.INTERNAL_SERVER_ERROR);
//		
//	}
//	
//
//}
package in.scalive.votezy.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex)
    {
        ErrorResponse err = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateResourceException(DuplicateResourceException ex)
    {
        ErrorResponse err = new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(VoteNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleVoteNotAllowedException(VoteNotAllowedException ex)
    {
        ErrorResponse err = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
    {
        Map<String, String> errors = new HashMap<>();
        BindingResult bResult = ex.getBindingResult();
        List<FieldError> errorList = bResult.getFieldErrors();
        for (FieldError error : errorList)
        {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    // ✅ NEW: handles wrong password / email not found from AuthService
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex)
    {
        ErrorResponse err = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex)
    {
        ErrorResponse err = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}