package fr.bca.configuration;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.RedirectViewControllerRegistration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@ComponentScan(basePackages = "fr.bca")
public class Application extends WebMvcConfigurerAdapter{

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);

		System.out.println("Let's inspect the beans provided by Spring Boot:");

		String[] beanNames = ctx.getBeanDefinitionNames();
		Arrays.sort(beanNames);
		for (String beanName : beanNames) {
			System.out.println("Context --" + beanName);
		}
	}
	
//	 @Override
//	  public void addViewControllers (ViewControllerRegistry registry) {
//	      RedirectViewControllerRegistration r =
//	                registry.addRedirectViewController("/jenkins", "http://jenkins.bcaexpertise.org:8080/");
//	  }
}

//@SpringBootApplication
//@ComponentScan(basePackages = "fr.bca")
//class Application extends SpringBootServletInitializer {
//    static void main(String[] args) {
//        SpringApplication.run(Application.class, args);
//    }
// 
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(Application.class);
//    }
// 
//    @Bean
//    FilterRegistrationBean clickjackingPreventionFilter() {    	
//    	
//    	
//        return new FilterRegistrationBean("/**",new Filter() {
//                    @Override
//					public
//                    void init(final FilterConfig filterConfig) throws ServletException {
//                    }
// 
//                    @Override
//					public
//                    void doFilter(final ServletRequest servletRequest,
//                                  final ServletResponse servletResponse,
//                                  final FilterChain filterChain) throws IOException, ServletException {
//                        final HttpServletResponse response = (HttpServletResponse) servletResponse;
//                        response.addHeader("X-FRAME-OPTIONS", "DENY");
//                        filterChain.doFilter(servletRequest, servletResponse);
//                    }
// 
//                    @Override
//					public
//                    void destroy() {
//                    }
//                }
//        );
//    }
//}