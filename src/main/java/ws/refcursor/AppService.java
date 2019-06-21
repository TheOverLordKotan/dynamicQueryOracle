package ws.refcursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;

import oracle.jdbc.OracleTypes;
import ws.refcursor.dto.ObjectNameRequest;
import ws.refcursor.props.MyProperties;
import ws.refcursor.util.PojoGenerator;


@Service
public class AppService {
	
	private static final Logger log = Logger.getLogger("AppService");
	
	private static String GET_OBJECTS_BY_NAME = "WS_REF_CURSOR_SQL.GET_OBJECTS_BY_NAME";
	private static String GET_OBJECTS_BY_OWNER = "WS_REF_CURSOR_SQL.GET_OBJECTS_BY_OWNER";
	
	private Object lock = new Object();
	
	@PersistenceContext
    EntityManager entityManager;
	
	@Autowired
    JdbcTemplate jdbcTemplate;
	
	@Autowired
	private MyProperties properties;
	
	public Map callProcForData(ObjectNameRequest  xml, String procName, String pojoClassName) throws Exception {
		SimpleJdbcCall procCall;
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		
		boolean debug = Boolean.parseBoolean(properties.getDebug());
		
		procCall = new SimpleJdbcCall(jdbcTemplate)
    	.withFunctionName(procName)
    	.withoutProcedureColumnMetaDataAccess();
		
		procCall.declareParameters(new SqlOutParameter("ref_cursor", OracleTypes.CURSOR));
		
		procCall.declareParameters(new SqlParameter("I_xml", java.sql.Types.VARCHAR));
		mapSqlParameterSource.addValue("I_xml", xml.getObjectName());
		
		if(debug){
			log.info("Call: "+procCall.getProcedureName()+"("+StringUtils.join(mapSqlParameterSource.getValues().keySet(), ",")+")\n"+
					 "I_xml: "+xml.getObjectName()
					 );
		}
		
		ArrayList<Map> resultArray = procCall.executeFunction(ArrayList.class,mapSqlParameterSource);
		
		return generateOutput(resultArray, pojoClassName);
	}
	

	
	public Map getObjectsByName(ObjectNameRequest  xml) throws Exception {
		return callProcForData(xml, GET_OBJECTS_BY_NAME, xml.getObjectName());
	}
	
	public Map getObjectsByOwner(ObjectNameRequest  xml) throws Exception {
		return callProcForData(xml, GET_OBJECTS_BY_OWNER, xml.getObjectName());
	}
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Map generateOutput(ArrayList<Map> resultArray, String pojoClassName) throws Exception {
		Map resultQuery = new LinkedHashMap();
		int resultSizeLimit = properties.getResponseLimit();
		int max = 0;
		if(resultArray==null)
			return new LinkedHashMap();
		
		/*
		if(resultArray.size()>=resultSizeLimit) {
			max = resultSizeLimit;
			log.warn("Function result limited from "+resultArray.size()+" to "+resultSizeLimit);
		} else{
			max = resultArray.size();
		}*/
		
		max = resultArray.size();
		List<String> headerLst = new ArrayList<String>(0);
		List<String> header = new ArrayList<String>(0);
		List<List<String>> data = new ArrayList<List<String>>(0);
		
		for (int i = 0; i < max; i++) {
			List<String> row = new ArrayList<String>(0);
			
			if(i==0){
				for(Object key: resultArray.get(i).keySet()){
					header.add(key.toString());
				}
			}
			
			for(String key: header){
				Object o = resultArray.get(i).get(key);
				if(o!=null)
					row.add(resultArray.get(i).get(key).toString());
				else
					row.add("");
			}
			data.add(row);
		}
		
		List responseList = new LinkedList();
		if(!header.isEmpty() && !data.isEmpty()){
			Map<String, Class<?>> props = new HashMap<String, Class<?>>();
			headerLst = reverseList(header);
			for (String property: header) {
				props.put(toProperty(property), String.class);
			}
			
			Class<?> clazz;
			
			synchronized(lock) {
				clazz = PojoGenerator.generate(pojoClassName, props,header);
			}
			
			for(List<String> row: data){
				Object obj = new Object();
				obj=	clazz.newInstance();
				
				for (int index = 0; index < header.size(); index++) {
					String property = header.get(index);
					String value = row.get(index);
					clazz.getMethod(toSetter(property), String.class).invoke(obj, value);
				}
				responseList.add(obj);
			}
			
		}
		resultQuery.put("Values", responseList);
		Map itemOrder = new LinkedHashMap();
		int valueter=1;
		for (String property: header) {
			itemOrder.put(valueter, property);
			valueter++;
		}
		resultQuery.put("Order", itemOrder);
		return resultQuery;
	}
	
	
	private String toProperty(String property){
		String ret = property;
		if(ret!=null && ret.length()>0){
			//ret = ret.replaceAll("[^a-zA-Z0-9]", "");
			if(ret!=null && ret.length()>0){
				ret = StringUtils.stripStart(ret, "1234567890");
				if(ret!=null && ret.length()>0){
					if (!Character.isUpperCase(ret.charAt(0))) {
						ret = StringUtils.uncapitalize(ret);
					}
				}
			}
		} 
		
		return ret;
	}
	
	private String toSetter(String property){
		String ret = toProperty(property);
		if(ret!=null && ret.length()>0){
			ret = "set"+StringUtils.capitalize(ret);
		}
		return ret;
	}
	
	
	private List reverseList(List myList) {
	    List invertedList = new ArrayList();
	    for (int i = myList.size() - 1; i >= 0; i--) {
	        invertedList.add(myList.get(i));
	    }
	    return invertedList;
	}
	
}

