package com.ugo.todoapp.security.web.servlet;

import com.ugo.todoapp.security.AccessDeniedException;
import com.ugo.todoapp.security.UnauthorizedAccessException;
import com.ugo.todoapp.security.UserSession;
import com.ugo.todoapp.security.UserSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import com.ugo.todoapp.security.support.RolesAllowedSupport;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.AnnotatedElement;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Role(역할) 기반으로 사용자가 사용 권한을 확인하는 인터셉터 구현체
 *
 * @author springrunner.kr@gmail.com
 */
public class RolesVerifyHandlerInterceptor implements HandlerInterceptor, RolesAllowedSupport {

    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Override
    public final boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //HandlerMethod는 컨트롤러의 핸들러를 의미한다.

        if(handler instanceof HandlerMethod){
            RolesAllowed rolesAllowed = ((HandlerMethod) handler).getMethodAnnotation(RolesAllowed.class);
            if(Objects.isNull(rolesAllowed)){
                rolesAllowed = AnnotatedElementUtils.findMergedAnnotation(((HandlerMethod) handler).getBeanType(), RolesAllowed.class);
            }
            if(Objects.nonNull(rolesAllowed)){
                log.debug("verify roles-allowed: {}" , rolesAllowed);

                if(Objects.isNull(request.getUserPrincipal())){
                    throw new UnauthorizedAccessException();
                }
                //권한체크
                Set<String> matchedRoles = Stream.of(rolesAllowed.value())
                        .filter(request::isUserInRole)
                        .collect(Collectors.toSet());

                log.debug("matchedRoles : {}" , matchedRoles);
                //실행 권한 없음
                if(matchedRoles.isEmpty()){
                   throw new AccessDeniedException();
                }
            }
        }
        return true;
    }

}
