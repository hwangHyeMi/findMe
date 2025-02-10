package mif.prunus.com.cmm;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import mif.prunus.front.cmm.MemberException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MemberException.class)
	public ResponseEntity<String> handleMemberException(MemberException e) {
		return new ResponseEntity<>(e.getMessage(), e.getStatus());
	}
}
