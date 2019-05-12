package ws.refcursor.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectNameRequest {

	@Size(min = 1, max = 4000)
	@NotNull
	String objectName;

	public ObjectNameRequest() {
		super();
	}

	public ObjectNameRequest(String objectName) {
		super();
		this.objectName = objectName;
	}

	public String getObjectName() {
		return objectName;
	}

	public void setObjectName(String objectName) {
		this.objectName = objectName;
	}

	@Override
	public String toString() {
		return "ObjectNameRequest [objectName=" + objectName + "]";
	}
	
}
