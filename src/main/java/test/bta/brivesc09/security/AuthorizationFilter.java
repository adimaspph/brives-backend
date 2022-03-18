package test.bta.brivesc09.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class AuthorizationFilter extends BasicAuthenticationFilter {

  	public AuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws IOException, ServletException {
		String header = request.getHeader("Authorization");

		if (header == null) {
		chain.doFilter(request, response);
		return;
		}

		UsernamePasswordAuthenticationToken authentication = authenticate(request);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(request, response);
	}

	private UsernamePasswordAuthenticationToken authenticate(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if (token != null && token.length() != 0) {
		String user = JWT.require(Algorithm.HMAC512(SecurityConstants.KEY.getBytes()))
			.build()
			.verify(token.replace("Bearer ", ""))
			.getSubject();

		String role = JWT.require(Algorithm.HMAC512(SecurityConstants.KEY.getBytes()))
			.build()
			.verify(token.replace("Bearer ", ""))
			.getClaim("role")
			.asString();

		if (user != null) {
			Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
			grantedAuthorities.add(new SimpleGrantedAuthority(role));
			return new UsernamePasswordAuthenticationToken(user, null, grantedAuthorities);
		}

		return null;
		}
		return null;
	}
}
