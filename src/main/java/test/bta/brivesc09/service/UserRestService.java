package test.bta.brivesc09.service;

import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.rest.SiswaDTO;
import test.bta.brivesc09.rest.StaffDTO;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface UserRestService {
   UserModel createUser(UserModel user);
   List<UserModel> getAllUser();
   UserModel getUserByUsername(String username);
   UserModel deleteUser(UserModel user);
   String encrypt(String data);
   UserModel getUserFromJwt(HttpServletRequest request);
   String checkConditions(StaffDTO staff);
   String checkConditions(SiswaDTO pelajar);
}