package com.org.ezequielBolzi.repository;

import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Repository
public interface EmployeeContentPostRepository extends JpaRepository<EmployeeContentPost, Long> {

    // Consulta para obtener los empleados con mas likes, ordenados de manera descendente y utiliza JOIN para conectar las entidades
    // EmployeeContentPost, Employee y Content
    @Query("SELECT new com.cleverpy.ezequielBolzi.dtos.EmployeeContentPostDTO(e.name, e.email, c.title, c.typeContent, ecp.likes) " +
            "FROM EmployeeContentPost ecp " +
            "JOIN ecp.employee e " +
            "JOIN ecp.content c " +
            "ORDER BY ecp.likes DESC")
    List<EmployeeContentPostDTO> findEmployeeWithMostLikes();

    // Metodo para devolver el primer elemento de la lista resultante.
    default List<EmployeeContentPostDTO> findEmployeeWithMostLikesLimitedToOne() {
        List<EmployeeContentPostDTO> result = findEmployeeWithMostLikes();
        if (!result.isEmpty()) {
            return Collections.singletonList(result.get(0));
        }
        return Collections.emptyList();
    }

    // Consulta para contabilizar los post asociados a un empleado.

    @Query("SELECT COUNT(e) FROM EmployeeContentPost e WHERE e.employee = :employee")
    int countByEmployee(@Param("employee") Employee employee);

    // Metodo para verificar si existe al menos un post asociado a un empleado.
    default boolean existsByEmployee(Employee employee) {
        return countByEmployee(employee) > 0;
    }

    // Consulta para obtener el numero de likes de un empleado
      @Query("SELECT COUNT(e) FROM EmployeeContentPost e WHERE e.likedByEmployee = :employee")
    int countLikesByEmployee(@Param("employee") Employee employee);

    // Consulta para seleccionar los posts con un numero de likes mayor al minimo especificado a traves de minLikes.
    @Query("SELECT new com.cleverpy.ezequielBolzi.dtos.EmployeeContentPostDTO(e.name, e.email, c.title, c.typeContent, ecp.likes) " +
            "FROM EmployeeContentPost ecp " +
            "JOIN ecp.employee e " +
            "JOIN ecp.content c " +
            "WHERE ecp.likes >= :minLikes")
    List<EmployeeContentPostDTO> findContentsWithMinimumLikes(@Param("minLikes") int minLikes);

    // Consulta para seleccionar los posts con un numero de likes menor al minimo especificado a traves de minLikes.
    @Query("SELECT new com.cleverpy.ezequielBolzi.dtos.EmployeeContentPostDTO(e.name, e.email, c.title, c.typeContent, ecp.likes) " +
            "FROM EmployeeContentPost ecp " +
            "JOIN ecp.employee e " +
            "JOIN ecp.content c " +
            "WHERE ecp.likes < :minLikes")
    List<EmployeeContentPostDTO> findContentsWithLessThanMinimumLikes(@Param("minLikes") int minLikes);


    Optional<EmployeeContentPost> findOneByEmployee_Id(Long employeeId);
}

