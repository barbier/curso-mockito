package br.com.curso.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.curso.api.domain.User;
import br.com.curso.api.repositories.UserRepository;

@Configuration
@Profile("local")
public class LocalConfig {
	
	@Autowired
	private UserRepository repository;
	
	@Bean
	public void startDB() {
		User u1 = new User(null, "Gabriel", "gabriel@mail.me", "123");
		User u2 = new User(null, "Biscoito", "biscoito@mail.me", "123");
		
		repository.saveAll(List.of(u1, u2));
	}
}
