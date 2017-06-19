package it.uniroma3.progetto.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.UserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		 inMemoryConfigurer()
	        .withUser("user")
	            .password("user")
	            .authorities("ADMIN")
	        .and()
	        .configure(auth);
		 auth.jdbcAuthentication().dataSource(dataSource);
//		.withUser("user").password("user").roles("ADMIN");
//		auth.jdbcAuthentication().dataSource(dataSource);
		System.out.println("OOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");

		//		
		//		.passwordEncoder(new BCryptPasswordEncoder())
		//		.usersByUsernameQuery("SELECT username,password,1 FROM users where username=?")
		//		.authoritiesByUsernameQuery("SELECT username,authority FROM authorities where username=?");
	}

	private InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder>
    inMemoryConfigurer() {
	return new InMemoryUserDetailsManagerConfigurer<>();
}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http
		.authorizeRequests()
		.antMatchers("/","/informazioniAutore","/informazioniQuadro","/css/**","/img/**").permitAll()
			.antMatchers("/inserisciQuadro" , "/inserisciAutore","/pannelloAmministratore").access("hasAuthority('ADMIN')")
			.anyRequest().authenticated()
		.and()
		.formLogin()
			.loginPage("/login")
			.permitAll()
		.and()
		.logout()
		.logoutSuccessUrl("/?logout")
			.permitAll();
//	        .rememberMe()
//	        	.key("$2a$04$dbc42eVSPP.GzzwGVzjkP.AGUeZuiO/2tdGaFReM17WzmJ9o7DsMW")
//	        	.rememberMeParameter("ricordami")
//	        	.and()
//	        .logout()
//	        	.logoutSuccessUrl("/?logout")
//	            .permitAll()
//	            .and()
//	        .sessionManagement()
//	        	.maximumSessions(1)
//	        	.expiredUrl("/login?expired");
    }
}