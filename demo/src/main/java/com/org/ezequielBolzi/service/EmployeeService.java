
package com.org.ezequielBolzi.service;


import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.dtos.EmployeeDTO;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;
import com.org.ezequielBolzi.repository.EmployeeRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EmployeeService {

    @Autowired
    private final EmployeeRepository employeeRepository;
    @Autowired
    private final EmployeeContentPostRepository employeeContentPostRepository;
    @Autowired
    private EntityManager entityManager;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeContentPostRepository employeeContentPostRepository) {
        this.employeeRepository = employeeRepository;
        this.employeeContentPostRepository = employeeContentPostRepository;
    }

    // Obtiene una lista de todos los empleados y los convierte en DTOs (Data Transfer Objects) para su uso.
    public List<EmployeeDTO> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> {
                    EmployeeDTO employeeDto = new EmployeeDTO();
                    employeeDto.setId(employee.getId());
                    employeeDto.setName(employee.getName());
                    employeeDto.setEmail(employee.getEmail());
                    employeeDto.setRole(employee.getRole());
                    employeeDto.setEdad(employee.getEdad());
                    return employeeDto;
                })
                .collect(Collectors.toList());
    }

    // Obtiene el empleado con el mayor número de likes en sus posts, limitado a un solo resultado.
    public List<EmployeeContentPostDTO> findEmployeeWithMostLikes() {
        return employeeContentPostRepository.findEmployeeWithMostLikesLimitedToOne();
    }

    @Transactional
    public boolean deleteEmployee(Long employeeId) { //Modifico metodo para eliminar empleado
        Optional<Employee> optionalEmployee = employeeRepository.findById(employeeId);
        if (optionalEmployee.isPresent()) {
            Employee employee = optionalEmployee.get();
            // Elimina el EmployeeContentPost asociado
            Optional<EmployeeContentPost> post = employeeContentPostRepository.findOneByEmployee_Id(employee.getId());
            if (post.isPresent()) {
                employeeContentPostRepository.deleteById(post.get().getId());
            }

            // Procede a eliminar el empleado
            entityManager.remove(employee);
            return true;
        }
        return false;
    }


}

