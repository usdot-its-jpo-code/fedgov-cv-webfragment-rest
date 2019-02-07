package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@ComponentScan
@Configuration
@EnableWebSecurity
public class BasicAuthCASSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(BasicAuthCASSecurityConfigurerAdapter.class);
    
    private CASAuthenticationProvider authenticationProvider;
    
    @Autowired
    public BasicAuthCASSecurityConfigurerAdapter(CASAuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
          .csrf().disable()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
          .and()
          .authorizeRequests().antMatchers("/rest/v2/health").permitAll()
          .and()
          .authorizeRequests().antMatchers("/rest/**").authenticated()
          .and()
          .httpBasic()
          .and()
          .authenticationProvider(authenticationProvider);
    }
}
