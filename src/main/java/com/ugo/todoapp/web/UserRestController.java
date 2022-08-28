package com.ugo.todoapp.web;

import com.ugo.todoapp.core.user.domain.User;
import com.ugo.todoapp.security.UserSession;
import com.ugo.todoapp.security.UserSessionRepository;
import com.ugo.todoapp.web.model.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController

public class UserRestController {

    private final UserSessionRepository sessionRepository;

    public UserRestController(UserSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/api/user/profile")
    public ResponseEntity<UserProfile> userProfile(UserSession userSession){

        if(Objects.nonNull(userSession)){
            return ResponseEntity.ok(new UserProfile(userSession.getUser()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
