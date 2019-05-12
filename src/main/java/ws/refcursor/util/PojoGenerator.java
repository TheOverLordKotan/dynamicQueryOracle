package ws.refcursor.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;

public class PojoGenerator {

	static Map<String, Class<?>> classStorage = new HashMap<String, Class<?>>();
	static ClassPool pool = ClassPool.getDefault();
	
	public static Class generate(String className, Map<String, Class<?>>  properties,List<String> header) throws NotFoundException,
			CannotCompileException, ClassNotFoundException {

		if(classStorage.containsKey(className)){
			return classStorage.get(className);
		} else {
			CtClass cc = pool.makeClass(className);
			// add this to define a super class to extend
			// cc.setSuperclass(resolveCtClass(MySuperClass.class));

			// add this to define an interface to implement
			cc.addInterface(resolveCtClass(Serializable.class));

			for (String property: header) {
			for (Entry<String, Class<?>> entry : properties.entrySet()) {
				if (property.toUpperCase().equals(entry.getKey().toUpperCase())) {
				cc.addField(new CtField(resolveCtClass(entry.getValue()), entry.getKey(), cc));

				// add getter
				cc.addMethod(generateGetter(cc, entry.getKey(), entry.getValue()));

				// add setter
				cc.addMethod(generateSetter(cc, entry.getKey(), entry.getValue()));
				break;
				}
			}
			}
			
			classStorage.put(className, cc.toClass());
			cc.detach();
			
			return classStorage.get(className);
		}	
		
		
	}

	private static CtMethod generateGetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String getterName = "get" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public ").append(fieldClass.getName()).append(" ")
				.append(getterName).append("(){").append("return this.")
				.append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtMethod generateSetter(CtClass declaringClass, String fieldName, Class fieldClass)
			throws CannotCompileException {

		String setterName = "set" + fieldName.substring(0, 1).toUpperCase()
				+ fieldName.substring(1);

		StringBuffer sb = new StringBuffer();
		sb.append("public void ").append(setterName).append("(")
				.append(fieldClass.getName()).append(" ").append(fieldName)
				.append(")").append("{").append("this.").append(fieldName)
				.append("=").append(fieldName).append(";").append("}");
		return CtMethod.make(sb.toString(), declaringClass);
	}

	private static CtClass resolveCtClass(Class clazz) throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		return pool.get(clazz.getName());
	}
}