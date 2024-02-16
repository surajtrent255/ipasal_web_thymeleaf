package com.ipasal.ipasalwebapp.configs;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.config.FlowDefinitionRegistryBuilder;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.ViewFactoryCreator;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.webflow.view.AjaxThymeleafViewResolver;
import org.thymeleaf.spring5.webflow.view.FlowAjaxThymeleafView;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

@Configuration
@EnableAutoConfiguration(exclude = {ThymeleafAutoConfiguration.class, ReactiveSecurityAutoConfiguration.class})
public class ThymeleafWebflowConfig extends AbstractFlowConfiguration{

	@Autowired
	private LocalValidatorFactoryBean factoryBean;
	
	@Bean
	public FlowHandlerAdapter FlowHandlerAdapter() {
		FlowHandlerAdapter flowHandlerAdapter = new FlowHandlerAdapter();
		flowHandlerAdapter.setFlowExecutor(this.flowExecutor());
		flowHandlerAdapter.setSaveOutputToFlashScopeOnRedirect(true);
		return flowHandlerAdapter;
	}
	
	@Bean
	public FlowExecutor flowExecutor() {
		return this.getFlowExecutorBuilder(this.flowRegistry())
			.build();
	}

	@Bean
	public FlowHandlerMapping flowHandlerMapping() {
		FlowHandlerMapping flowHandlerMapping = new FlowHandlerMapping();
		flowHandlerMapping.setOrder(-1);
		flowHandlerMapping.setFlowRegistry(this.flowRegistry());
		return flowHandlerMapping;
	}
	
	@Bean
	public FlowDefinitionRegistry flowRegistryDefault() {
		FlowDefinitionRegistryBuilder builder = this.getFlowDefinitionRegistryBuilder();
			builder.setBasePath("classpath:flows/default")
				.setFlowBuilderServices(this.flowBuilderServices())
				.addFlowLocationPattern("/**/*-flow.xml");
		return builder.build();
	}
	
	@Bean
	public FlowDefinitionRegistry flowRegistry() {
		FlowDefinitionRegistryBuilder builder = this.getFlowDefinitionRegistryBuilder();
			 builder.setBasePath("classpath:flows/special")
				//.addFlowLocationPattern("/**/*-flow.xml")
				.setFlowBuilderServices(this.flowBuilderServices());
			 List<String> activeProfiles = Arrays.asList(getApplicationContext().getEnvironment().getActiveProfiles());
			 if(activeProfiles.contains("national")) {
				 builder.addFlowLocationPattern("/**/*-flow.xml");
			 }
			 
			 return builder.setParent(this.flowRegistryDefault())
			 	.build();
	}

	@Bean
	public FlowBuilderServices flowBuilderServices() {
		return this.getFlowBuilderServicesBuilder()
				.setViewFactoryCreator(this.viewFactoryCreator())
				.setValidator(this.factoryBean)
				.build();
	}

	@Bean
	public ViewFactoryCreator viewFactoryCreator() {
		MvcViewFactoryCreator viewFactoryCreator = new MvcViewFactoryCreator();
		viewFactoryCreator.setViewResolvers(Collections.singletonList(this.viewResolver()));
		viewFactoryCreator.setUseSpringBeanBinding(true);
		return viewFactoryCreator;
	}
	
	@Bean
	public AjaxThymeleafViewResolver viewResolver() {
		AjaxThymeleafViewResolver viewResolver = new AjaxThymeleafViewResolver();
		viewResolver.setViewClass(FlowAjaxThymeleafView.class);
		viewResolver.setTemplateEngine(this.templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		return viewResolver;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addDialect(new SpringSecurityDialect());
		templateEngine.setTemplateResolver(this.templateResolver());
		templateEngine.setEnableSpringELCompiler(true);
		return templateEngine;
	}

	@Bean
	public ClassLoaderTemplateResolver templateResolver() {
		ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
		templateResolver.setPrefix("templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setCacheable(true);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setTemplateMode("HTML5");
		return templateResolver;
	}
}
