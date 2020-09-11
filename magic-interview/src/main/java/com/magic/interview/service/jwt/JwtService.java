package com.magic.interview.service.jwt;

import com.google.common.collect.ImmutableMap;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2020-09-09 17:48
 **/
@Service
@Slf4j
public class JwtService {

	@Value("${jwt.secret}")
	private String secret;

	public String generatorJwtToken(String uid) {
		return Jwts.builder()
				//.setHeader(ImmutableMap.of("typ", "JWT"))
				.setSubject(uid)
				.setIssuedAt(new Date())
				.setExpiration(Date.from(LocalDateTime.now().plusSeconds(60).atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}

	public String generatorJwtToken2(String uid, Integer version) {
		return Jwts.builder()
				//.setHeader(ImmutableMap.of("typ", "JWT"))
				.setIssuedAt(new Date())
				.setSubject(uid)
				.addClaims(ImmutableMap.of("version", version))
				.setExpiration(Date.from(LocalDateTime.now().plusSeconds(30).atZone(ZoneId.of("Asia/Shanghai")).toInstant()))
				.signWith(SignatureAlgorithm.HS256, secret)
				.compact();
	}
}
