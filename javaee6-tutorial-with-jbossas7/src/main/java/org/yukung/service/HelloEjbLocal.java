package org.yukung.service;

import javax.ejb.Local;

@Local
public interface HelloEjbLocal {
	String greetLocal(String str);
}
