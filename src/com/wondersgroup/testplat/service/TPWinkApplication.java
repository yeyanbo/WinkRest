package com.wondersgroup.testplat.service;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospectorPair;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class TPWinkApplication extends Application {

	
	  /**
	   * Get the list of service classes provided by this JAX-RS application
	   */
	  @Override
	  public Set<Class<?>> getClasses() {
	    Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
	    serviceClasses.add(SecurityEventService.class);
	    return serviceClasses;
	  }
	  
	  @Override
	  public Set<Object> getSingletons() {
	    Set<Object> s = new HashSet<Object>();
	    
	    ObjectMapper mapper = new ObjectMapper();
	    AnnotationIntrospector primary = new JaxbAnnotationIntrospector(mapper.getTypeFactory());
	    AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
	    AnnotationIntrospectorPair pair = new AnnotationIntrospectorPair(primary, secondary);
	    mapper.setAnnotationIntrospector(pair);
	    
	    // Set up the provider
	    JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
	    jaxbProvider.setMapper(mapper);
	    
	    s.add(jaxbProvider);
	    return s;
	  }
}
