package com.ipasal.ipasalwebapp;


import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@EnableConfigurationProperties
@SpringBootApplication
public class IpasalwebappApplication {

	@Value("${http.port}")
	private int httpPort;

	@Value("${server.port}")
	private static int serverPort;
	
    public static void main(String[] args) {
		System.out.println("ipasal with port" + serverPort);
        SpringApplication.run(IpasalwebappApplication.class, args);
    }
    
    //Create bean for rest template. Using rest template we can call remote rest api.
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
    
    @Bean
    ObjectMapper getObjectMapper() {
    	return new ObjectMapper();
    }
    
 // Lets configure additional connector to enable support for both HTTP and HTTPS
	/*
	 * @Bean
	 * 
	 * @Profile("ishani-prod") public ServletWebServerFactory servletContainer() {
	 * TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory()
	 * {@Override protected void postProcessContext(Context context) {
	 * SecurityConstraint securityConstraint = new SecurityConstraint();
	 * securityConstraint.setUserConstraint("CONFIDENTIAL"); SecurityCollection
	 * collection = new SecurityCollection(); collection.addPattern("/*");
	 * securityConstraint.addCollection(collection);
	 * context.addConstraint(securityConstraint); } };
	 * tomcat.addAdditionalTomcatConnectors(createStandardConnector()); return
	 * tomcat; }
	 */

	/*
	 * private Connector createStandardConnector() { Connector connector = new
	 * Connector("org.apache.coyote.http11.Http11NioProtocol");
	 * connector.setScheme("http"); connector.setPort(httpPort);
	 * connector.setSecure(false); connector.setRedirectPort(443); return connector;
	 * }
	 */
}
