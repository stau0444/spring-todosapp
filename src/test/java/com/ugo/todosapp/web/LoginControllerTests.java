package com.ugo.todosapp.web;

import com.ugo.todoapp.TodosApplication;
import com.ugo.todoapp.core.user.domain.User;
import com.ugo.todoapp.security.UserSession;
import com.ugo.todoapp.security.UserSessionRepository;
import com.ugo.todoapp.web.LoginController;
import com.ugo.todoapp.web.model.SiteProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

//스프링 테스트 확장팩
//@ExtendWith(SpringExtension.class)
//컨텍스트 구성정보
//@ContextConfiguration(classes= TodosApplication.class)
//스프링 웹 기반의 테스트임을 선언
//@WebAppConfiguration
//@ExtendWith , @ContextConfiguration 이 통합된 애노테이션
//@SpringJUnitConfig(TodosApplication.class)
//Spring 기반 웹 통합 테스트임을 선언하는 애노테이션
//@SpringJUnitWebConfig(TodosApplication.class)
//스프링 부트 기반 테스트임을 선언하는 어노테이션
//@SpringBootApplication이 선언된 클래스를 자동으로 찾아서 컨테이너를 구성한다
@SpringBootTest(classes = {TodosApplication.class})
public class LoginControllerTests {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserSessionRepository repository;
    @Autowired
    LoginController controller;

    MockMvc mockMvc;


    //movckMVC 구성
    @BeforeEach
    void setUp(WebApplicationContext wac){
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void componentScanTest(){
        //loginController가 빈으로 등록되어 있는지 확인
        boolean loginController = applicationContext.containsBean("loginController");
        //테스트 검증
        Assertions.assertTrue(loginController);
    }


    /** 핸들러의 기능은 검증하지만 전체적인 흐름은 검증하지 못하는 테스트 코드*/
    @Test
    void show_login_form_when_unauthorized_V2(){
        Assertions.assertEquals("login" , controller.loginPage());
    }

    //개발자가 직접 등록하는 빈의 경우 @Autowired를 붙혀줘야한다.
    @Test
    void show_login_form_when_unauthorized_V3(@Autowired SiteProperties siteProperties) throws Exception {
        //Assertions.assertEquals("login" , controller.loginPage());
        RequestBuilder request = MockMvcRequestBuilders.get("/login");
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.view().name("login"),
                MockMvcResultMatchers.model().attribute("site", siteProperties)
        );
    }


    /** 비효율적인 테스트 코드
     *  - 단위 테스트가 실행될때 마다 컨테이너가 생성,종료되고 있다.*/
    @Test
    void show_todoList_form_when_authorized(){
        //모의 request , response 객체를 만들고
        //MockHttpServletRequest request = new MockHttpServletRequest();
        //MockHttpServletResponse response = new MockHttpServletResponse();

        //컨텍스트에 등록한다.
        //RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request,response));

        //세션에 유저 저장
        //repository.set(new UserSession(new User("tester","")));

        //Assertions.assertEquals("redirect:/todos" , controller.loginPage());

        //RequestContextHolder.resetRequestAttributes();
    }

    @Test
    void show_todoList_form_when_authorized_v2() throws Exception {

        //get 요청 후에
        RequestBuilder request = MockMvcRequestBuilders.get("/login")
                .with(mockRequest ->{
                    //리퀘스트를 홀더에 등록하고
                    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(mockRequest));
                    //세션에 유저를 등록한후에
                    repository.set(new UserSession(new User("tester","")));
                    //리퀘스트를 반환한다
                    return mockRequest;
                });
        mockMvc.perform(request).andExpectAll(
                MockMvcResultMatchers.status().is3xxRedirection(),
                MockMvcResultMatchers.view().name("redirect:/todos")
        );
    }
}
