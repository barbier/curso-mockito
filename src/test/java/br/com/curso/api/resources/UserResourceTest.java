package br.com.curso.api.resources;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.curso.api.domain.User;
import br.com.curso.api.domain.dto.UserDTO;
import br.com.curso.api.services.impl.UserServiceImpl;

@SpringBootTest
class UserResourceTest {
	
	private static final int ID = 1;
	private static final String NAME = "Gabriel";
	private static final String EMAIL = "gabriel@mail.me";
	private static final String PASSWORD = "123";
	private static final int FIRST_INDEX = 0;
	
	@InjectMocks
	private UserResource resource;
	
	@Mock
	private UserServiceImpl service;
	
	@Mock
	private ModelMapper mapper;
	
	private User user;
	private UserDTO userDTO = new UserDTO();

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		
		startUser();
	}

	@Test
	void whenFindByIdThenReturnSuccess() {
		when(service.findById(anyInt())).thenReturn(user);
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		ResponseEntity<UserDTO> response = resource.findById(ID);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(UserDTO.class, response.getBody().getClass());
		
		assertEquals(ID, response.getBody().getId());
		assertEquals(NAME, response.getBody().getName());
		assertEquals(EMAIL, response.getBody().getEmail());
		assertEquals(PASSWORD, response.getBody().getPassword());
	}
	
	@Test
	void whenFindAllThenReturnAListaOfUserDTO() {
		when(service.findAll()).thenReturn(List.of(user));
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		ResponseEntity<List<UserDTO>> response = resource.findAll();
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(ArrayList.class, response.getBody().getClass());
		assertEquals(UserDTO.class, response.getBody().get(FIRST_INDEX).getClass());
		
		assertEquals(ID, response.getBody().get(FIRST_INDEX).getId());
		assertEquals(NAME, response.getBody().get(FIRST_INDEX).getName());
		assertEquals(EMAIL, response.getBody().get(FIRST_INDEX).getEmail());
		assertEquals(PASSWORD, response.getBody().get(FIRST_INDEX).getPassword());
	}
	
	@Test
	void whenCreateThenReturnStatusCreated() {
		when(service.create(any())).thenReturn(user);
		
		ResponseEntity<UserDTO> response = resource.create(userDTO);
		
		assertNotNull(response.getHeaders().get("Location"));
		assertEquals(HttpStatus.CREATED, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
	}
	
	@Test
	void whenUpdateThenReturnSuccess() {
		when(service.update(userDTO)).thenReturn(user);
		when(mapper.map(any(), any())).thenReturn(userDTO);
		
		ResponseEntity<UserDTO> response = resource.update(ID, userDTO);
		
		assertNotNull(response);
		assertNotNull(response.getBody());
		
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(UserDTO.class, response.getBody().getClass());
		
		assertEquals(ID, response.getBody().getId());
		assertEquals(NAME, response.getBody().getName());
		assertEquals(EMAIL, response.getBody().getEmail());
	}
	
	@Test
	void whenDeleteThenReturnSuccess() {
		doNothing().when(service).delete(anyInt());
		
		ResponseEntity<UserDTO> response = resource.delete(ID);
		
		assertNotNull(response);
		assertEquals(ResponseEntity.class, response.getClass());
		verify(service, times(1)).delete(anyInt());
		assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	}
	
	private void startUser() {
		user = new User(ID, NAME, EMAIL, PASSWORD);
		userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
	}

}
