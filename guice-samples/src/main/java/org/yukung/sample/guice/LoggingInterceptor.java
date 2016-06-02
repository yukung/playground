package org.yukung.sample.guice;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class LoggingInterceptor implements MethodInterceptor {

	public Object invoke(MethodInvocation invocation) throws Throwable {
		String methodName = invocation.getMethod().getName();
		System.out.println("before: " + methodName);
		Object obj = invocation.proceed();
		System.out.println("after: " + methodName);
		return obj;
	}

}
