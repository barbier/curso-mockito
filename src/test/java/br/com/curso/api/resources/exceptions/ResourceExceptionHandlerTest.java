package br.com.curso.api.resources.exceptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import br.com.curso.api.services.exceptions.DataIntegrityViolationException;
import br.com.curso.api.services.exceptions.ObjectNotFoundException;

@SpringBootTest
class ResourceExceptionHandlerTest {
	
	private static final String EMAIL_ERROR_MESSAGE = "E-mail já cadastrado no sistema";
	private static final String USER_ERROR_MESSAGE = "Usuário não encontrado";
	@InjectMocks
	private ResourceExceptionHandler exceptionHandler;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
	

	@Test
	void whenObjectNotFoundExceptionThenReturnAResponseEntity() {
		ResponseEntity<StandardError> response = 
			exceptionHandler.objectNotFound(
				new ObjectNotFoundException(USER_ERROR_MESSAGE),
				new MockHttpServletRequest()
			);
		assertNotNull(response);
		assertNotNull(response.getBody());
		
		assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
		assertEquals(ResponseEntity.class, response.getClass());
		assertEquals(StandardError.class, response.getBody().getClass());
		assertEquals(USER_ERROR_MESSAGE, response.getBody().getError());
		assertEquals(404, response.getBody().getStatus());
		assertNotEquals("/user/2", response.getBody().getPath());
		assertNotEquals(LocalDateTime.now(), response.getBody().getTimestamp());
	}
	
	@Test
	void dataIntegrityViolationException() {
		ResponseEntity<StandardError> response = 
				exceptionHandler.dataIntegrityViolation(
					new DataIntegrityViolationException(EMAIL_ERROR_MESSAGE),
					new MockHttpServletRequest()
				);
			assertNotNull(response);
			assertNotNull(response.getBody());
			
			assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
			assertEquals(ResponseEntity.class, response.getClass());
			assertEquals(StandardError.class, response.getBody().getClass());
			assertEquals(EMAIL_ERROR_MESSAGE, response.getBody().getError());
			assertEquals(400, response.getBody().getStatus());
	}

}
