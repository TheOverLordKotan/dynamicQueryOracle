package ws.refcursor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ws.refcursor.dto.ErrorResponse;
import ws.refcursor.dto.ObjectNameRequest;
import ws.refcursor.dto.ObjectOwnerRequest;
import ws.refcursor.props.MyProperties;
import ws.refcursor.util.ErrorCodes;
import ws.refcursor.util.Method;


@RestController
@SuppressWarnings({"rawtypes","unchecked"})
@RequestMapping("/api")
public class WsController {
	
	private static final Logger log = Logger.getLogger("WsController");
	
	@Autowired
	ConfigurableApplicationContext ctx;
	
	@Autowired
	private MyProperties properties;
	
	@Autowired
	AppService service;
	
	
	@Autowired
	Validator validator;
	
	
	@RequestMapping("/isAlive")
    public String isAlive(@RequestBody String req) {
		return req;
    }
	
	@RequestMapping("/getObjectsByName")
    public Map getObjectsByName(@RequestBody List<ObjectNameRequest> request) {
		return getData(request);
	}
	
	@RequestMapping("/getObjectsByOwner")
    public Map getObjectsByOwner(@RequestBody List<ObjectOwnerRequest> request) {
		return getData(request);
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/getObject/{objectName}")
	public Map getObjectByName(@PathVariable String objectName) throws Exception {
		if(objectName.length()>30)
			throw new Exception(ErrorCodes.getErrorMsg(ErrorCodes.ERROR.INVALID_LENGTH));
		
		List<ObjectNameRequest> request = new ArrayList<ObjectNameRequest>();
		request.add(new ObjectNameRequest(objectName));
		return getData(request);
	}
	
	@ExceptionHandler({ Exception.class })
    public String handleException(Exception e) {
		return e.getMessage();
    }
	
	
	private List<ErrorResponse> checkRequest(Object req) {
		List<ErrorResponse> errors = new ArrayList<ErrorResponse>(0);
		if(req!=null){
			if(req instanceof ObjectNameRequest){
				ObjectNameRequest value = (ObjectNameRequest)req;
				Set<ConstraintViolation<ObjectNameRequest>> violations = validator.validate(value);
				for (ConstraintViolation<ObjectNameRequest> violation : violations) {
					String propertyPath = violation.getPropertyPath().toString();
		            String message = violation.getMessage();
		            errors.add(new ErrorResponse(ErrorCodes.ERROR.INVALID_REQUEST_DATA, propertyPath+" - "+message, value.toString()));
		        }
			} else if(req instanceof ObjectOwnerRequest) {
				ObjectOwnerRequest value = (ObjectOwnerRequest)req;
				Set<ConstraintViolation<ObjectOwnerRequest>> violations = validator.validate(value);
				for (ConstraintViolation<ObjectOwnerRequest> violation : violations) {
					String propertyPath = violation.getPropertyPath().toString();
		            String message = violation.getMessage();
		            errors.add(new ErrorResponse(ErrorCodes.ERROR.INVALID_REQUEST_DATA, propertyPath+" - "+message, value.toString()));
		        }
			}
		}
		
		return errors;
	}
	
	private Map getData(List<?> request) {
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
		Method method = Method.ND;
		List response = new ArrayList(0);
		
		if(request!=null && request.size()>0){
			if(request.get(0) instanceof ObjectNameRequest)
				method = Method.GET_BY_NAME;
			else if(request.get(0) instanceof ObjectOwnerRequest)
				method = Method.GET_BY_OWNER;
		} else {
			log.warn(method+" - Request object is empty. Please send some data");
			response.add(new ErrorResponse(ErrorCodes.ERROR.NO_REQUEST_DATA, ""));
			stopWatch.stop();
			log.info(method+" execution time: "+stopWatch.getTotalTimeMillis()+ "ms");
			Map responsemap = new LinkedHashMap();
			responsemap.put("Values", response);
			return responsemap;
		}
		
		int requestLimit = properties.getRequestLimit();
		int responseLimit = properties.getResponseLimit();
		
		if(request.size()>requestLimit){
			log.warn(method+" - Request size "+request.size()+" limited to "+requestLimit);
			response.add(new ErrorResponse(ErrorCodes.ERROR.REQUEST_OVERLIMIT, request.size()+"/"+requestLimit));
			stopWatch.stop();
			log.info(method+" execution time: "+stopWatch.getTotalTimeMillis()+ "ms");
			Map responsemap = new LinkedHashMap();
			responsemap.put("Values", response);
			return responsemap;
		}
		
		
		List<ErrorResponse> errors = new ArrayList<ErrorResponse>(0);
		for(Object req: request) {
			errors.addAll(checkRequest(req));
			if(errors.size()>0){
				log.warn(method+" - Invalid input object format "+req.toString());
				response.clear();
				//response.add(new ErrorResponse(ErrorCodes.ERROR.INVALID_REQUEST_DATA, req.toString()));
				response.addAll(errors);
				stopWatch.stop();
				log.info(method+" execution time: "+stopWatch.getTotalTimeMillis()+ "ms");
				Map responsemap = new LinkedHashMap();
				responsemap.put("Values", response);
				return responsemap;
			}
		}
			
		try {
			
			@SuppressWarnings("unused")
			boolean parallel = Boolean.parseBoolean(properties.getParallel());
			ObjectNameRequest value = null;
			if(Method.GET_BY_NAME.equals(method)) {
				for (Object errorResponse : request) {
					
					ObjectNameRequest errorResponsePeti = (ObjectNameRequest) errorResponse; 
					 value =	errorResponsePeti;
				}
				
				return service.getObjectsByName(value);
			} else if(Method.GET_BY_OWNER.equals(method)) {
				for (Object errorResponse : request) {
					
					 value =(ObjectNameRequest)	errorResponse;
				}
				return service.getObjectsByOwner(value);
				

			} 
			
		} catch(Exception e){
			log.error(method+" - "+ExceptionUtils.getStackTrace(e) );
			response.add(new ErrorResponse(ErrorCodes.ERROR.CODE_ERROR, e.getMessage(), ""));
			stopWatch.stop();
			log.info(method+" execution time: "+stopWatch.getTotalTimeMillis()+ "ms");
			Map responsemap = new LinkedHashMap();
			responsemap.put("Values", response);
			return responsemap;
		}
			
			
		if(response.size()>responseLimit){
			log.warn(method+" - Response size "+response.size()+" limited to "+responseLimit);
			ErrorResponse er = new ErrorResponse(ErrorCodes.ERROR.RESPONSE_OVERLIMIT, response.size()+"/"+responseLimit);
			response.clear();
			response.add(er);
			stopWatch.stop();
			log.info(method+" execution time: "+stopWatch.getTotalTimeMillis()+ "ms");
			Map responsemap = new LinkedHashMap();
			responsemap.put("Values", response);
			return responsemap;
		}
		
		
		stopWatch.stop();
		log.info(method+" execution time: "+stopWatch.getTotalTimeMillis()+ "ms");
		Map responsemap = new LinkedHashMap();
		responsemap.put("Values", response);
		return responsemap;
			
	}
	
	
	
	@RequestMapping(value = "/user/", method = RequestMethod.GET)
	public ResponseEntity<Map> listAllUsers() {
		List<ObjectNameRequest> request = new ArrayList<>();
		ObjectNameRequest hola = new ObjectNameRequest();
		hola.setObjectName("select count(*) from dual");
		request.add(hola);
		Map resultado = getData(request);
		if (resultado.isEmpty()) {
			return new ResponseEntity(HttpStatus.NO_CONTENT);
			// You many decide to return HttpStatus.NOT_FOUND
		}
		return new ResponseEntity<Map>(resultado, HttpStatus.OK);
	}
	
}
