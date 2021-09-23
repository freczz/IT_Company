package com.it_company.employees;

public class Person {
	
	  private String name, position, project;

	    public Person(String name, String position, String project) {
	        this.name = name;
	        this.position = position;
	        this.project = project;
	    }
	    
	    public String getName() {
	        return name;
	    }

	    public String getPosition() {
	        return position;
	    }
	    
	    public String getProject() {
	    	return project;
	    }
}
