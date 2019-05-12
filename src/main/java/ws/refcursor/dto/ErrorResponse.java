package ws.refcursor.dto;

import ws.refcursor.util.ErrorCodes;

public class ErrorResponse {

	ErrorCodes.ERROR errorCode;
	String errorMsg;
	String errorValue;
	
	public ErrorResponse(ErrorCodes.ERROR errorCode, String errorValue) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = ErrorCodes.getErrorMsg(errorCode);
		this.errorValue = errorValue;
	}
	
	public ErrorResponse(ErrorCodes.ERROR errorCode, String errorMsg, String errorValue) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.errorValue = errorValue;
	}

	public ErrorCodes.ERROR getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCodes.ERROR errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorValue() {
		return errorValue;
	}

	public void setErrorValue(String errorValue) {
		this.errorValue = errorValue;
	}

	@Override
	public String toString() {
		return "ErrorResponse [errorCode=" + errorCode + ", errorMsg="
				+ errorMsg + ", errorValue=" + errorValue + "]";
	}
	
}
