package com.org.ezequielBolzi.service;

import com.org.ezequielBolzi.dtos.EmployeeDTO;
import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.enums.Role;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;
import com.org.ezequielBolzi.repository.EmployeeRepository;
import com.org.ezequielBolzi.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeContentPostRepository employeeContentPostRepository;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void findAllEmployees() {
        // Preparar datos de prueba
        Employee employee1 = new Employee();
        employee1.setName("John Doe");
        employee1.setEmail("john.doe@example.com");
        employee1.setRole(Role.BACKEND);
        employee1.setEdad(30);

        Employee employee2 = new Employee();
        employee2.setName("Jane Doe");
        employee2.setEmail("jane.doe@example.com");
        employee2.setRole(Role.BACKEND);
        employee2.setEdad(35);

        List<Employee> employees = Arrays.asList(employee1, employee2);

        // Simular el comportamiento del repositorio
        when(employeeRepository.findAll()).thenReturn(employees);

        // Llamar al método bajo prueba
        List<EmployeeDTO> result = employeeService.findAllEmployees();

        // Verificar el resultado
        assertEquals(2, result.size(), "La lista de empleados debe contener 2 elementos");
        assertEquals("John Doe", result.get(0).getName(), "El nombre del primer empleado debe ser John Doe");
        assertEquals("Jane Doe", result.get(1).getName(), "El nombre del segundo empleado debe ser Jane Doe");
    }

    @Test
    void findEmployeeWithMostLikes() {
        // Preparar datos de prueba
        EmployeeContentPostDTO post = new EmployeeContentPostDTO();
        post.setTitle("Post Title");
        post.setLikes(100); // Asumiendo que existe un método setter para likes

        // Simular el comportamiento del repositorio
        when(employeeContentPostRepository.findEmployeeWithMostLikesLimitedToOne()).thenReturn(Arrays.asList(post));

        // Llamar al método bajo prueba
        List<EmployeeContentPostDTO> result = employeeService.findEmployeeWithMostLikes();

        // Verificar el resultado
        assertEquals(1, result.size(), "La lista de posts debe contener 1 elemento");
        assertEquals("Post Title", result.get(0).getTitle(), "El título del post debe ser Post Title");
        assertEquals(100, result.get(0).getLikes(), "El número de likes del post debe ser 100");
    }
}
