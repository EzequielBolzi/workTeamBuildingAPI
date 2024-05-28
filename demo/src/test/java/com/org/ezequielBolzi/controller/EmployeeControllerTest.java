package com.org.ezequielBolzi.controller;

import com.org.ezequielBolzi.controller.EmployeeController;
import com.org.ezequielBolzi.customException.CustomBadRequestException;
import com.org.ezequielBolzi.dtos.EmployeeDTO;
import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.enums.User;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;
import com.org.ezequielBolzi.repository.EmployeeRepository;
import com.org.ezequielBolzi.service.EmployeeService;
import com.org.ezequielBolzi.service.LikeService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private LikeService likeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    private EmployeeContentPostRepository employeeContentPostRepository;

    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    @Test
    void testDeleteEmployee() {
        Authentication authentication = Mockito.mock(Authentication.class);
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setTypeUser(User.ADMIN);
        Mockito.when(authentication.getPrincipal()).thenReturn(employee);
        Employee employeeB = new Employee();
        employeeB.setId(2L);
        employeeB.setTypeUser(User.EMPLOYEE);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Simular llamada al método deleteEmployeeById en el servicio
        when(employeeService.deleteEmployee(employeeB.getId())).thenReturn(true);

        // Llamar al método del controlador para eliminar un empleado
        ResponseEntity<Void> response = employeeController.deleteEmployee(employeeB.getId());

        // Verificar que se llamó al método deleteEmployeeById en el servicio
        verify(employeeService, times(1)).deleteEmployee(employeeB.getId());

        // Verificar que la respuesta del controlador sea la esperada
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteEmployee_UnauthorizedAccess() {
        // Preparar datos de prueba
        Long employeeId = 1L;

        // Simular la autenticación del usuario como empleado
        Authentication authentication = Mockito.mock(Authentication.class);
        Employee employee = new Employee();
        employee.setId(employeeId);
        employee.setTypeUser(User.EMPLOYEE);
        Mockito.when(authentication.getPrincipal()).thenReturn(employee);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Llamar al método del controlador para eliminar un empleado y verificar que lance la excepción
        assertThrows(CustomBadRequestException.class, () -> employeeController.deleteEmployee(employeeId));

        // Verificar que no se llamó al método deleteEmployeeById en el servicio
        verify(employeeService, never()).deleteEmployee(employeeId);
    }

    @Test
    void testGetAllEmployees() {
        List<EmployeeDTO> employees = Arrays.asList(new EmployeeDTO(), new EmployeeDTO());

        // Configurar el mock del servicio para devolver los empleados
        when(employeeService.findAllEmployees()).thenReturn(employees);

        // Llamar al método del controlador
        ResponseEntity<List<EmployeeDTO>> response = employeeController.getAllEmployees();

        // Verificar la respuesta
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(employees, response.getBody());
    }


    // Método para crear objetos de prueba
    private EmployeeContentPostDTO createEmployeeWithMostLikes() {
        Employee employee1 = new Employee();
        employee1.setId(1L);
        EmployeeContentPostDTO employeeWithMostLikes = new EmployeeContentPostDTO();
        employeeWithMostLikes.setName(employee1.getName());
        return employeeWithMostLikes;
    }

    @Test
    void testGetEmployeeWithMostLikes() {
        // Preparar los datos de prueba
        EmployeeContentPostDTO employeeWithMostLikes = createEmployeeWithMostLikes();

        // Configurar el mock del servicio para devolver un empleado específico cuando se llama a findEmployeeWithMostLikes
        when(employeeService.findEmployeeWithMostLikes()).thenReturn(Arrays.asList(employeeWithMostLikes));

        // Llamar al método del controlador
        ResponseEntity<List<EmployeeContentPostDTO>> response = employeeController.getEmployeeWithMostLikes();

        // Verificar la respuesta
        assertEquals(200, response.getStatusCodeValue());
        assertThat(response.getBody()).hasSize(1);
        assertEquals(employeeWithMostLikes, response.getBody().get(0));
    }

}