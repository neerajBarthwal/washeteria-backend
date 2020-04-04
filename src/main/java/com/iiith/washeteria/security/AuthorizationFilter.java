package com.iiith.washeteria.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iiith.washeteria.businessentities.AuthToken;
import com.iiith.washeteria.businessentities.ErrorMessage;
import com.iiith.washeteria.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;

@Component
public class AuthorizationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtUtil jwtUtil;
	
	private final HttpClient httpClient = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .build();
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String authorizationHeader = request.getHeader("Authorization");
		String userName = request.getHeader("userName");
		String password = request.getHeader("password");
		
		
		if(authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")) {
			try {
				validateJWT(authorizationHeader);
				filterChain.doFilter(request, response);
			}catch(SignatureException signException) {
				invalidateRequest(response, new ErrorMessage("TAMPERED_TOKEN", signException.getMessage()));
			}catch(ExpiredJwtException expiredException) {
				invalidateRequest(response, new ErrorMessage("EXPIRED_TOKEN", expiredException.getMessage()));
			}
			
		}else {
			if(userName==null || password==null) {
				sendResponse(response,"",HttpServletResponse.SC_UNAUTHORIZED);
			}
			//Authorize the user with IIIT-H reverse proxy server and create a JWT.
			else if(isAuthentic(userName,password)) {
				String jwt = jwtUtil.createJWT(userName);
				AuthToken token = new AuthToken(jwt);
				sendResponse(response,serializeToJson(token),HttpServletResponse.SC_OK);
			}else {
				String message = serializeToJson(new ErrorMessage("INVALID USER", "Invalid username or password"));
				sendResponse(response,message,HttpServletResponse.SC_UNAUTHORIZED);
			}
		}
	}

	private boolean isAuthentic(String userName, String password) {
		String authValue = userName+":"+password;
		String basicAuth = Base64.getEncoder().encodeToString(authValue.getBytes());
		HttpRequest request = HttpRequest.newBuilder()
							.GET()
							.uri(URI.create("https://reverseproxy.iiit.ac.in"))
							.setHeader("Authorization", "Basic "+basicAuth)
							.build();
		
		HttpResponse<String> response = null;
		try {
			response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			return false;
		}
		if(response.statusCode()==200)
			return true;
		return false;
	}
	
	private void sendResponse(HttpServletResponse response, String message, int statusCode){
		
		try {
			PrintWriter printWriter = response.getWriter();
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(statusCode);
			printWriter.print(message);
			printWriter.flush();
		} catch (IOException e) {
			//log this exception
			e.printStackTrace();
		}
		
	}

	private void invalidateRequest(HttpServletResponse response, 
								   ErrorMessage errorMessage) {
		
		String jsonMessage = serializeToJson(errorMessage);
		sendResponse(response, jsonMessage, HttpServletResponse.SC_UNAUTHORIZED);
		
	}
	
	private String serializeToJson(Object obj){
		String json="";
		try {
			ObjectMapper mapper = new ObjectMapper();
			json = mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			//log this exception
			e.printStackTrace();
		}
		return json;
	}

	private void validateJWT(String authorizationHeader) {
		String jwt = authorizationHeader.substring(7);
		jwtUtil.validateToken(jwt);
	}

//	public static void main(String[] args) {
//		AuthorizationFilter f = new AuthorizationFilter();
//		if(f.isAuthentic("neeraj.barthwal@students.iiit.ac.in", "")) {
//			System.out.println("AUTHENNTCAted!!");
//		}else {
//			System.out.println("NO");
//		}
//	}
}
