package com.org.ezequielBolzi.service;

import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.enums.TypeContent;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;
import com.org.ezequielBolzi.service.EmployeeContentPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class EmployeeContentPostServiceTest {

    @Mock
    private EmployeeContentPostRepository employeeContentPostRepository;

    @InjectMocks
    private EmployeeContentPostService employeeContentPostService;
    private Employee employee;
    private Content content;
    private EmployeeContentPost employeeContentPost;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        employee = new Employee();
        employee.setId(1L);
        employee.setName("Employee Name");

        content = new Content();
        content.setId(1L);
        content.setTitle("Test Content");
        content.setYear(2020);
        content.setDirector("Director Name");
        content.setGenre("Genre");
        content.setDuration("Duration");
        content.setTypeContent(TypeContent.SERIE);

        employeeContentPost = new EmployeeContentPost();
        employeeContentPost.setId(1L);
        employeeContentPost.setEmployee(employee);
        employeeContentPost.setContent(content);
        employeeContentPost.setLikes(0);
    }

    @Test
    void save() {
        when(employeeContentPostRepository.save(any(EmployeeContentPost.class))).thenReturn(employeeContentPost);

        EmployeeContentPost result = employeeContentPostService.save(employeeContentPost);

        assertNotNull(result, "El resultado no puede ser nulo");
        assertEquals(employeeContentPost.getId(), result.getId(), "Los IDs deben coincidir");
        assertEquals(employee.getName(), result.getEmployee().getName(), "Los nombres de los empleados deben coincidir");
        assertEquals(content.getTitle(), result.getContent().getTitle(), "Los títulos de contenido deben coincidir");
        assertEquals(content.getYear(), result.getContent().getYear(), "Los años de contenido deben coincidir");
        assertEquals(content.getDirector(), result.getContent().getDirector(), "Los directores de contenido deben coincidir");
        assertEquals(content.getGenre(), result.getContent().getGenre(), "Los géneros de contenido deben coincidir");
        assertEquals(content.getDuration(), result.getContent().getDuration(), "Las duraciones de contenido deben coincidir");
    }

    @Test
    void existsByEmployee() {
        when(employeeContentPostRepository.existsByEmployee(any(Employee.class))).thenReturn(true);

        boolean result = employeeContentPostService.existsByEmployee(employee);

        assertTrue(result, "Debe existir el empleado");
    }

    @Test
    void updateEmployeeContentPost() {
        Content updatedContent = new Content();
        updatedContent.setId(1L);
        updatedContent.setTitle("Updated Title");
        updatedContent.setYear(2021);
        updatedContent.setDirector("Updated Director");
        updatedContent.setGenre("Updated Genre");
        updatedContent.setDuration("Updated Duration");
        updatedContent.setTypeContent(TypeContent.SERIE);

        when(employeeContentPostRepository.findById(anyLong())).thenReturn(Optional.ofNullable(employeeContentPost));
        when(employeeContentPostRepository.save(any(EmployeeContentPost.class))).thenReturn(employeeContentPost);

        EmployeeContentPost result = employeeContentPostService.updateEmployeeContentPost(1L, updatedContent, 1L);

        assertEquals(updatedContent.getTitle(), result.getContent().getTitle(), "Los títulos de contenido deben coincidir");
        assertEquals(updatedContent.getYear(), result.getContent().getYear(), "Los años de contenido deben coincidir");
        assertEquals(updatedContent.getDirector(), result.getContent().getDirector(), "Los directores de contenido deben coincidir");
        assertEquals(updatedContent.getGenre(), result.getContent().getGenre(), "Los géneros de contenido deben coincidir");
        assertEquals(updatedContent.getDuration(), result.getContent().getDuration(), "Las duraciones de contenido deben coincidir");
    }

    @Test
    void getContentsWithMinimumLikes() {
        List<EmployeeContentPostDTO> contents = Arrays.asList(new EmployeeContentPostDTO(), new EmployeeContentPostDTO());
        when(employeeContentPostRepository.findContentsWithMinimumLikes(1)).thenReturn(contents);

        List<EmployeeContentPostDTO> result = employeeContentPostService.getContentsWithMinimumLikes(1);

        assertEquals(contents.size(), result.size(), "El tamaño de la lista debe coincidir");
    }

    @Test
    void getContentsWithLessThanMinimumLikes() {
        List<EmployeeContentPostDTO> contents = Arrays.asList(new EmployeeContentPostDTO(), new EmployeeContentPostDTO());
        when(employeeContentPostRepository.findContentsWithLessThanMinimumLikes(1)).thenReturn(contents);

        List<EmployeeContentPostDTO> result = employeeContentPostService.getContentsWithLessThanMinimumLikes(1);

        assertEquals(contents.size(), result.size(), "El tamaño de la lista debe coincidir");
    }
}
