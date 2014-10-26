package com.wondersgroup.testplat.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("/")
public class SecurityEventService {

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_XML)
	public User getUserInXML() {
		return getUser("unknown");
	}

	@GET
	@Path("/user")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUserInJSON() {
		return getUser("unknown");
	}
	
	@GET
	@Path("/users")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public List<User> getUserList() {
		
		List<User> users= new ArrayList<User>();
		
		users.add(getUser("zzzz"));
		users.add(getUser("xxxx"));
		
		return users;
	}
	
	
	private User getUser(String name){
		
		System.out.print("Create User");
		User user = new User();
		user.setName(name);
		
		return user;
	}
}
