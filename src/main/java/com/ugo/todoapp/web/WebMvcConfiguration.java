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
 * Spring Web MVC ??????
 *
 * @author springrunner.kr@gmail.com
 */
@Configuration
//????????? MVC ?????? ??? ?????????
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
    //?????? ?????? ?????? ?????? ??????
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        ????????? ???????????? ?????? , ?????? ????????? ?????? ?????? ?????????
//        /????????? ???????????? ???????????? ??????????????????
//         /assets ?????? ?????? ?????????  asset/?????? ??????????????? ??????.
        registry.addResourceHandler("/static/assets/**")
                .addResourceLocations("classpath:static/assets/");
//
//        ?????? ???????????? ???????????? ??????
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
        logger.debug("webMVCConfiguration  ?????? ???");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
         //registry.enableContentNegotiation(new CommaSeparatedValuesView());
        // ?????? ?????? ?????? ????????????, ?????????????????? ????????? ContentNegotiatingViewResolver ????????? ????????????.
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
     * ?????????????????? ????????? ContentNegotiatingViewResolver??? ????????? ???????????? ????????? ????????????
     */
    @Configuration
    public static class ContentNegotiationCustomizer {

        @Autowired
        public void configurer(ContentNegotiatingViewResolver viewResolver) {
            // TODO ContentNegotiatingViewResolver ????????? ??????
            List<View> defaultViews =  new ArrayList<>(viewResolver.getDefaultViews());
            defaultViews.add(new CommaSeparatedValuesView());
            viewResolver.setDefaultViews(defaultViews);
        }

    }

}
