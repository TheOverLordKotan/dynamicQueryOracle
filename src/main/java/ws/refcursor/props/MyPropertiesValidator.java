package ws.refcursor.props;

import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

public class MyPropertiesValidator implements Validator {

	final Pattern booleanPattern = Pattern.compile("^TRUE$|^FALSE$|^true$|^false$");
	
	@Override
	public boolean supports(Class<?> type) {
		return type == MyProperties.class;
	}

	@Override
	public void validate(Object o, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "requestLimit", "requestLimit.empty");
		ValidationUtils.rejectIfEmpty(errors, "responseLimit", "responseLimit.empty");
		ValidationUtils.rejectIfEmpty(errors, "debug", "debug.empty");
		ValidationUtils.rejectIfEmpty(errors, "parallel", "parallel.empty");
		ValidationUtils.rejectIfEmpty(errors, "partitionSize", "partitionSize.empty");
		ValidationUtils.rejectIfEmpty(errors, "poolSize", "poolSize.empty");
		
		MyProperties properties = (MyProperties) o;	
		if (properties.getRequestLimit() != null && (properties.getRequestLimit()<10 || properties.getRequestLimit()>1000)) {
			errors.rejectValue("requestLimit", "requestLimit out of scope 10-1000");
		}
		
		if (properties.getResponseLimit() != null && (properties.getResponseLimit()<100 || properties.getRequestLimit()>10000)) {
			errors.rejectValue("responseLimit", "responseLimit out of scope 100-10000");
		}
		
		if (properties.getPartitionSize() != null && (properties.getPartitionSize()<1 || properties.getPartitionSize()>1000)) {
			errors.rejectValue("partitionSize", "partitionSize out of scope 1-1000");
		}
		
		if (properties.getPoolSize() != null && (properties.getPoolSize()<1 || properties.getPoolSize()>10)) {
			errors.rejectValue("poolSize", "poolSize out of scope 1-10");
		}
		
		if (properties.getDebug() != null
				&& !this.booleanPattern.matcher(properties.getDebug()).matches()) {
			errors.rejectValue("debug", "Invalid debug (TRUE/FALSE)");
		}
		
		if (properties.getParallel() != null
				&& !this.booleanPattern.matcher(properties.getParallel()).matches()) {
			errors.rejectValue("parallel", "Invalid parallel (TRUE/FALSE)");
		}
	}
	
	
	
}
