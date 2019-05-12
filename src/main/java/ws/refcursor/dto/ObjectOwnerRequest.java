package ws.refcursor.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectOwnerRequest {

	@Size(min = 1, max = 30)
	@NotNull
	String objectOwner;

	public ObjectOwnerRequest() {
		super();
	}

	public ObjectOwnerRequest(String objectOwner) {
		super();
		this.objectOwner = objectOwner;
	}

	public String getObjectOwner() {
		return objectOwner;
	}

	public void setObjectOwner(String objectOwner) {
		this.objectOwner = objectOwner;
	}

	@Override
	public String toString() {
		return "ObjectOwnerRequest [objectOwner=" + objectOwner + "]";
	}

}
