package com.org.ezequielBolzi.controller;

import com.org.ezequielBolzi.controller.ContentController;
import com.org.ezequielBolzi.dtos.ContentDTO;
import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.enums.Role;
import com.org.ezequielBolzi.enums.User;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.repository.ContentRepository;
import com.org.ezequielBolzi.service.ContentService;
import com.org.ezequielBolzi.service.EmployeeContentPostService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContentControllerTest {
    @Mock
    private ContentRepository contentRepository;
    @Mock
    private ContentService contentService;
    @Mock
    private EmployeeContentPostService employeeContentPostService;

    private ContentController contentController;

    @BeforeEach
    void setUp() {
        contentController = new ContentController(contentRepository, contentService, employeeContentPostService);
    }

    // Campo estático para almacenar el objeto Employee simulado
    private static Employee employeeSimulated;

    @BeforeAll
    static void setupEmployee() {
        employeeSimulated = Employee.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .role(Role.BACKEND)
                .edad(30)
                .password("password123")
                .typeUser(User.EMPLOYEE)
                .build();
    }

    @BeforeEach
    void resetSecurityContext() {
        // Limpiar el SecurityContext antes de cada prueba
        SecurityContextHolder.clearContext();
    }

    @Test
    void testGetAllContents() {
        List<ContentDTO> expectedContents = Arrays.asList(new ContentDTO(), new ContentDTO());
        when(contentService.getAllContentsAsDTOs()).thenReturn(expectedContents);

        List<ContentDTO> response = contentController.getAllContents();
        assertEquals(expectedContents, response);
    }
    @Test
    void testGetContentsByGenre() {
        List<ContentDTO> expectedContents = Arrays.asList(new ContentDTO(), new ContentDTO());
        when(contentService.filterContentsByGenre("genre")).thenReturn(expectedContents);

        List<ContentDTO> response = contentController.filterContentGenre("genre");
        assertEquals(expectedContents, response);
    }

    @Test
    void testGetContentsByYear() {
        List<ContentDTO> expectedContents = Arrays.asList(new ContentDTO(), new ContentDTO());
        when(contentService.filterContentsByYear(2022)).thenReturn(expectedContents);

        List<ContentDTO> response = contentController.filterContentYear(2022);
        assertEquals(expectedContents, response);
    }

    @Test
    void testGetContentsWithMinimumLikes() {
        List<EmployeeContentPostDTO> expectedContents = Arrays.asList(new EmployeeContentPostDTO(), new EmployeeContentPostDTO());
        when(contentService.getContentsWithMinimumLikes(10)).thenReturn(expectedContents);

        List<EmployeeContentPostDTO> response = contentController.getContentsWithMinimumLikes(10);
        assertEquals(expectedContents, response);
    }

    @Test
    void testGetContentsWithLessThanMinimumLikes() {
        List<EmployeeContentPostDTO> expectedContents = Arrays.asList(new EmployeeContentPostDTO(), new EmployeeContentPostDTO());
        when(contentService.getContentsWithLessThanMinimumLikes(10)).thenReturn(expectedContents);

        List<EmployeeContentPostDTO> response = contentController.getContentsWithLessThanMinimumLikes(10);
        assertEquals(expectedContents, response);
    }

    @Test
    void testGetAllMovies() {
        List<ContentDTO> expectedContents = Arrays.asList(new ContentDTO(), new ContentDTO());
        when(contentService.getAllMoviesAsDTOs()).thenReturn(expectedContents);

        List<ContentDTO> response = contentController.getAllMovies();
        assertEquals(expectedContents, response);
    }


    @Test
    void testCreateMovie() {
        // Crear objetos DTO y mockear servicios
        ContentDTO expectedContent = new ContentDTO();
        when(contentService.createMovie(any(Content.class), any(Employee.class))).thenReturn(expectedContent);

        // Mockear SecurityContext y Authentication
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(employeeSimulated);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        // Configurar el SecurityContextHolder para usar el SecurityContext mockeado
        SecurityContextHolder.setContext(securityContext);

        // Ejecutar la prueba
        ContentDTO response = contentController.createMovie(new Content());

        // Verificar resultados
        assertEquals(expectedContent, response);
    }

    @Test
    void testGetAllSeries() {
        List<ContentDTO> expectedContents = Arrays.asList(new ContentDTO(), new ContentDTO());
        when(contentService.getAllSeriesAsDTOs()).thenReturn(expectedContents);

        List<ContentDTO> response = contentController.getAllSeries();
        assertEquals(expectedContents, response);
    }

    // Test createSerie()
    @Test
    void testCreateSerie() {
        // Preparar el objeto esperado
        ContentDTO expectedContent = new ContentDTO();

        // Mockear el comportamiento del service
        when(contentService.createSerie(any(Content.class), any(Employee.class))).thenReturn(expectedContent);

        // Limpiar el SecurityContext antes de la prueba
        SecurityContextHolder.clearContext();

        // Configurar el SecurityContext con el objeto Employee simulado
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(employeeSimulated); // Reutiliza el objeto Employee simulado
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Ejecutar la prueba
        ContentDTO response = contentController.createSerie(new Content());

        // Verificar los resultados
        assertEquals(expectedContent, response);
    }

}