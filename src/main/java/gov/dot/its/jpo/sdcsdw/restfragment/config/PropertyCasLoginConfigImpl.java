package gov.dot.its.jpo.sdcsdw.restfragment.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
public class PropertyCasLoginConfigImpl implements CasLoginConfig
{
    private String casUrl;
    private String serviceUrl;
    
    @Autowired
    public PropertyCasLoginConfigImpl() {
        this.casUrl = System.getProperty("cas.server.login.url");
        this.serviceUrl = System.getProperty("whtools.server.prefix.url");
    }
    
    @Override
    public String getCasUrl()
    {
        return casUrl;
    }

    @Override
    public String getServiceUrl()
    {
        return serviceUrl;
    }

}
