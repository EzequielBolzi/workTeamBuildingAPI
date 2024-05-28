package com.org.ezequielBolzi.repository;

import com.org.ezequielBolzi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(Long id);

    Optional<Employee> findByEmail(String email);
    List<Employee> findAll();

}
