package br.com.andersondepaiva.auth.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.andersondepaiva.core.infra.security.AuthService;

@Service
public class AuthServicePasswordEncoder extends AuthService implements PasswordEncoder {

}
