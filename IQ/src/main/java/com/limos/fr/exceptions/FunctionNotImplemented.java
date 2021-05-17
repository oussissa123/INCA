package com.limos.fr.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FunctionNotImplemented extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FunctionNotImplemented() {
		super("Opups this function is not implemented ... (:");
	}
}
