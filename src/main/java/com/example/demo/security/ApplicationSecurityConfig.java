package com.example.demo.security;

import java.util.List;
import static org.springframework.http.HttpMethod.GET;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.example.demo.filters.CustomAuthenticationFilter;
import com.example.demo.services.AppUserService;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final AppUserService appUserService;

    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder, AppUserService appUserService) {
        this.passwordEncoder = passwordEncoder;
        this.appUserService = appUserService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        //sin trabajar con cookies es STATELESS
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        CustomAuthenticationFilter customAuthenticationFilter=new CustomAuthenticationFilter(authenticationManagerBean());
        //asi podemos cambiar la configuracion de la pagina de arranque
        //la configuracion por defecto para el login esta en la clase de la que hereda CustomAuthenticationFilter
        customAuthenticationFilter.setFilterProcessesUrl("/otro/login") ;//2
//        http.authorizeRequests().antMatchers("/login").permitAll();//1
        http.authorizeRequests().antMatchers("/otro/login/**").permitAll();//2
        http.authorizeRequests().antMatchers(GET,"/students/list").hasRole(ApplicationUserRol.GUEST.name());
        http.authorizeRequests().antMatchers(GET,"/students/student/*").hasRole(ApplicationUserRol.ADMIN.name());
//        http.addFilter(new CustomAuthenticationFilter(authenticationManagerBean()));//1
        http.addFilter(customAuthenticationFilter);//2
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        List<UserDetails> collect = appUserService.getUsers().stream().map(
                (appuser) -> User.builder()
                        .username(appuser.getUsername())
                        .password(passwordEncoder.encode(appuser.getPassword()))
                        .roles(appuser.getRoles())
                        .build()
        ).collect(Collectors.toList());
        System.err.println();
        return new InMemoryUserDetailsManager(collect);
    }
}
