package gov.dot.its.jpo.sdcsdw.restfragment.config;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import gov.dot.its.jpo.sdcsdw.restfragment.util.CasLogin;

@Component
public class CASAuthenticationProvider implements AuthenticationProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(CASAuthenticationProvider.class);
    
    private CasLoginConfig casLoginConfig;
    
    @Autowired
    public CASAuthenticationProvider(CasLoginConfig casLoginConfig) {
        this.casLoginConfig = casLoginConfig;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException
    {
        LOGGER.info("Trying to authenticate with CAS");
        
        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            
            final Object principal = authentication.getPrincipal();
            final Object credentials = authentication.getCredentials();
            
            if (principal instanceof String && credentials instanceof String) {
                final String username = (String)principal;
                final String password = (String)credentials;
                
                CasLogin login = new CasLogin(username, password, casLoginConfig.getCasUrl(), new RestTemplate());
                
                try {
                    String ticket = login.getTicketGrantingTicket(casLoginConfig.getServiceUrl());
                    return new UsernamePasswordAuthenticationToken(username, ticket, new ArrayList<>());
                } catch(javax.security.sasl.AuthenticationException ex) {
                    throw new BadCredentialsException(ex.getMessage(), ex);
                } catch (IOException ex) {
                    throw new AuthenticationServiceException("Error while logging into CAS", ex);
                }
            } else {
                LOGGER.warn("Could not authenticate with CAS, principal and credentials were not strings");
                return null;
            }
        } else {
            LOGGER.warn("Could not authenticate with CAS, not a username/password authentication");
            return null;
        }
    }

    public boolean supports(Class<?> authentication)
    {
        LOGGER.info("Checking if CAS authentication is supported");
        
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    
    
    
    
}


