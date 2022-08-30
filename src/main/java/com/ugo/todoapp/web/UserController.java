package com.ugo.todoapp.web;

import com.ugo.todoapp.core.user.domain.ProfilePictureStorage;
import com.ugo.todoapp.security.UserSession;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.security.RolesAllowed;
import java.io.IOException;
import java.net.URI;

@Controller
public class UserController {

    private final ProfilePictureStorage pictureStorage;

    public UserController(ProfilePictureStorage pictureStorage) {
        this.pictureStorage = pictureStorage;
    }

    @RequestMapping("/user/profile-picture")
    @RolesAllowed("ROLE_USER")
    public  @ResponseBody Resource profilePicture(UserSession userSession) throws IOException {
        return pictureStorage.load(userSession.getUser().getProfilePicture().getUri());

    }

    // 반환되는 데이터를 원하는 처리를 거친 후에 반환할 수 있다.
//    public static class ProfilePictureReturnValueHandler implements HandlerMethodReturnValueHandler{
//
//        private final ProfilePictureStorage pictureStorage;
//
//        public ProfilePictureReturnValueHandler(ProfilePictureStorage pictureStorage) {
//            this.pictureStorage = pictureStorage;
//        }
//
//        @Override
//        public boolean supportsReturnType(MethodParameter returnType) {
//            return ProfilePicture.class.isAssignableFrom(returnType.getParameterType());
//        }
//
//        @Override
//        public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
//            HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
//            //returnValue 는 핸들러가 리턴한 데이터를 의미한다.
//            //supportsReturnType이  ProfilePicture으로 설정되 있으니  returnValue는 ProfilePicture으로 캐스팅 가능하다
//            Resource profilePicture = pictureStorage.load(((ProfilePicture) returnValue).getUri());
//            //인풋스트림을 가져와 response에 옮겨준다.
//            profilePicture.getInputStream().transferTo(response.getOutputStream());
//
//        }
//    }
}
