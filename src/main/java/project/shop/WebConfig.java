package project.shop;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import project.shop.web.NumberFormatter;
import project.shop.web.interceptor.LoginInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new LoginInterceptor())
            .order(1)
            .addPathPatterns("/**")
            .excludePathPatterns("/", "/css/**", "/errors", "/error" ,"/fragment/**", "/login", "/logout", "/join", "/item/itemInfo/**", "/images/**", "/item/addComment/**", "/api/**");
  }

  @Override
  public void addFormatters(FormatterRegistry registry) {
    registry.addFormatter(new NumberFormatter());
  }

}
