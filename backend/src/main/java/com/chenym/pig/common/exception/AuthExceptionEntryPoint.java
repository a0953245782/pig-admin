package com.chenym.pig.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws ServletException, IOException {
		if (!("/api/auth/logout".equals(request.getServletPath()))) {
			Map map = new HashMap();
			map.put("status", 401);
			map.put("message", authException.getMessage());
			// map.put("path", request.getServletPath());
			// map.put("timestamp", String.valueOf(new Date().getTime()));
			response.setContentType("application/json");
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			try {
				ObjectMapper mapper = new ObjectMapper();
				mapper.writeValue(response.getOutputStream(), map);
			} catch (Exception e) {
				throw new ServletException();
			}
		} else {
			request.getRequestDispatcher("/api/auth/logout").forward(request, response);
		}
	}

}