package com.magic.interview.service.jwt;

import com.google.gson.Gson;
import com.magic.base.dto.Result;
import com.magic.base.dto.enums.RespStatusEnum;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

/**
 * @author Cheng Yufei
 * @create 2020-09-09 18:19
 **/
@Configuration
@Slf4j
public class FilterConfig {

	@Autowired
	private Gson gson;
	@Value("${jwt.secret}")
	private String secret;
	@Autowired
	private JwtService jwtService;

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	@Bean(name = "jwtTokenFilter")
	public FilterRegistrationBean jwtToken() {
		FilterRegistrationBean<Filter> registrationBean = new FilterRegistrationBean<>();
		OncePerRequestFilter filter = new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
				String authorization = request.getHeader("Authorization");
				if (StringUtils.isBlank(authorization)) {
					fail(response, RespStatusEnum.NO_TOKEN);
					return;
				}
				String token = authorization.split(" ")[1];
				if (isValidToken(token, response)) {
					filterChain.doFilter(request, response);
					return;
				}
				fail(response, RespStatusEnum.TOKEN_FAILURE);
				return;
			}
		};
		String[] urls = new String[]{
				"/jwt/getInfo"
		};
		registrationBean.addUrlPatterns(urls);
		registrationBean.setFilter(filter);
		return registrationBean;
	}

	public void fail(HttpServletResponse response, RespStatusEnum statusEnum) {
		response.setContentType("application/json;charset=utf-8");
		//response.setHeader("Authorization","Bearer abc");
		try (PrintWriter writer = response.getWriter()) {
			writer.write(gson.toJson(Result.fail(statusEnum)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isValidToken(String token, HttpServletResponse response) {
		Claims body = null;
		try {
			body = Jwts.parser()
					.setSigningKey(secret)
					.parseClaimsJws(token)
					.getBody();
			LocalDateTime expiration = LocalDateTime.from(body.getExpiration().toInstant().atZone(ZoneId.of("Asia/Shanghai")));
			LocalDateTime now = LocalDateTime.now();
			long between = ChronoUnit.SECONDS.between(now, expiration);
			log.info("{},{},{}", formatter.format(expiration), formatter.format(now), between);
			if (between <= 10) {
				log.info("距离token过期还有{}s, 刷新token", between);
				String subject = body.getSubject();
				Integer version = (Integer) body.get("version");
				String newToken = jwtService.generatorJwtToken2(subject, version + 1);
				response.setContentType("application/json;charset=utf-8");
				response.setHeader("Authorization", "Bearer " + newToken);
			}
			return true;
		} catch (JwtException e) {
			log.error(">非法token<");
		}
		return false;
	}
}
