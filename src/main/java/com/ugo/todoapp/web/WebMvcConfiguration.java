package com.ugo.todoapp.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;

/**
 * Spring Web MVC 설정
 *
 * @author springrunner.kr@gmail.com
 */
@Configuration
//스프링 MVC 전략 빈 설정자
public class WebMvcConfiguration implements WebMvcConfigurer {
    //정적 자원 제공 전략 설정
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        리소스 핸들러를 등록 , 정적 자원을 제공 하는 핸들러
//        /서블릿 컨텍스트 경로에서 정적자원제공
//         /assets 으로 오는 경로를  asset/에서 제공한다는 의미.
//        registry.addResourceHandler("/assets/**")
//                .addResourceLocations("assets/");
//
//        파일 경로에서 정적자원 제공
//        registry.addResourceHandler("/assets/**")
//                .addResourceLocations("file:/Users/ugo/IdeaProjects/spring-web-mvc-study/todos/files/assets/");
//
//        registry.addResourceHandler("/assets/**")
//                .addResourceLocations(
//                        "assets/",
//                        "classpath:assets/",
//                        "file:/Users/ugo/IdeaProjects/spring-web-mvc-study/todos/files/assets/"
//                );
    }

    public WebMvcConfiguration() {
        Logger logger = LoggerFactory.getLogger(getClass());
        logger.debug("webMVCConfiguration  등록 됨");
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        // registry.enableContentNegotiation();
        // 위와 같이 직접 설정하면, 스프링부트가 구성한 ContentNegotiatingViewResolver 전략이 무시된다.
    }

    /**
     * 스프링부트가 생성한 ContentNegotiatingViewResolver를 조작할 목적으로 작성된 컴포넌트
     */
    public static class ContentNegotiationCustomizer {

        public void configurer(ContentNegotiatingViewResolver viewResolver) {
            // TODO ContentNegotiatingViewResolver 사용자 정의
        }

    }

}
