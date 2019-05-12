package ws.refcursor.props;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "my")
public class MyProperties {

	Integer requestLimit;
	Integer responseLimit;
	String  debug;
	String  parallel;
	Integer partitionSize;
	Integer poolSize;

	public Integer getRequestLimit() {
		return requestLimit;
	}

	public void setRequestLimit(Integer requestLimit) {
		this.requestLimit = requestLimit;
	}

	public Integer getResponseLimit() {
		return responseLimit;
	}

	public void setResponseLimit(Integer responseLimit) {
		this.responseLimit = responseLimit;
	}

	public String getDebug() {
		return debug;
	}

	public void setDebug(String debug) {
		this.debug = debug;
	}

	public String getParallel() {
		return parallel;
	}

	public void setParallel(String parallel) {
		this.parallel = parallel;
	}

	public Integer getPartitionSize() {
		return partitionSize;
	}

	public void setPartitionSize(Integer partitionSize) {
		this.partitionSize = partitionSize;
	}

	public Integer getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(Integer poolSize) {
		this.poolSize = poolSize;
	}
	
}
