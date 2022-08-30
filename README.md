
## Project for Spring web MVC study

---

### 웹 정적 자원 설정

- 서블릿 컨텍스트 경로(webapp)에 정적자원을 제공
- 클래스 경로로부터 정적자원 제공
- 파일 경로로부터 정적자원 제공

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

---

### 외부의 애플리케이션 설정정보 다루기

#### 1.Environment 인터페이스

- 스프링은 Environment 인터페이스를 통해 다양한 방식으로 외부로부터 설정정보를 가져오는 추상화된 코드를 제공한다.
- 다른 설정이 없다면 기본적으로 application.yml(혹은 properties)로 부터 설정정보를 가져온다

#### 2.@Value("${설정 옵션명}") 

- @Value 어노테이션을 사용하면 ${}안의 옵션에 해당하는 데이터를  스프링이 주입해준다.

#### 3.@ConfigurationProperties(prefix = "") (springboot)

- 스프링 부트에선 @ConfigurationProperties 제공하여 외부 설정정보를 읽어 올 수 있다
- 외부 설정정보로 사용할 객체를 bean으로 선언해줘야한다.
- 외부 설정정보로 사용할 클래스에 선언하면 되고, 
- 외부 설정 정보에서 prefix로 제공한 데이터,해당 클래스의 프로퍼티에 맞는 </br> 옵션을 찾고 해당 데이터를 가져온다.
- @ConfigurationPropertiesScan을 엔트리 포인트에 선언해주면
- 설정정보 객체를 따로 빈으로 선언하지 않아도 @ConfigurationProperties를 찾아 빈으로 만들어준다,
* SiteProperties 클래스 참고

---

### 더 간결하게 모델과 뷰를 다루는 방식

- ModelAndView 객체를 사용하지 않고 Model 인터페이스를 
- 핸들러의 파라미터로 받아 모델 객체를 사용하고 
- 리턴타입을 String으로 선언한 후 viewName을 반환해주면 더 간결하게 코드를 작성할 수 있다.
- 핸들링하는 요청경로와 view의 이름이 같다면 view 이름을 return을 하지 않아도 요청경로를 view 이름으로 사용한다.

````java
    //ModelAndView를 쓰는 것보다 훨씬 코드가 간결해진다.
    @RequestMapping("/todos")
    public String todos(Model model){
        model.addAttribute("site",siteProperties);
    }
````

- 만약 화면을 구성하는 용도로만 사용하는 모델이라면
- 모델을 리턴하는 메서드를 만들고 메서드에 @ModelAttribute("모델이름") 선언하면
- 화면 구성시 해당 

````java
    
    @ModelAttribute("site")
    public SiteProperties siteProperties(){
        return new SiteProperties();
    }
    @RequestMapping("/todos")
    public String todos(){
        //site라는 모델을 따로 model로 반환하지 않아도 된다.
    }
````


---

### 빈 초기화 

- 빈이 생성되는 시점에 필요한 초기화 작업을 할 수 있다.
- InitializingBean 인터페이스 구현
- ApplicationRunner 인터페이스 구현 (springboot)
- CommandLineRunner 인터페이스 구현 (springboot)

#### @ConditionalOnProperty() 

- application.properties에 정의된 특정 프로퍼티의 값에 따라 
- 해당 어노테이션이 선언된 클래스가 bean으로 생성될지 말지를 정할 수 있다.
- 어노테이션의 옵션 중 name 으로 프로퍼티를 지정할 수 있으며 , havingValue를 통해 해당 프로퍼티 특정 값일 경우를 지정할 수있다.
- @ConditionalOn 으로 시작되는 어노테이션을 보면 Class , Bean , CloudPlatform 등 
- 여러 가지 상황에서 Conditional하게 빈을 생성할지 않할지를 설정할 수있다  

---

### 요청 페이로드 검증

#### jakarta bean validation

- 요청 데이타에 대한 검증을 해주는 라이브러리이다 .
- 요청 핸들러에서 요청 데이터를 읽어들일 때 검증할 수 있으며 요청데이터가 맵핑되는
- Command 클래에 필드에 필요한 어노테이션을 선언해주면된다.
- @NotBlank , @Size , @Email 등 데이터 검증에  필요한 여러 어노테이션이 있다.
- 요청 데이터가 맵핑되는 핸들러의 파라미터에서 @Valid 어노테이션 선언이 필요하다.


---

### 파일 업 / 다운로드 

#### 콘텐트 협상 (Content Nagotiation)

- HTTP에서 동일한 URL 리소스를 다양한 콘텐트 형식으로 제공하기 위해 사용되는 메커니즘이다.
- 응답 콘텐트 형식 선택은 클라이언트와 서버 간의 콘텐트 협상에 의해 결정된다.

#### View 기반의 컨텐츠 협상 전략

- 만약 View 객체를 리턴한다면 ContentNegotiatingViewResolver는 등록된 모든 viewResolver들로 부터 사용가능한 뷰를 후보로 등록한다.
- 이후 클라이언트가 요청한 Accept 헤더 기반으로 적합한 view를 선택한다.

#### MessageConverter 기반의 컨텐츠 협상 전량

- @ResponseBody가 붙어있는 핸들러는 RequestResponseBodyMethodProcessor를 통해 응답이 이뤄진다.
- RequestResponseBodyMethodProcessor 내부에는 등록한 여러개의 MessageConverter들이 등록되어있으며
- 요청 Accept 헤더에 맞는 컨버터를 통해 데이터가 객체로 객체가 데이터로 변환된다.


---

### 문자 인코딩(character encoding)

- 문자들의 집합을 부호화하는 방법 ,다시 말해 컴퓨터가 이용할 수 있는 신호로 만드는 것을 의미한다.
- 신호를 부  ,복호화하려면 미리 정해진 규칙을 바탕으로 처리해야 올바르게 문자를 다룰 수 있다.
- 부 , 복호화 규칙을 문자열 세트 또는 문자셋 이라 부르며 대표적으로 ASCII , UNICODE 등이 있다.
- 스프링 애플리케이션은 톰캣과 같은 서블릿 컨테이너안에서 구동되기 떄문에 서블릿 컨테이너의 설정을 기본적으로 따른다
- 톰캣의 경우  ISO8859-1을 기본 타입으로 사용하기 때문에 기본 설정정보를 바꿔줄 필요가 있다.



---

### 사용자 친화적 오류처리와 , I18N 국제화 처

#### 두가지 방식의 오류 처리 전략

#### 스프링 MVC 오류 처리 전략

- DispatcherServlet은  핸들러를 처리하는 중에 오류가 발생하면
- HandlerExceptionResolver 에게 오류 처리를 위임한다. 
- HandlerExceptionResolver는 여러개 등록될 수 있으며 
- 먼저 처리 되는 resolver가 반환하는 ModelAndView를 사용해 error.html을 구성한다.

#### 서블릿 컨테이너 오류 처리 전략

- 서블릿 컨테이너의 배포서술자 web.xml을 통해 오류 처리 전략을 설정할 수있다.
- HTTP 상태코드를 이용하거나 예외 타입에 따라서 오류를 처리할 수 있다.
- 두 방법 모두 location 태그를 이용해 해당 오류 발생시 특정 경로로 요청을 전달하여 예외를 처리한다

#### 스프링 부트의 오류 처리 전략

- 스프링부트는 위의 두가지 방식을 모두 사용하여 오류 처리 전략을 조금 더 쉽게 다룰 수 있는 방법을 제공한다.
- 스프링부트 기본 예외처리 전략에서는 메시지가 노출되지 않도록 설정되어 있기 떄문에 메시지를 보이고 싶다면 설정을 바꿔 줘야 한다.
- /templates/error 폴더에서 error 코드에 응답하는 html을 만들어 놓으면 특정 에러 발생시 그에 맞는 html을 반환한다.
- ex) 404.html -> page not fount


````
server:
  servlet:
    encoding:
      charset: utf-8
      force: true
  error:
    include-message: always
````


#### 스프링 부트의 MessageSourceAutoConfiguration

- 스프링 부트에서 제공하는 MessageSourceAutoConfiguration 클래스는 
- MessageSourceProperties에 정의된 MessageSource를 구성하는 정보들을 통해 MessageSource를 읽어들인다.
- MessageSourceProperties에는 base properties name 이 messages로 정의되어 있으므로 
- messages.properties 파일을 작성하면 MessageSource는 해당 프로퍼티 정보를 읽어 데이터를 가져온다.
- MessageSource의 getMessage() 메서드는  첫번째인자로 프로퍼티파일 내의 프로퍼티의 이름 , 두번째로는 동적으로 메시지를 구성하기 위한 요소를 담은 오브젝트 배열, 
- 세번째로는 프로퍼티가 존재하지 않을 경우에 사용할 defaultMessage , 네번째로는 국제화를 처리할 수 있도록하는 Locale 정보를 넘겨줄 수 있다.
- 오브젝트 배열로 넘긴 요소는 배열의 index에 따라 프로퍼티에서 {0}, {1} 과 같은 방식으로 사용할 수 있다.

#### MessageSourceResolvable 인터페이스

- MessageSource의 getMessage 메서드의 파라미터인 , code  , args , defaultMessage를 예외 클래스에서 미리 정의해 놓을 수 있게하는 인터페이스다
- 예외클래스에서 MessageSourceResolvable 인터페이스를 구현하면 MessageSource의 getMessage 메서드에서
- 첫번쨰 두번쨰 세번쨰 파라미터인 code  , args , defaultMessage 대신
- MessageSourceResolvable로 캐스팅한 에러객체를 넘겨서 해당객체에 정의된 code  , args , defaultMessage를 사용할 수 있다.
- 애플리케이션 내에서 최상위 예외클래스를 만들고  MessageSourceResolvable를 구현하고 다른 예외들이 해당 클래스를 상속받도록 하면 편리하다.

---


### 국제화 (i18n)

- 소프트웨어에서 다국어 지원을 의미한다.
- 웹 환경에서는 콘텐트 협상 매커니즘을 기반으로 제공한다.
- 기본적으로 사용자 요청의 Accept-Language 헤더를 기반으로 서버에서는 해당 언어정보에 맞는 데이터를 응답한다. 
- DispatcherServlet은  LocaleResolver를 이용해서 Local 결정 전략을 제공받는다. 
- Cookie , Session , Fixed를 통해서 Locale 정보를 얻을 수도 있다.
- 스프링의 MessageSource 인터페이스는 국제화를 고려해 만들어진 인터페이스이다.
- MessageSource 의 getMessage() 메서드 마지막 파라미터인 Locale을 통해 메시지의 Locale 정보가 결정되고
- 그에 맞는 메시지가 반환된다.

---


### 인증 , 인가 웹요청 보호


#### @ControllerAdvice

- 모든 컨트롤러에서 참조하는 정보를 정의해 놓을 수 있다.

#### BindingResult  , @ExceptionHandler

- BindResult는 사용자가 입력한 입력값을 담는 역할을 하며 BindException 발생시
- 핸들러 파리미터로 BindingResult를 직접받아 해당 예외를 처리할 수 있다.
- BindingResult를  핸들러 파라미터로 직접 받으면 스프링은 해당 오류를 개발자가 처리한다 인지하고 예외를 터뜨리지 않는다.
- ExceptionHandler를 통해 특정 예외에 대한 처리를 관리할 수 있다.
- ControllerAdvice에 ExceptionHandler를 등록해 전역적으로 동작하도록 할수 있다.

#### HttpEntity 

- 하나의 요청에서 여러개의 상황에 따라 다른 상태코드를 반환해줘야할 때 사용할 수 있다.
- ResponseEntity , RequestEntity 두가지 구현체가 있으며 응답 혹은 요청시에 사용할 수 있다.


#### @SessionAttributes , @SessionAttribute

[//]: # (- 핸들러에서 서블릿이 제공하는 HttpSession 세션을 사용할 수 있지만  한 기술에 의존적인 코드가 되어버릴 수 있다.)

[//]: # (- 스프링이 제공하는 @SessionAttributes , @SessionAttribute을 이용해 더 확장성 있는 코드 작성이 가능하다)

[//]: # (- @SessionAttributes는 컨트롤러 레벨에서 세션을 선언할때 사용하며 옵션으로 해당 세션의 이름을 지정할 수 있다.)

[//]: # (- @SessionAttributes에 선언된 세션은 핸들러에서 model.addAttributes&#40;&#41;에 model )

[//]: # (- )


#### 확장성 있는 웹 애플리케이션 아키텍쳐

- 확장성은 대규모 웹 애플리케이션을 설계할 때 고려 사항 중 하나이다 .
- 더 많은 부하를 처리할 수 있도록 처리량 증가를 목적으로 적용한다.
- 수직확장(scale up) , 수평확장(scale out) 두가지 개념이있다.
- 수직확장은 한 서버의 하드웨어를 업그레이드해 처리량을 증가시키는 방법이고
- 수평확장은 웹 서버의 수를 늘려 처리량을 증가시키는 방법이다.
- 스케일 업 방식에서 하드웨어의 업그레이드는 비싼 비용을 치르며 하드웨어적으로는 해결할 수 있는 한계가 존재한다.
- 떄문에 최근에 웹 애플리케이션에서 확장성을 고려할 땐 scale out 방식이 많이 사용되며
- 특히 클라우드 기반의 애플리케이션 서버 개발시 scale out에 대한 고려가 필요하다.

#### 다중 서버환경에서의 세션

- 다중 서버에서 브라우저가 항상 하나의 서버에 요청하게 된다는 보장이 없다.
- 때문에 로드벨런서와 스티키 세션또는 세션 클러스터링 이라는 기법이 사용된다.

#### 스티키 세션(sticky session)

- 로드밸런서는 브라우저와 서버 사이에서 브라우저의 요청이 어떤 서버로 전달되었는지 관리하며
- 만약 다음 요청이 있을때 같은 서버로 요청이 전달될 수 있게 라우팅 해주어 세션정보를 유지시킬 수 있다.

#### 세션 클러스터링(session clustering)

- 다중 서버의 세션을 동기화하는 기법이다 .
- 서버 수가 많지 않다면 상관없지만 서버 수가 늘어날 수록 동기화에 들어가는 비용이 커진다.
- 동기화 과정에서 서버 속도가 느려질 수 있다.

#### 세션 스토리지 (session storage)

- 별도의 세션을 위한 스토리지를 따로 준비해두고 각서버는 세션스토리지를 통해서 세션을 읽어 들인다.
- 대표적으로 레디스가 있으며 인메모리기반으로 동작하기 때문에 성능이 좋다.

---



#### HandlerMethodArgumentResolver

- HandlerMethodArgumentResolver를 통해 핸들러의 파라미터에 접근해 
- 파라미터가 특정 컨디션에 부합하는지를 판단하고 부합할 경우에 특정 로직을 거치도록 지정할 수 있다


---



### 소프트웨어 테스팅

- 시스템이 정해진 요구를 만족하는지 , 예상과 실제 결과가 어떤 차이를 보이는지 수동 또는 자동 방법을 동원하여 검사하고 평가하는 일련의 과정
- 테스트 과정을 통해 결과가 원하는데로 나오지 않는 경우 코드의 결함을 확인할 수 있고 이 결함을 제거하는 과정(debugging)을 통해 
- 최종적으로 테스트가 성공하면 결함이 제거되었음을 확인하고 믿을 수 있는 코드가 된다.

#### 단위 테스트(unit Test)

- 작은 코드 조각을 검증하고 ,  빠르게 수행행되며 ,격리된 방식으로 실행되는 테스트를 말한다.
- 일부 의존관계를 유지하고 비지니스 로직을 검증하는 테스트를 작성한다.
- 테스트 대상 외 모든 의존관계를 모의객체(mock)로 교체 후 작성할 수 도 있다.
- 단위 테스트가 말하는 단위가 어느정도인지 정해진 기준은 없다. 
- 충분히 하나의 관심에 집중해 효율적으로 테스트할 수 있는 만큼이 단위의 기준이 될 수 있다.
- 단위는 작을수록 좋으며 단위를 넘어서는 다른 코드는 신경쓰지 않고  참여하지도 않게 테스트를 작성하면 가장 좋다.

#### 통합 테스트(Integration Testing)

- 애플리케이션 내 모든 구성 요소간 상호 작용을 검증하기 위해 실행되는 테스트이다 .
- 데이터베이스나 메시지 브로커 등 공유 의존관계를 통합해 테스트한다.
- 외부 시스템이나 UI를 포함해 사용자 관점에서 기능을 검증하는 E2E도 통합 테스트의 일부이다.

#### 테스트 피라미드(TestPyramid)

- 테스트 피라미드는 효율적인 자동화된 테스트를 작성하기 위한 구조이다.
- 핵심은 느리고 비용이 많이 드는 통합 테스트나 E2E보다 저렴한 단위 테스트를 많이 만드는 것이다.


<img width="407" alt="스크린샷 2022-08-30 오후 9 42 09" src="https://user-images.githubusercontent.com/51349774/187439856-b01b4236-79b2-4930-95da-d7abeb78c856.png">



