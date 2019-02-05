package gov.dot.its.jpo.sdcsdw.restfragment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import gov.dot.its.jpo.sdcsdw.restfragment.rest.QueryController;

@Configuration
@ComponentScan(basePackages = {"gov.dot.its.jpo.sdcsdw.restfragment", "gov.dot.its.jpo.sdcsdw.restfragment.rest", "gov.dot.its.jpo.sdcsdw.restfragment.services"})
public class AppConfig {

    @Bean
    QueryController queryController() { return new QueryController(); }

}