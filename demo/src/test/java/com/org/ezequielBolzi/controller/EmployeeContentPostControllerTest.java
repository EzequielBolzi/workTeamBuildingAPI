package com.org.ezequielBolzi.controller;

import com.org.ezequielBolzi.controller.EmployeeContentPostController;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.service.EmployeeContentPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeContentPostControllerTest {
    @Mock
    private EmployeeContentPostService employeeContentPostService;

    @InjectMocks
    private EmployeeContentPostController employeeContentPostController;

    private static Employee employeeSimulated; // Campo estático para almacenar el objeto Employee simulado

    @BeforeEach
    void setUp() {
        // Crear un objeto Employee simulado
        employeeSimulated = new Employee();

        // Simular el SecurityContext y Authentication
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(employeeSimulated);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }


    @Test
    void testDeletePost() {
        Long postId = 1L;
        String expectedMessage = "Posteo eliminado";

        ResponseEntity<String> response = employeeContentPostController.deletePost(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(employeeContentPostService, times(1)).deleteEmployeeContentPost(postId, employeeSimulated.getId());
    }

    @Test
    void testUpdateContent() {
        Long postId = 1L;
        Content postToUpdate = new Content();
        String expectedMessage = "Contenido del posteo modificado";
        Long simulatedEmployeeId = 1L; // Asegúrate de que este ID corresponda al empleado simulado

        // Asegúrate de que employeeSimulated esté correctamente inicializado
        employeeSimulated.setId(simulatedEmployeeId);

        EmployeeContentPost updatedContent = new EmployeeContentPost();

        updatedContent.setLikes(5);
        when(employeeContentPostService.updateEmployeeContentPost(postId, postToUpdate, simulatedEmployeeId)).thenReturn(updatedContent);

        ResponseEntity<String> response = employeeContentPostController.updateContent(postId, postToUpdate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMessage, response.getBody());
        verify(employeeContentPostService, times(1)).updateEmployeeContentPost(postId, postToUpdate, simulatedEmployeeId);
    }


}
