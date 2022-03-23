package test.bta.brivesc09.service;

import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.security.SecurityConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserRestServiceImpl implements UserRestService {
   @Autowired
   private UserDb userDb;

   @Override
   public UserModel createUser(UserModel user) {
       return userDb.save(user);
   }

   @Override
   public List<UserModel> getAllUser() {
       return userDb.findAll();
   }

   @Override
   public UserModel getUserByUsername(String username) {
       Optional<UserModel> user = userDb.findByUsername(username);
       if (user.isPresent()) {
           return user.get();
       }
       return null;
   }

   @Override
   public UserModel deleteUser(UserModel user) {
       userDb.delete(user);


       return user;
   }

   @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        return hash;
    }

    @Override
    public UserModel getUserFromJwt(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.length() != 0) {
          String user = JWT.require(Algorithm.HMAC512(SecurityConstants.KEY.getBytes()))
              .build()
              .verify(token.replace("Bearer ", ""))
              .getSubject();
          return userDb.findByUsername(user).get();
        }
        return null;
    }

    @Override
    public String checkConditions(StaffDTO staff) {
        String message="";
        // check condition
        // 1) password lebih dari 8 karakter
        if (staff.getPassword().length() < 8) {
            message += "Password harus terdiri atas lebih dari 8 karakter;";
        }
        // 2) username udah terdaftar + nomor pegawai udah terdaftar
        if (getUserByUsername(staff.getUsername()) != null) {
            message += "Username telah terdaftar;";
        }
        return message;
    }
}