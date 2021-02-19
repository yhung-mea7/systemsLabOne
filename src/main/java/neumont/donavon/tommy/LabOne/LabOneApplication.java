package neumont.donavon.tommy.LabOne;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.web.filter.ForwardedHeaderFilter;

import javax.servlet.DispatcherType;
import java.util.List;


@SpringBootApplication
@EnableCaching
public class LabOneApplication {

	@Bean
	FilterRegistrationBean<ForwardedHeaderFilter> forwardedHeaderFilter()
	{
		ForwardedHeaderFilter filter = new ForwardedHeaderFilter();
		FilterRegistrationBean<ForwardedHeaderFilter> registration = new FilterRegistrationBean<>(filter);
		registration.setDispatcherTypes(DispatcherType.REQUEST, DispatcherType.ASYNC, DispatcherType.ERROR);
		registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
		registration.setUrlPatterns(List.of("/absoluteURLWithFilter"));
		return registration;
	}

	public static void main(String[] args) {

		SpringApplication.run(LabOneApplication.class, args);
	}

}
