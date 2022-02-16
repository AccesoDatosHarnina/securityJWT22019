package com.example.demo.domain;


import java.util.ArrayList;
import java.util.Collection;



public class AppUser {
    private Long id;
    private String username;
    private String password;

    private Collection<String> roles=new ArrayList<>();
    public Long getId() {
		return id;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public AppUser(Long id, String username, String password) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
	}
	public void addRole(String role){
        roles.add(role);
    }
    public String[] getRoles(){
        return roles.toArray(new String[0]);
    }
}
