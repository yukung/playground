package org.yukung.service;

import javax.ejb.Remote;

@Remote
public interface HelloEjbRemote {
	String greetRemote(String str);
}
