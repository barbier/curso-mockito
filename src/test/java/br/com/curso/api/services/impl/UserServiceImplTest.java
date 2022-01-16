package br.com.curso.api.services.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.curso.api.domain.User;
import br.com.curso.api.domain.dto.UserDTO;
import br.com.curso.api.repositories.UserRepository;
import br.com.curso.api.services.exceptions.DataIntegrityViolationException;
import br.com.curso.api.services.exceptions.ObjectNotFoundException;

@SpringBootTest
class UserServiceImplTest {
	
	private static final int ID = 1;
	private static final String NAME = "Gabriel";
	private static final String EMAIL = "gabriel@mail.me";
	private static final String PASSWORD = "123";
	private static final int FIRST_INDEX = 0;
	
	private static final String EMAIL_ERROR_MESSAGE = "E-mail já cadastrado no sistema";
	private static final String USER_ERROR_MESSAGE = "Usuário não encontrado";

	@InjectMocks
	private UserServiceImpl service;
	
	@Mock
	private UserRepository repository;
	
	@Mock
	private ModelMapper mapper;

	private User user;
	private UserDTO userDTO;
	private Optional<User> optionalUser;
	
	@BeforeEach
	void setup() {
		MockitoAnnotations.openMocks(this);
		startUser();
	}
	
	@Test
	void whenFindByIdThenReturnAnUserInstance() {
		when(repository.findById(anyInt())).thenReturn(optionalUser);
		
		User response = service.findById(ID);
		
		assertNotNull(response);
		assertEquals(User.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(EMAIL, response.getEmail());
	}
	
	@Test
	void whenFindByIdThenReturnObjectNotFoundException() {
		when(repository.findById(anyInt())).thenThrow(new ObjectNotFoundException(USER_ERROR_MESSAGE));
		
		try {
			service.findById(ID);
		} catch (Exception ex) {
			assertEquals(ObjectNotFoundException.class, ex.getClass());
			assertEquals(USER_ERROR_MESSAGE, ex.getMessage());
		}
	}
	
	@Test
	void whenFindAllThenReturnAListOfUsers() {
		when(repository.findAll()).thenReturn(List.of(user));
		
		List<User> response = service.findAll();
		
		assertNotNull(response);
		assertEquals(1, response.size());
		assertEquals(User.class, response.get(FIRST_INDEX).getClass());
		assertEquals(ID, response.get(FIRST_INDEX).getId());
		assertEquals(NAME, response.get(FIRST_INDEX).getName());
		assertEquals(EMAIL, response.get(FIRST_INDEX).getEmail());
		assertEquals(PASSWORD, response.get(FIRST_INDEX).getPassword());
	}
	
	@Test
	void whenCreateThenReturnSuccess() {
		when(repository.save(any())).thenReturn(user);
		
		User response = service.create(userDTO);
		
		assertNotNull(response);
		assertEquals(User.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(EMAIL, response.getEmail());
		assertEquals(PASSWORD, response.getPassword());
	}
	
	@Test
	void whenCreateThenReturnADataIntegrityViolationException() {
		when(repository.findByEmail(anyString())).thenReturn(optionalUser);
		
		try {
			optionalUser.get().setId(2);
			service.create(userDTO);
		} catch (Exception ex) {
			assertEquals(DataIntegrityViolationException.class, ex.getClass());
			assertEquals(EMAIL_ERROR_MESSAGE, ex.getMessage());
		}
	}
	
	@Test
	void whenUpdateThenReturnSuccess() {
		when(repository.save(any())).thenReturn(user);
		
		User response = service.update(userDTO);
		
		assertNotNull(response);
		assertEquals(User.class, response.getClass());
		assertEquals(ID, response.getId());
		assertEquals(NAME, response.getName());
		assertEquals(EMAIL, response.getEmail());
		assertEquals(PASSWORD, response.getPassword());
	}
	
	@Test
	void whenUpdateThenReturnADataIntegrityViolationException() {
		when(repository.findByEmail(anyString())).thenReturn(optionalUser);
		
		try {
			optionalUser.get().setId(2);
			service.create(userDTO);
		} catch (Exception ex) {
			assertEquals(DataIntegrityViolationException.class, ex.getClass());
			assertEquals(EMAIL_ERROR_MESSAGE, ex.getMessage());
		}
	}
	
	@Test
	void deleteWithSuccess() {
		when(repository.findById(anyInt())).thenReturn(optionalUser);
		doNothing().when(repository).deleteById(anyInt());
		service.delete(ID);
		verify(repository, times(1)).deleteById(anyInt());
	}
	
	@Test
	void deleteWithObjectNotFoundException() {
		when(repository.findById(anyInt()))
			.thenThrow(new ObjectNotFoundException(USER_ERROR_MESSAGE));
		
		try {
			service.delete(ID);
		} catch (Exception ex) {
			assertEquals(ObjectNotFoundException.class, ex.getClass());
			assertEquals(USER_ERROR_MESSAGE, ex.getMessage());
		}
	}
	
	private void startUser() {
		user = new User(ID, NAME, EMAIL, PASSWORD);
		userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
		optionalUser = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
	}

}
