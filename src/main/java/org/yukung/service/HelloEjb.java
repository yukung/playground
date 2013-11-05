package org.yukung.service;

import javax.ejb.Stateless;

@Stateless
public class HelloEjb implements HelloEjbLocal, HelloEjbRemote {
	public String greetLocal(String str) {
		return "Hello, " + str + "!!";
	}
	public String greetRemote(String str) {
		return "Hello, " + str + "!!";
	}
}
