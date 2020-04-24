package com.iiith.washeteria.exceptions;

public enum ExceptionCodes {
	
	TAMPERED_TOKEN("TAMPERED_TOKEN", "Could not verify signature of JWT because it has been tampered."),
	EXPIRED_TOKEN("EXPIRED_TOKEN", "This JWT has expired and is no longer acceptable."),
	MALFORMED_TOKEN("MALFORMED_TOKEN", "Could not parse JWT for verification. Illegal token format."),
	INVALID_CREDENTIALS("INVALID_CREDENTIALS","Invalid username or password. Use your IIIT-H credentials to login."),
	OVERLAPPING_EVENT("OVERLAPPING_EVENT","Event could not be created because it overlaps with an existing event."),
	ALL_SLOTS_OCCUPIED("ALL_SLOTS_OCCUPIED","Event could not be created because all slots are occupied");

	private final String code;
	private final String msg;

	ExceptionCodes(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return this.code;
	}

	public String getMsg() {
		return this.msg;
	}
}
