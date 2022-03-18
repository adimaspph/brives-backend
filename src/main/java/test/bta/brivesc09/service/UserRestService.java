package test.bta.brivesc09.service;

import test.bta.brivesc09.model.UserModel;
import java.util.List;

public interface UserRestService {
   UserModel createUser(UserModel user);
   List<UserModel> getAllUser();
   UserModel getUserByUsername(String username);
   void deleteUser(String username);
   String encrypt(String data);
}