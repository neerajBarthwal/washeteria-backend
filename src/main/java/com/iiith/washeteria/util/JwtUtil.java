package com.iiith.washeteria.util;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.iiith.washeteria.security.UserClaims;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Service
public class JwtUtil {

	private static final String ISSUER = "IIIT-HYD-WASHTERERIA-APP";
	private static final String SECRET_KEY = "My-Hard-Secret-Key";
	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = io.jsonwebtoken.SignatureAlgorithm.HS256;



	public String createJWT(String userName) {

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		byte[] apiKeySecretBytes = SECRET_KEY.getBytes();
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());
		long expirationOffset = 1000L*60L*500000L;
		//set claims for JWT
		JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
				.setIssuedAt(now)
				.setSubject("Issued@ "+now.getTime()+" in favour of "+userName)
				.setAudience(userName)
				.setIssuer(JwtUtil.ISSUER)
				.setExpiration(new Date(now.getTime()+expirationOffset))
				.claim("claims", new UserClaims(userName, now.getTime(),"self washeteria/"))
				.signWith(SIGNATURE_ALGORITHM, signingKey);

		return builder.compact();
	}

	public boolean validateToken(String jwt) {
		try {
			Claims claims = Jwts.parser().setSigningKey(SECRET_KEY.getBytes())
					.parseClaimsJws(jwt).getBody();
			claims.getAudience();

		}catch (SignatureException|ExpiredJwtException|MalformedJwtException e) {
			throw e;
		}catch(Exception e) {
			throw e;
		}
		return true;
	}
	
//	public static void main(String[] args) {
//		JwtUtil jwtUtil = new JwtUtil();
//		String token = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI1MzMzNTRmNi04MDAxLTRiODQtYWNmYS1mYmVhNWM3NzhjM2QiLCJpYXQiOjE1ODYwMDkyMjUsInN1YiI6Iklzc3VlZEAgMTU4NjAwOTIyNTgxMSBpbiBmYXZvdXIgb2YgbmVlcmFqLmJhcnRod2FsQHN0dWRlbnRzLmlpaXQuYWMuaW4iLCJhdWQiOiJuZWVyYWouYmFydGh3YWxAc3R1ZGVudHMuaWlpdC5hYy5pbiIsImlzcyI6IklJSVQtSFlELVdBU0hURVJFUklBLUFQUCIsImV4cCI6MTU4NjAwOTUyNSwiY2xhaW1zIjp7InVzZXJOYW1lIjoibmVlcmFqLmJhcnRod2FsQHN0dWRlbnRzLmlpaXQuYWMuaW4iLCJ0b2tlbklzc3VlVGltZSI6MTU4NjAwOTIyNTgxMSwic2NvcGUiOiJzZWxmIHdhc2hldGVyaWEvIn19.2iBqPMVChvbUE2WR5g6xc2s1P_g-1FWaaypSuNGXvpw";//jwtUtil.createJWT("neeraj.barthwal@students.iiit.ac.in");
//		System.out.println(token);
//		
//		if(jwtUtil.validateToken(token)) {
//			System.out.println("VALID!!");
//		}else {
//			System.out.println("INVALID");
//		}
//	}


}
