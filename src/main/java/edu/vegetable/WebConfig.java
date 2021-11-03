package edu.vegetable;

import edu.vegetable.Interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnClass(WebMvcConfigurer.class)
public class WebConfig {

    @Configuration
    @ConditionalOnWebApplication
    public static class CustomWebMvcConfigure implements WebMvcConfigurer{

        @Autowired
        LoginInterceptor loginInterceptor;

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(loginInterceptor).addPathPatterns("/**_manage.html","/add_**.html","/delete_**.html","/update_**.html","/main.html")
                    .excludePathPatterns("/", "/login.html", "**/css/**","**/fonts/**","**/img/**","**/js/**","/mobile_**");
        }
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {

            // 注意如果filePath是写死在这里，一定不要忘记尾部的/或者\\，这样才能读取其目录下的文件
            registry.addResourceHandler("/**").addResourceLocations(
                    "classpath:/META-INF/resources/",
                    "classpath:/resources/",
                    "classpath:/static/",
                    "classpath:/public/",
                    "file:/resources/static/uploadfile/image/",
                    "classpath:/webapp/");
        }



    }

}
