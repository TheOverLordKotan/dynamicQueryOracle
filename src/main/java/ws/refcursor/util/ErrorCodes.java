package ws.refcursor.util;

public class ErrorCodes {

	public enum ERROR {
		REQUEST_OVERLIMIT, RESPONSE_OVERLIMIT, CODE_ERROR, NO_REQUEST_DATA, INVALID_REQUEST_DATA,
		INVALID_LENGTH, INVALID_NUMBER, INVALID_DATE, MANDATORY_FIELD_EMPTY, NULL_OBJECT, NO_DATA_FOUND, INVALID_LIST 
	}
	
	public static String getErrorMsg(ERROR errorCode) {
		String errorMsg;
        switch (errorCode) {
                                
            case REQUEST_OVERLIMIT:
            	errorMsg = "Request size exceeded limit. Please reduce request size and try again";
                break;
                         
            case RESPONSE_OVERLIMIT:
            	errorMsg = "Response size exceeded limit. Please reduce request size and try again";
                break;
                
            case NO_REQUEST_DATA:
            	errorMsg = "Request object is empty. Please send some data";
                break;
                
            case INVALID_REQUEST_DATA:
            	errorMsg = "Invalid input object format. Please correct input data";
                break;
                
            case INVALID_LENGTH:
            	errorMsg = "Invalid length";
                break;
                
            case INVALID_NUMBER:
            	errorMsg = "Invalid number";
                break;
                
            case INVALID_DATE:
            	errorMsg = "Invalid date";
                break;
                
            case MANDATORY_FIELD_EMPTY:
            	errorMsg = "Mandatory field is empty";
                break;
                        
            case NULL_OBJECT:
            	errorMsg = "Input object is empty";
                break;
                
            case NO_DATA_FOUND:
            	errorMsg = "No data found";
                break;
                
            case INVALID_LIST:
            	errorMsg = "Invalid list values";
                break;
                
            default:
            	errorMsg = errorCode.toString();
                break;
        }
        
        return errorMsg;
    }
	
}
