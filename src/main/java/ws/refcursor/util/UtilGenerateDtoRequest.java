package ws.refcursor.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UtilGenerateDtoRequest {
	
    @Autowired
    private ObjectMapper mapper;

    /**
     * Converts message to JSON. Used mostly by {@link org.springframework.jms.core.JmsTemplate}
     */
    public String toMessage(Object object) throws Exception {
        String json= null;

        try {
            json = mapper.writeValueAsString(object);
        } catch (Exception e) {
            throw new Exception("Message cannot be parsed. ", e);
        }


        return json;
    }
    
    /**
     * Converts message to JSON. Used mostly by {@link org.springframework.jms.core.JmsTemplate}
     */
    public Object toObject(String object) throws Exception {
        Object json= null;

        try {
        	 json = mapper.readValue(object, Object.class);
        } catch (Exception e) {
            throw new Exception("Message cannot be parsed. ", e);
        }


        return json;
    }


}