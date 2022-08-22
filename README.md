
## Project for Spring web MVC study

---

### 웹 정적 자원 설정

- 서블릿 컨텍스트 경로(webapp)에 정적자원을 제공
- 파일 경로로 부터 정적자원 제공

````java
        //서블릿 컨텍스트 경로에서 정적자원제공
        // 해당 방식은 war 파일 방식의 패키징에서만 동작한다 
        // (jar 파일에서는 wabapp 디렉토리 사용 불가)
        // /assets 으로 오는 경로를  asset/에서 제공한다는 의미.
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("assets/");

        //파일 경로에서 정적자원 제공
        registry.addResourceHandler("/assets/**")
                .addResourceLocations("file:/Users/ugo/IdeaProjects/spring-web-mvc-study/todos/files/assets/");
        
        //클래스 경로로 부터 정적자원 제공
        registry.addResourceHandler("/assets/**")
        .addResourceLocations("classpath:/static/assets/");
````

---
### viewResolver

- view의 이름을 받아 해당 view name에 맞는 html을 취득 해당 html을 출력하여 view 객체를 생성하고. 이를 DispatcherServlet에게 넘겨준다.
- viewResolver에는 prefix ,suffix 설정을 갖고 있어 view name 만으로도 해당 html 파일을 찾아낼 수있다.
- 기본 prefix - classpath:/templates/
- 기본 suffix - .html
- fullViewName =  classpath:/templates/todos.html
- 위와 같은 형식으로 뷰의 경로를 찾는다.

### 외부의 애플리케이션 설정정보 다루기

#### 1.Environment 인터페이스

- 스프링은 Environment 인터페이스를 통해 다양한 방식으로 외부로부터 설정정보를 가져오는 추상화된 코드를 제공한다.
- 다른 설정이 없다면 기본적으로 application.yml(혹은 properties)로 부터 설정정보를 가져온다

#### 2.@Value("${설정 옵션명}") 

- @Value 어노테이션을 사용하면 ${}안의 옵션에 해당하는 데이터를  스프링이 주입해준다.

#### @ConfigurationProperties(prefix = "") 3.(springboot)

