package mk.learning.fileshare.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import mk.learning.fileshare.Services.AuthenticationService;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	AuthenticationService authService;
	
	
	@Override
	protected void configure(HttpSecurity http) {
		try {
			http.csrf().disable();
			http.authorizeRequests()
			//.antMatchers("/bootstrap/**","/Login.css","/Login.js","/logo.jpg").permitAll()
			.antMatchers("/bootstrap/**","/HTML/*","/Images/**").permitAll()
			.anyRequest().authenticated()
			.and()
			.formLogin().loginPage("/login")
			.failureUrl("/login?failed").permitAll()
			.defaultSuccessUrl("/welcome",false);
			
			http.authenticationProvider(authService);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
