package com.example.demo.filters;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
//        log.info(username, ":", password);
        System.err.println(username+":"+password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    //este metodo es llamado despues del de arriba
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        //dependencia jwt
        //obtenemos el user pasado arriba y le aplicamos una encriptacion de hmac256
        User user = (User) authentication.getPrincipal();
        System.out.println("Sacando las autorities");
        for (GrantedAuthority au: user.getAuthorities()) {
            System.out.println(au.toString());
        }
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        //usamod JWT.create para crear el token. le ponemos estos datos
        String accesToken = JWT.create()
                .withSubject(user.getUsername())
                //TimeStamp es para pasar localDAte a DAte y digo de aqui a diez minutos
                .withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(10)))
                .withIssuer(request.getRequestURL().toString())
                .withArrayClaim("roles",
                        user
                                .getAuthorities()
                                .stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()).toArray(new String[0]))
        //si todo ha salido bien tengo un token para el usuario
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(Timestamp.valueOf(LocalDateTime.now().plusMinutes(30)))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        HashMap<String, String> tokens=new HashMap<>();
        tokens.put("access_token", accesToken);
        tokens.put("refresh_token", refreshToken);
        response.setContentType(APPLICATION_JSON_VALUE);
      //el objectMapper serializa cualquier objeto de java (en este caso tokens)
        //serializamosy enviamos el objeto al body mediante el outputstream
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
