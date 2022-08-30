package com.ugo.todosapp.web;

import com.ugo.todoapp.TodosApplication;
import com.ugo.todoapp.core.user.domain.User;
import com.ugo.todoapp.security.UserSession;
import com.ugo.todoapp.security.UserSessionRepository;
import com.ugo.todoapp.web.LoginController;
import com.ugo.todoapp.web.model.SiteProperties;
import org.hamcrest.core.StringEndsWith;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.function.BodyInserters.*;


@SpringBootTest(
        classes = {TodosApplication.class} ,
        //테스트시 기존의 포트와 충돌하지 않는 랜덤한 포트를 사용하도록 한다.
        webEnvironment = WebEnvironment.RANDOM_PORT
)
public class LoginControllerE2ETests {

    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    UserSessionRepository repository;
    @Autowired
    LoginController controller;

    WebTestClient webTestClient;
    WebClient webClient;

    // 스프링 부트가 webEnvironment로 랜덤포트를 사용하여 테스트를 실행하고
    // 테스트 메서드에서 @LocalServerPort을 이용해 해당 포트번호를 받아와 사용할 수 있다.
    @BeforeEach
    void setUp(@LocalServerPort int port){
        webClient = WebClient.create("http://localhost:"+port);
        webTestClient = WebTestClient.bindToServer().baseUrl("http://localhost:"+port).build();
    }
    @Test
    void show_login_form_when_unauthorized(@Autowired SiteProperties siteProperties){
        webTestClient.get().uri("/login").exchange().expectAll(
          spec -> spec.expectStatus().isOk(),
                spec -> spec.expectBody()
                        .xpath("//input[@name='username']").exists()
                        .xpath("//input[@name='password']").exists()
                        .xpath("//a[text()='"+ siteProperties.getAuthor()+"']").exists()

        );
    }

    @Test
    void show_todoList_form_when_authorized(){

        MultiValueMap<String , String> cookieStore = new LinkedMultiValueMap<>();

        //form data 구성
        MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();{
            formData.add("username","user");
            formData.add("password","password");
        }

        FormInserter<String> formBody =BodyInserters.fromFormData(formData);

        webClient.post().uri("/login").body(formBody).exchangeToMono(
                resp->{
                    resp.cookies().forEach((name, cookies)->{
                        cookies.forEach(cookie->cookieStore.add(name,cookie.getValue()));
                    });
                    return Mono.empty();
                }
        ).block();
        //2. 로그인시 일어날 일들 검증
        webTestClient.get().uri("/login")
                .cookies(c->c.addAll(cookieStore))
                .exchange()
                .expectAll(
                        spec->spec.expectStatus().is3xxRedirection(),
                        spec->spec.expectHeader().value(HttpHeaders.LOCATION, StringEndsWith.endsWith("/todos"))
                );
    }

}
