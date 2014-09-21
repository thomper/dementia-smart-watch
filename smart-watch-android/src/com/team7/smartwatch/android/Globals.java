package com.team7.smartwatch.android;

import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Globals {
	
	private static Globals globals = null;
	
	public HttpContext httpContext;
	public final String SERVER_ADDRESS = "http://192.168.1.41:8080";
	
	private Globals() {
		
		// Create httpContext so that we have a cookie store.
		httpContext = new BasicHttpContext();
		httpContext.setAttribute(ClientContext.COOKIE_STORE,
				new BasicCookieStore());
	}
	
	public static Globals get() {

		if (globals == null) {
			globals = new Globals();
		}
		return globals;
	}

}
