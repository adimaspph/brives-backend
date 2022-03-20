package test.bta.brivesc09.security;

import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.repository.UserDb;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  private UserDb userDb;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

    Optional<UserModel> userWrap = userDb.findByUsername(username);
    if (userWrap.isPresent()) {
      UserModel user = userWrap.get();
      Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
      grantedAuthorities.add(new SimpleGrantedAuthority(user.getRole().getNamaRole()));

      return new User(user.getUsername(), user.getPassword(), grantedAuthorities);

    } else{
      throw new UsernameNotFoundException(username);
    }

  }

}