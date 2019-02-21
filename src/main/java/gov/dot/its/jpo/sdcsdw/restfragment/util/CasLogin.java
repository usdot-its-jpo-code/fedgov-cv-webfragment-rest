package gov.dot.its.jpo.sdcsdw.restfragment.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.security.sasl.AuthenticationException;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Wrapper for logging into the out-of-date CAS used in the SDC/SDW
 * @author Andrew M Melnick
 *
 */
public class CasLogin {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(CasLogin.class.getName());
    
    public static final String COOKIE_HEADER_NAME = "Cookie";
    public static final String SET_COOKIE_HEADER_NAME = "Set-Cookie";
    public static final String TICKET_GRANTING_TICKET_COOKIE_NAME = "CASTGC";
    public static final String JSESSION_ID_COOKIE_NAME = "JSESSIONID";
    public static final String USERNAME_INPUT_NAME = "username";
    public static final String PASSWORD_INPUT_NAME = "password";
    public static final String LOGIN_TICKET_INPUT_NAME = "lt";
    public static final String EXECUTION_INPUT_NAME = "execution";
    public static final String EVENT_ID_INPUT_NAME = "_eventId";
    public static final String SUBMIT_INPUT_NAME = "submit";
    public static final String SUBMIT_INPUT_LOGIN = "LOGIN";
    
    public static final String INPUT_VALUE_ATTR = "value";
    
    private String username;
    private String password;
    private String casUrl;
    private RestTemplate restTemplate;
    
    
    /**
     * Construct a CAS login wrapper
     * @param username Username to log in with
     * @param password Password to log in with
     * @param casUrl Login URL of the CAS
     * @param restTemplate A pre-configured rest template to use
     */
    public CasLogin(String username, String password, String casUrl, RestTemplate restTemplate) {
        this.username = username;
        this.password = password;
        this.casUrl = casUrl;
        this.restTemplate = restTemplate;
    }
    
    /**
     * Log into the CAS and return the Ticket Granting Ticket (TGT) received
     * @param serviceUrl 
     * @return Ticket Granting Ticket (TGT) received
     * @throws IOException Authentication failed
     */
    public String getTicketGrantingTicket(String serviceUrl) throws IOException {
        
        LoginInfo loginInfo = getLoginInfo();
        
        return getTicketGrantingTicket(username, password, loginInfo);
    }
    
    private static class LoginInfo {
        private String loginTicket;
        private String execution;
        private String eventId;
        private String jsessionId;
        
        public String loginTicket() { return loginTicket; }
        public LoginInfo loginTicket(String loginTicket) { this.loginTicket = loginTicket; return this; }
        
        public String execution() { return execution; }
        public LoginInfo execution(String execution) { this.execution = execution; return this; }
        
        public String eventId() { return eventId; }
        public LoginInfo eventId(String eventId) { this.eventId = eventId; return this; }
        
        public String jsessionId() { return jsessionId; }
        public LoginInfo jsessionId(String jsessionId) { this.jsessionId = jsessionId; return this; }
    }
    
    private static String getKvp(List<String> headerValues, String key) {
        if (headerValues == null) {
            return null;
        }
        for (String headerValue : headerValues) {
            String[] entry = headerValue.split("=", 2);
            if (entry.length < 2) {
                LOGGER.warn("Ingoring bad kvp " + headerValue);
            }
            String entryKey = entry[0];
            String entryValue = entry[1];
            if (entryKey.equals(key)) {
                return entryValue;
            }
        }
        return null;
    }
    
    private static String getKvpHeader(HttpHeaders headers, String headerName, String key) {
        return getKvp(headers.get(headerName), key);
    }
    
    private static <T> String getKvpHeader(ResponseEntity<T> response, String headerName, String key) {
        return getKvpHeader(response.getHeaders(), headerName, key);
    }
    
    private static String tryFindInputValue(Document doc, String name) throws AuthenticationException {
        Elements elements = doc.select("input[name=" + name + "]");
        
        switch (elements.size()) { 
        case 0:
            throw new AuthenticationException("Failed to load login ticket: No " + name + " element present");
        case 1:
            return elements.get(0).attr(INPUT_VALUE_ATTR);
        default:
            throw new AuthenticationException("Failed to load login ticket: Multiple " + name + " elements present");
        }
    }
    
    private LoginInfo getLoginInfo() throws IOException {
        ResponseEntity<String> response = restTemplate.getForEntity(casUrl, String.class);
       
        if (response.getStatusCode() != HttpStatus.OK) {
            throw new AuthenticationException("Failed to load login ticket: " + response.toString());
        }
        
        Document loginPage = Jsoup.parse(response.getBody());
        
        try {
            return new LoginInfo()
                .loginTicket(tryFindInputValue(loginPage, LOGIN_TICKET_INPUT_NAME))
                .execution(tryFindInputValue(loginPage, EXECUTION_INPUT_NAME))
                .eventId(tryFindInputValue(loginPage, EVENT_ID_INPUT_NAME))
                .jsessionId(getKvpHeader(response, SET_COOKIE_HEADER_NAME, JSESSION_ID_COOKIE_NAME));
        } catch (AuthenticationException ex) {
            throw new AuthenticationException(ex.getMessage() + ", " + response.toString(), ex.getCause());
        }
    }
    
    /**
     * Gets the TGT for the given username and password
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    private String getTicketGrantingTicket(String username, String password, LoginInfo loginInfo) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set(COOKIE_HEADER_NAME, JSESSION_ID_COOKIE_NAME + "=" + loginInfo.jsessionId());
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add(USERNAME_INPUT_NAME, username);
        formData.add(PASSWORD_INPUT_NAME, password);
        formData.add(LOGIN_TICKET_INPUT_NAME, loginInfo.loginTicket());
        formData.add(EXECUTION_INPUT_NAME, loginInfo.execution());
        formData.add(EVENT_ID_INPUT_NAME, loginInfo.eventId());
        formData.add(SUBMIT_INPUT_NAME, SUBMIT_INPUT_LOGIN);
        
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        
        ResponseEntity<String> response = restTemplate.postForEntity(casUrl, request, String.class);
        
        if(response.getStatusCode() != HttpStatus.OK && response.getStatusCode() != HttpStatus.FOUND) {
            throw new AuthenticationException("bad username or password: " + response.toString());
        }
        
        String ticket = getKvpHeader(response, SET_COOKIE_HEADER_NAME, TICKET_GRANTING_TICKET_COOKIE_NAME);
        
        if (ticket == null) {
            throw new AuthenticationException("Error communicating with CAS: Login OK but did not receive a TGT, " + response.toString());
        } else {
            return ticket;
        }
    }
}