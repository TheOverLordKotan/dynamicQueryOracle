package ws.refcursor;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import ws.refcursor.dto.ObjectNameRequest;
import ws.refcursor.util.Method;

@Component
@Scope("prototype")
public class ProcessThread implements Callable<Map> {

	private static final Logger log = Logger.getLogger("ProcessThread");
	
	@Autowired
	AppService service;
	
	ObjectNameRequest   xml;
	
	Method method;

	public void setXml(ObjectNameRequest xml) {
		this.xml = xml;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	@Override
	public Map call() throws Exception {
		try {
			log.info(method+" - Partition "+xml.hashCode()+" started");	
			Map responsemap = new LinkedHashMap();
			if(Method.GET_BY_NAME.equals(method))
				responsemap = service.getObjectsByName(xml);
			else if(Method.GET_BY_OWNER.equals(method))
				responsemap = service.getObjectsByOwner(xml);			
			
			log.info(method+" - Partition "+xml.hashCode()+" ended");
			return responsemap;
		} catch (Exception e) { 
			e.printStackTrace();
			log.error(method+" - Error: "+e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	
	
}
