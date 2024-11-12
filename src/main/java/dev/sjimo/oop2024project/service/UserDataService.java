package dev.sjimo.oop2024project.service;

import dev.sjimo.oop2024project.repository.UserDataRepository;
import dev.sjimo.oop2024project.request.UserDataRequest;
import dev.sjimo.oop2024project.utils.ErrorCode;
import dev.sjimo.oop2024project.utils.ResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserDataService {

    @Autowired
    private UserDataRepository userDataRepository;

    public UserDataRequest getUserData(Long userId) {
        var userData = userDataRepository.findByUser_Id(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));

        return new UserDataRequest(userData.getUsername(),userData.getGender(),userData.getGravatarEmail(),userData.getDescription());
    }
    public void setUserData(Long userId, UserDataRequest userDataRequest) {
        var userData = userDataRepository.findByUser_Id(userId).orElseThrow(() -> new ResponseException(ErrorCode.USER_NOT_EXIST));
        if (userDataRequest.getUsername()!=null) {
            userData.setUsername(userDataRequest.getUsername());
        }
        if (userDataRequest.getGender()!=null) {
            userData.setGender(userDataRequest.getGender());
        }
        if (userDataRequest.getGravatarEmail()!=null) {
            userData.setGravatarEmail(userDataRequest.getGravatarEmail());
        }
        if (userDataRequest.getDescription()!=null) {
            userData.setDescription(userDataRequest.getDescription());
        }
        userDataRepository.save(userData);
    }

}
