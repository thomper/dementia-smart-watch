package com.team7.smartwatch.server;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;

/* Simplifies getting JSON from a HttpServletRequest. */
public class JSONConverter {

	public static JSONObject getJSON(HttpServletRequest request)
			throws IOException {

		String text = getRawText(request);
        return new JSONObject(text);
	}

	private static String getRawText(HttpServletRequest request)
			throws IOException {
		StringBuffer sb = new StringBuffer();
		BufferedReader reader = request.getReader();
		String line;
		
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		
		return sb.toString();
    }
}
