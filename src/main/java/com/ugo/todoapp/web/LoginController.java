package com.ugo.todoapp.web;

import com.ugo.todoapp.core.user.application.UserPasswordVerifier;
import com.ugo.todoapp.core.user.application.UserRegistration;
import com.ugo.todoapp.core.user.domain.User;
import com.ugo.todoapp.core.user.domain.UserEntityNotFoundException;
import com.ugo.todoapp.core.user.domain.UserPasswordNotMatchedException;
import com.ugo.todoapp.security.UserSession;
import com.ugo.todoapp.security.UserSessionRepository;
import com.ugo.todoapp.web.model.SiteProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.Objects;

@Controller
public class LoginController {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final UserPasswordVerifier userPasswordVerifier;
    private final UserRegistration userRegistration;
    private final UserSessionRepository sessionRepository;


    public LoginController(
            UserPasswordVerifier userPasswordVerifier,
            UserRegistration userRegistration,
            UserSessionRepository sessionRepository
    ) {
        this.userPasswordVerifier = userPasswordVerifier;
        this.userRegistration = userRegistration;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/login")
    public String  loginPage(){
        UserSession userSession = sessionRepository.get();
        if(Objects.nonNull(userSession)){
            return "redirect:/todos";
        }
        return "login";
    }


    @PostMapping("/login")
    public String loginProcess(@Valid LoginCommand command ,Model model){
        User user;
        try{
            user = userPasswordVerifier.verify(command.getUsername(),command.getPassword());
        }catch (UserEntityNotFoundException e){
            user = userRegistration.join(command.getUsername(),command.getPassword());
        }

        sessionRepository.set(new UserSession(user));
        return "redirect:/todos";
    }

    @ExceptionHandler(BindException.class)
    public String handleBindException(BindException error, Model model){
        model.addAttribute("bindingResult",error.getBindingResult());
        model.addAttribute("message","입력값이 올바르지 않습니다.");
        return "login";
    }

    @ExceptionHandler(UserPasswordNotMatchedException.class)
    public String handleUserPasswordNotValidException(UserPasswordNotMatchedException error, Model model){
        model.addAttribute("message","비밀번호가 일치하지 않습니다.");
        return "login";
    }

    static class LoginCommand{
        @Size(min=4 , max= 14)
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public LoginCommand(String username, String password) {
            this.username = username;
            this.password = password;
        }

        @Override
        public String toString() {
            return "LoginCommand{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }
    }
}
