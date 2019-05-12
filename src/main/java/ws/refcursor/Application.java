package ws.refcursor;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.castor.CastorMarshaller;
import org.springframework.stereotype.Service;
import org.springframework.validation.Validator;

import ws.refcursor.props.MyProperties;
import ws.refcursor.props.MyPropertiesValidator;
import ws.refcursor.util.UtilGenerateDtoRequest;

@SpringBootApplication(scanBasePackages={"ws.refcursor"})
public class Application {
	
	private static final Logger log = Logger.getLogger("Application");
	
	@Bean
	public Validator configurationPropertiesValidator() {
		return new MyPropertiesValidator();
	}
	
	@Bean
    public UtilGenerateDtoRequest UtilGenerateDtoRequest() {
		
        return new UtilGenerateDtoRequest();
    }
	
	@Bean
    public CastorMarshaller castorMarshaller() {
		CastorMarshaller castorMarshaller = new CastorMarshaller();
		ClassPathResource cpr = new ClassPathResource("/ws/refcursor/util/mapping-castor.xml");
		castorMarshaller.setMappingLocation(cpr);
		castorMarshaller.setSuppressXsiType(true);
        return castorMarshaller;
    }
	
	@Bean
	public javax.validation.Validator validator() {
		ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
	    javax.validation.Validator validator = validatorFactory.getValidator();
		return validator;
	}
	

	@Service
	static class Startup implements CommandLineRunner {

		@Autowired
		private MyProperties properties;

		@Override
		public void run(String... args) {
			log.info("=========================================");
			log.info("Server configuration ");
			log.info("Request Limit: " + this.properties.getRequestLimit());
			log.info("Response Limit: " + this.properties.getResponseLimit());
			log.info("Debug: " + this.properties.getDebug());
			log.info("Parallel: " + this.properties.getParallel());
			log.info("Partition Size: " + this.properties.getPartitionSize());
			log.info("Pool Size: " + this.properties.getPoolSize());
			log.info("=========================================");
		}
	}
	

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
