package com.elhadjium.PMBackend;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.LocaleResolver;  

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class PmBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PmBackendApplication.class, args);
	}
	
	@Bean
	public LocaleResolver localeResolver() {
	    SessionLocaleResolver slr = new SessionLocaleResolver();
	    slr.setDefaultLocale(Locale.US);
	    return slr;
	}
	
	@Bean  
	public ResourceBundleMessageSource messageSource()  
	{  
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();  
		messageSource.setBasename("messages");  
		return messageSource;  
	}  
}
