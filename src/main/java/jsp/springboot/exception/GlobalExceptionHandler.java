package jsp.springboot.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import jsp.springboot.dto.ResponseStructure;


@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
	@ExceptionHandler(IdNotFoundException.class)
	public ResponseEntity<ResponseStructure<String>> handleINFE(IdNotFoundException e){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(e.getMessage());
		res.setData("Failure");
		
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(NoRecordAvailableException.class)
	public ResponseEntity<ResponseStructure<String>> handleNRAE(NoRecordAvailableException e){
		ResponseStructure<String> res=new ResponseStructure<>();
		res.setStatusCode(HttpStatus.NOT_FOUND.value());
		res.setMessage(e.getMessage());
		res.setData("Failure");
		
		return new ResponseEntity<>(res,HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(DuplicateIfscException.class)
	public ResponseEntity<ResponseStructure<String>> handleDIE(DuplicateIfscException e) {

		ResponseStructure<String> res = new ResponseStructure<>();

		res.setStatusCode(HttpStatus.CONFLICT.value());
		res.setMessage(e.getMessage());
		res.setData("Failure");

		return new ResponseEntity<>(res, HttpStatus.CONFLICT);

	}
	
	@ExceptionHandler(DuplicateContactNumberException.class)
	public ResponseEntity<ResponseStructure<String>> handleDCNE(DuplicateContactNumberException e) {

	    ResponseStructure<String> res = new ResponseStructure<>();

	    res.setStatusCode(HttpStatus.CONFLICT.value());
	    res.setMessage(e.getMessage());
	    res.setData("Failure");

	    return new ResponseEntity<>(res, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DuplicateAccountNumberException.class)
	public ResponseEntity<ResponseStructure<String>> handleDANE(DuplicateAccountNumberException e){

		ResponseStructure<String> res=new ResponseStructure<>();

		res.setStatusCode(HttpStatus.CONFLICT.value());
		res.setMessage(e.getMessage());
		res.setData("Failure");

		return new ResponseEntity<ResponseStructure<String>>(res,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(InsufficientBalanceException.class)
	public ResponseEntity<ResponseStructure<String>> handleIBE(InsufficientBalanceException e){

		ResponseStructure<String> res=new ResponseStructure<>();

		res.setStatusCode(HttpStatus.CONFLICT.value());
		res.setMessage(e.getMessage());
		res.setData("Failure");

		return new ResponseEntity<ResponseStructure<String>>(res,HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(MinimumBalanceException.class)
	public ResponseEntity<ResponseStructure<String>> minimumBalanceException(
	        MinimumBalanceException ex){

		ResponseStructure<String> res = new ResponseStructure<>();

		res.setStatusCode(HttpStatus.BAD_REQUEST.value());
		res.setMessage("Minimum Balance Exception");
		res.setData(ex.getMessage());

		return new ResponseEntity<>(res,HttpStatus.BAD_REQUEST);
	}

}
