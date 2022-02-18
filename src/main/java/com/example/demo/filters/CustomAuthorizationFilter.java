package com.example.demo.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		if (request.getServletPath().equals("/login")) {
			// Esto se puede traducir como 'de esto yo no me encargo'
			filterChain.doFilter(request, response);
		} else {
			String authorizartionHeader = request.getHeader(AUTHORIZATION);
			if (authorizartionHeader != null && authorizartionHeader.startsWith("Bearer ")) {
				try {
					// me voy a quedar solo con el token de acceso
					String token = authorizartionHeader.substring("Bearer ".length());
					// configuro el algorimo de encriptacion
					Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
					// constuyo el verificador de token
					JWTVerifier verifier = JWT.require(algorithm).build();
					// voy a decodificar el token usando el verificador antes creado
					DecodedJWT decodecJWT = verifier.verify(token);
					// podemos extraer el username
					String username = decodecJWT.getSubject();
					// asi condigo los roles del usuarios
					String[] roles = decodecJWT.getClaim("roles").asArray(String.class);
					// convertir los roles en sus derechos de acceso
					Collection<SimpleGrantedAuthority> authorities = new ArrayList();
					Arrays.stream(roles).forEach(role -> authorities.add(new SimpleGrantedAuthority(role)));
					// crear el passsword de autenticacion
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							username, null, authorities);
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
					filterChain.doFilter(request, response);
				} catch (Exception e) {
					System.out.println("error login " + e.getMessage());
					response.setHeader("error", e.getMessage());
					response.setStatus(FORBIDDEN.value());
					Map<String, String> error = new HashMap<>();
					error.put("error_message", e.getMessage());
					response.setContentType(APPLICATION_JSON_VALUE);
					new ObjectMapper().writeValue(response.getOutputStream(), error);
				}

			} else {
				filterChain.doFilter(request, response);
			}
		}

	}

}
