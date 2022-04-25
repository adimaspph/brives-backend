package test.bta.brivesc09.service;

import test.bta.brivesc09.model.JadwalModel;
import test.bta.brivesc09.model.StaffModel;
import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.repository.StaffDb;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.rest.SiswaDTO;
import test.bta.brivesc09.rest.StaffDTO;
import test.bta.brivesc09.security.SecurityConstants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserRestServiceImpl implements UserRestService {
   @Autowired
   private UserDb userDb;

    @Autowired
    private StaffDb staffDb;

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
       StaffModel staff = user.getStaff();
       staff.setListJadwal(null);
       staff.setListMapel(null);
       staff.setLog(null);

       staffDb.delete(staff);

       user.setStaff(null);
       user.setSiswa(null);
       user.setRole(null);
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
        char[] alphabet = staff.getPassword().toCharArray();
        boolean hasCapital = false;
        boolean hasDigit = false;
        boolean hasLetter = false;
        boolean hasMoreThanEight = false;

        if (staff.getPassword().length() >= 8) {
            hasMoreThanEight = true;
        }
        
        
        for (char c : alphabet) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
        }

        if (!(hasMoreThanEight && hasDigit && hasCapital && hasLetter)) {
            message += "Password harus";
            if (hasMoreThanEight) {
                message += ", terdiri atas lebih dari 8 karakter";
            }
    
            if (!hasCapital) {
                message += ", memiliki setidaknya 1 kapital";
            }
    
            if (!hasDigit) {
                message += ", memiliki setidaknya 1 angka";
            }
    
            if (!hasLetter) {
                message += ", memiliki setidaknya 1 huruf";
            }
        }
        
        // 2) username udah terdaftar udah terdaftar
        if (getUserByUsername(staff.getUsername()) != null) {
            if (message!="") {
                message += " dan ";
            }
            message += "Username telah terdaftar";
        }
        return message;
    
    }

    @Override
    public String checkConditions(SiswaDTO pelajar) {
        String message="";
        // check condition
        // 1) password lebih dari 8 karakter
        char[] alphabet = pelajar.getPassword().toCharArray();
        boolean hasCapital = false;
        boolean hasDigit = false;
        boolean hasLetter = false;
        boolean hasMoreThanEight = false;

        if (pelajar.getPassword().length() >= 8) {
            hasMoreThanEight = true;
        }
        
        
        for (char c : alphabet) {
            if (Character.isDigit(c)) {
                hasDigit = true;
            }
            if (Character.isUpperCase(c)) {
                hasCapital = true;
            }
            if (Character.isLetter(c)) {
                hasLetter = true;
            }
        }

        if (!(hasMoreThanEight && hasDigit && hasCapital && hasLetter)) {
            message += "Password harus";
            if (hasMoreThanEight) {
                message += ", terdiri atas lebih dari 8 karakter";
            }
    
            if (!hasCapital) {
                message += ", memiliki setidaknya 1 kapital";
            }
    
            if (!hasDigit) {
                message += ", memiliki setidaknya 1 angka";
            }
    
            if (!hasLetter) {
                message += ", memiliki setidaknya 1 huruf";
            }
        }
        
        // 2) username udah terdaftar udah terdaftar
        if (getUserByUsername(pelajar.getUsername()) != null) {
            if (message!="") {
                message += " dan ";
            }
            message += "Username telah terdaftar";
        }
        return message;
    
    }
}