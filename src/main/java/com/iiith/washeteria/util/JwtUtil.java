package com.iiith.washeteria.util;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
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

	@Autowired
	private JwtProperties jwtProperties = JwtProperties.getInstance();
//	private static final String ISSUER ="";
//	private static final String SECRET_KEY = "";
	private static final SignatureAlgorithm SIGNATURE_ALGORITHM = io.jsonwebtoken.SignatureAlgorithm.HS256;



	public String createJWT(String userName) {

		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		byte[] apiKeySecretBytes = jwtProperties.getJwtSecretKey().getBytes();
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, SIGNATURE_ALGORITHM.getJcaName());
		long expirationOffset = 1000L*60L*500000L;
		//set claims for JWT
		JwtBuilder builder = Jwts.builder().setId(UUID.randomUUID().toString())
				.setIssuedAt(now)
				.setSubject("Issued@ "+now.getTime()+" in favour of "+userName)
				.setAudience(userName)
				.setIssuer(jwtProperties.getJwtIssuer())
				.setExpiration(new Date(now.getTime()+expirationOffset))
				.claim("claims", new UserClaims(userName, now.getTime(),"self washeteria/"))
				.signWith(SIGNATURE_ALGORITHM, signingKey);

		return builder.compact();
	}

	public boolean validateToken(String jwt) {
		try {
			Claims claims = Jwts.parser().setSigningKey(jwtProperties.getJwtSecretKey().getBytes())
					.parseClaimsJws(jwt).getBody();
			claims.getAudience();

		}catch (SignatureException|ExpiredJwtException|MalformedJwtException e) {
			throw e;
		}catch(Exception e) {
			throw e;
		}
		return true;
	}
	
}
