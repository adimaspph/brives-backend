package test.bta.brivesc09.restcontroller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import test.bta.brivesc09.model.RoleModel;
import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.payload.request.LoginRequest;
// import test.bta.brivesc09.payload.request.SignupRequest;
import test.bta.brivesc09.payload.response.JwtResponse;
// import test.bta.brivesc09.payload.response.MessageResponse;
// import test.bta.brivesc09.repository.RoleDb;
import test.bta.brivesc09.repository.UserDb;
import test.bta.brivesc09.rest.BaseResponse;
import test.bta.brivesc09.security.jwt.JwtUtils;
import test.bta.brivesc09.service.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthRestController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserDb userDb;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public BaseResponse<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    BaseResponse<JwtResponse> response = new BaseResponse<>();

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    response.setStatus(200);
    response.setMessage("User berhasil login");
    JwtResponse jwtSend = new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(),userDetails.getEmail(), roles);
    response.setResult(jwtSend);
    
    return response;
  }
}