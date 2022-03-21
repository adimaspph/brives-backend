package test.bta.brivesc09.service;

import test.bta.brivesc09.model.UserModel;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface UserRestService {
   UserModel createUser(UserModel user);
   List<UserModel> getAllUser();
   UserModel getUserByUsername(String username);
   void deleteUser(String username);
   String encrypt(String data);
   UserModel getUserFromJwt(HttpServletRequest request);
}