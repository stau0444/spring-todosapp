package com.ugo.todoapp.web;

import com.ugo.todoapp.commons.web.servlet.ExecutionTimeHandlerInterceptor;
import com.ugo.todoapp.commons.web.servlet.LoggingHandlerInterceptor;
import com.ugo.todoapp.commons.web.view.CommaSeparatedValuesView;
import com.ugo.todoapp.core.todos.domain.Todo;
import com.ugo.todoapp.core.user.domain.ProfilePictureStorage;
import com.ugo.todoapp.security.UserSessionRepository;
import com.ugo.todoapp.security.web.servlet.RolesVerifyHandlerInterceptor;
import com.ugo.todoapp.security.web.servlet.UserSessionFilter;
import com.ugo.todoapp.security.web.servlet.UserSessionHandlerMethodArgumentResolver;
import org.apache.juli.logging.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ObjectToStringHttpMessageConverter;
import org.springframework.web.filter.CommonsRequestLoggingFilter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.ugo.todoapp.web.TodoController.*;

/**
 * Spring Web MVC 설정
 *
 * @author springrunner.kr@gmail.com
 */
@Configuration
//스프링 MVC 전략 빈 설정자
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private UserSessionRepository userSessionRepository;

    @Autowired
    private ProfilePictureStorage pictureStorage;

//    @Override
//    public void addReturnValueHandlers(List<HandlerMethodReturnValueHandler> handlers) {
//        handlers.add(new UserController.ProfilePictureReturnValueHandler(pictureStorage));
//    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new UserSessionHandlerMethodArgumentResolver(userSessionRepository));
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new RolesVerifyHandlerInterceptor());
        registry.addInterceptor(new LoggingHandlerInterceptor());
        registry.addInterceptor(new ExecutionTimeHandlerInterceptor());
    }
    //정적 자원 제공 전략 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        리소스 핸들러를 등록 , 정적 자원을 제공 하는 핸들러
//        /서블릿 컨텍스트 경로에서 정적자원제공
//         /assets 으로 오는 경로를  asset/에서 제공한다는 의미.
        registry.addResourceHandler("/static/assets/**")
                .addResourceLocations("classpath:static/assets/");
//
//        파일 경로에서 정적자원 제공
//        registry.addResourceHandler("/assets/**")
//                .addResourceLocations("file:/Users/ugo/IdeaProjects/spring-web-mvc-study/todos/files/assets/");
//
//        registry.addResourceHandler("/assets/**")
//                .addResourceLocations(
//                        "classpath:static/assets/"
//                );
    }

    public WebMvcConfiguration() {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.debug("webMVCConfiguration  등록 됨");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
         //registry.enableContentNegotiation(new CommaSeparatedValuesView());
        // 위와 같이 직접 설정하면, 스프링부트가 구성한 ContentNegotiatingViewResolver 전략이 무시된다.
        //registry.viewResolver(new TodoCsvViewResolver());
    }


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        DefaultConversionService conversionService = new DefaultConversionService();
        conversionService.addConverter(new Converter<Todo, String>() {
            @Override
            public String convert(Todo source) {
                return source.toString();
            }
        });
        converters.add(new ObjectToStringHttpMessageConverter(conversionService));
    }

    @Bean
    public FilterRegistrationBean<CommonsRequestLoggingFilter> commonsRequestLoggingFilter(){
        FilterRegistrationBean<CommonsRequestLoggingFilter> filter = new FilterRegistrationBean<>();
        CommonsRequestLoggingFilter crlf = new CommonsRequestLoggingFilter();
        filter.setFilter(crlf);
        filter.setUrlPatterns(Collections.singletonList("/*"));
        return filter;
    }
    @Bean
    public FilterRegistrationBean<UserSessionFilter> userSessionFilter(){
        UserSessionFilter userSessionFilter = new UserSessionFilter(userSessionRepository);

        FilterRegistrationBean<UserSessionFilter> filter = new FilterRegistrationBean<>();
        filter.setFilter(userSessionFilter);
        filter.setUrlPatterns(List.of("/*"));
        return filter;
    }

    /**
     * 스프링부트가 생성한 ContentNegotiatingViewResolver를 조작할 목적으로 작성된 컴포넌트
     */
    @Configuration
    public static class ContentNegotiationCustomizer {

        @Autowired
        public void configurer(ContentNegotiatingViewResolver viewResolver) {
            // TODO ContentNegotiatingViewResolver 사용자 정의
            List<View> defaultViews =  new ArrayList<>(viewResolver.getDefaultViews());
            defaultViews.add(new CommaSeparatedValuesView());
            viewResolver.setDefaultViews(defaultViews);
        }

    }

}
