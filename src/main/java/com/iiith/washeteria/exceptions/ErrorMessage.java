package com.iiith.washeteria.exceptions;

public class ErrorMessage extends Exception{
	
	private static final long serialVersionUID = 30091641349553958L;
	private String errorCode;
	private String message;
	
	public ErrorMessage() {
	}
	
	public ErrorMessage(ExceptionCodes exCode) {
		this.errorCode = exCode.getCode();
		this.message = exCode.getMsg();
	}
	

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
