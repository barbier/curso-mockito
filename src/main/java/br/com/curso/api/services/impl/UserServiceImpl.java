package br.com.curso.api.services.impl;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.curso.api.domain.User;
import br.com.curso.api.domain.dto.UserDTO;
import br.com.curso.api.repositories.UserRepository;
import br.com.curso.api.services.UserService;
import br.com.curso.api.services.exceptions.DataIntegrityViolationException;
import br.com.curso.api.services.exceptions.ObjectNotFoundException;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository repository;
	
	@Autowired
	private ModelMapper mapper;
	
	@Override
	public User findById(Integer id) {
		Optional<User> obj = repository.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException("Usuário não encontrado"));
	}
	
	public List<User> findAll() {
		return repository.findAll();
	}
	
	@Override
	public User create(UserDTO obj) {
		findByEmail(obj);
		return repository.save(mapper.map(obj, User.class));
	}
	
	private void findByEmail(UserDTO obj) {
		Optional<User> user = repository.findByEmail(obj.getEmail());
		if (user.isPresent() && !user.get().getId().equals(obj.getId())) {
			throw new DataIntegrityViolationException("E-mail já cadastrado no sistema");
		}
	}

	@Override
	public User update(UserDTO obj) {
		findByEmail(obj);
		return repository.save(mapper.map(obj, User.class));
	}

	@Override
	public void delete(Integer id) {
		findById(id);
		repository.deleteById(id);
	}

}
