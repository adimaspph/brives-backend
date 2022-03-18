package test.bta.brivesc09.security;


import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import test.bta.brivesc09.model.UserModel;
import test.bta.brivesc09.repository.UserDb;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    UserDb userDb;

    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
      
    }
    public AuthenticationFilter(AuthenticationManager authenticationManager, ApplicationContext ctx) {
      	this.authenticationManager = authenticationManager;
     	 this.userDb = (UserDb) ctx.getBean(UserDb.class);
      	setFilterProcessesUrl("/api/user/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		try {
			UserModel temp = new ObjectMapper().readValue(req.getInputStream(), UserModel.class);
			Optional<UserModel> optUser = userDb.findByUsername(temp.getUsername());

			UserModel applicationUser = optUser.get();
			applicationUser.setPassword(temp.getPassword());
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
			grantedAuthorities.add(new SimpleGrantedAuthority(applicationUser.getRole().getNamaRole()));

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(applicationUser.getUsername(),
				applicationUser.getPassword(), grantedAuthorities));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
		Authentication auth) throws IOException {
        User user = ((User) auth.getPrincipal());
        UserModel User = userDb.findByUsername(user.getUsername()).get();
        String token = JWT.create()
            .withClaim("sub", User.getUsername())
            .withClaim("id", User.getIdUser())
            .withClaim("role", User.getRole().getNamaRole())
            .withClaim("nama", User.getNamaLengkap())
            .sign(Algorithm.HMAC512(SecurityConstants.KEY.getBytes()));
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("token", token);
        data.put("role", user.getAuthorities());
        String body = new ObjectMapper().writeValueAsString(data);

        res.getWriter().write(body);
        res.getWriter().flush();
    }
}

