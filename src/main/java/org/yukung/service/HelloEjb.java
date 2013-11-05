package org.yukung.service;

import javax.ejb.Stateless;

@Stateless
public class HelloEjb {
	public String greet(String str) {
		return "Hello, " + str + "!!";
	}
}
