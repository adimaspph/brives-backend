package test.bta.brivesc09.service;

import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.repository.UserDb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
   public void deleteUser(String username) {
       UserModel User = getUserByUsername(username);
       userDb.delete(User);
   }

   @Override
    public String encrypt(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode(password);
        return hash;
    }
}