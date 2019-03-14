package fr.bca.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@Configuration
@EnableWebMvc
public class MvcConfiguration extends WebMvcConfigurerAdapter {

//	@Bean
//	public ViewResolver getViewResolver() {
//		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//		resolver.setPrefix("/templates/");
//		resolver.setSuffix(".jsp");
//		return resolver;
//	}
	
	
//	@Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/usine").setViewName("/templates/usine");
//        registry.addViewController("/").setViewName("/WEB-INF/templates/usine");
//    }
//	  
	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	
//	  @Bean	  
//	  public ViewResolver viewResolver() {
//	        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();	        
//	        viewResolver.setTemplateEngine(templateEngine());
//	        viewResolver.setCharacterEncoding("UTF-8");
//	    return viewResolver;
//	}  
//	  
//	@Bean	    
//	public SpringTemplateEngine templateEngine() {
//	    
//	    SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//	    templateEngine.setTemplateResolver(templateResolver());
//	
//	    return templateEngine;
//	}
//
//	@Bean   
//	public ClassLoaderTemplateResolver templateResolver() {
//	    
//	    ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//	    
//	    templateResolver.setPrefix("templates/");
//	    templateResolver.setCacheable(false);
//	    templateResolver.setSuffix(".jsp");        
//	    templateResolver.setTemplateMode("HTML5");
//	    templateResolver.setCharacterEncoding("UTF-8");
//	    
//	    return templateResolver;
//	}
	 
	
	
}
