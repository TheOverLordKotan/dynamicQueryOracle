package ws.refcursor;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import ws.refcursor.dto.ObjectNameRequest;
import ws.refcursor.dto.ObjectOwnerRequest;
import ws.refcursor.util.ErrorCodes;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class ControllerTest {
	
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    
    private HttpMessageConverter mappingJackson2HttpMessageConverter;
    
    private List dualList;
    
    private List ownerList;
    
    @Autowired
    private WebApplicationContext webApplicationContext;
    
    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
    
    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.dualList = new ArrayList();
        this.dualList.add(new ObjectNameRequest("DUAL"));
        
        this.ownerList = new ArrayList();
        this.ownerList.add(new ObjectOwnerRequest("SYS"));
    }
    
    @Test
    public void testIsAlive() throws Exception {
        mockMvc.perform(post("/isAlive")
                .content(this.json("OK"))
                .contentType(contentType))
                .andExpect(status().isOk());
    }
    
    @Test
    public void testDualTable() throws Exception {
        mockMvc.perform(post("/getObjectsByName")
                .content(this.json(dualList))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
    }
    
    @Test
    public void testSysOwner() throws Exception {
        mockMvc.perform(post("/getObjectsByOwner")
                .content(this.json(ownerList))
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType));
    }
    
    @Test
    public void testOneObjectGet() throws Exception {
    	mockMvc.perform(get("/getObject/DUAL"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)));
    }
    
    @Test
    public void testTooLongObjectNameGet() throws Exception {
    	mockMvc.perform(get("/getObject/123456789012345678901234567890123"))
                .andExpect(status().isOk())
                .andExpect(content().string(equalToIgnoringCase(ErrorCodes.getErrorMsg(ErrorCodes.ERROR.INVALID_LENGTH))));
    }
    
    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
