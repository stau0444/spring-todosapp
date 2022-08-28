package com.ugo.todoapp.security.web.servlet;

import com.ugo.todoapp.security.UserSession;
import com.ugo.todoapp.security.UserSessionRepository;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class UserSessionHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserSessionRepository userSessionRepository;

    public UserSessionHandlerMethodArgumentResolver(UserSessionRepository userSessionRepository) {
        this.userSessionRepository = userSessionRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        //해당 파라미터 타입이 UserSession 타입에서 파생된 타입인지 체크
        //true일 경우 UserSessionHandlerMethodArgumentResolver가 동작한다.
        return UserSession.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return userSessionRepository.get();
    }
}
