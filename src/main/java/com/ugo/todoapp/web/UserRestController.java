package com.ugo.todoapp.web;

import com.ugo.todoapp.core.user.application.ProfilePictureChanger;
import com.ugo.todoapp.core.user.domain.ProfilePicture;
import com.ugo.todoapp.core.user.domain.ProfilePictureStorage;
import com.ugo.todoapp.core.user.domain.User;
import com.ugo.todoapp.security.UserSession;
import com.ugo.todoapp.security.UserSessionRepository;
import com.ugo.todoapp.web.model.UserProfile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RolesAllowed("ROLE_USER")
public class UserRestController {

    private final ProfilePictureChanger pictureChanger;
    private final ProfilePictureStorage pictureStorage;
    private final UserSessionRepository sessionRepository;

    public UserRestController(ProfilePictureChanger pictureChanger, ProfilePictureStorage pictureStorage, UserSessionRepository sessionRepository) {
        this.pictureChanger = pictureChanger;
        this.pictureStorage = pictureStorage;
        this.sessionRepository = sessionRepository;
    }

    @GetMapping("/api/user/profile")
    public UserProfile userProfile(UserSession userSession){
        return new UserProfile(userSession.getUser());
    }

    @PostMapping("/api/user/profile-picture")
    public UserProfile updateUserProfile(MultipartFile profilePicture , UserSession userSession) throws IOException {

        URI profilePictureUri = pictureStorage.save(profilePicture.getResource());

        // 이미지 갱신
        User updatedUser = pictureChanger.change(userSession.getName(), new ProfilePicture(profilePictureUri));

        // 세션 갱신
        sessionRepository.set(new UserSession(updatedUser));
        return new UserProfile(updatedUser);
    }
}
