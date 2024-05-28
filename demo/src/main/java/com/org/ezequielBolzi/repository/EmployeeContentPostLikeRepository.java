package com.org.ezequielBolzi.repository;

import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.model.EmployeeContentPostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeContentPostLikeRepository extends JpaRepository<EmployeeContentPostLike, Long> {
    boolean existsByEmployeeAndPost(Employee employee, EmployeeContentPost post);


    // Consulta para eliminar registro relacionado con un empleado y un post, utilizado para unlike.
    @Modifying
    @Query("delete from EmployeeContentPostLike ecp where ecp.employee.id = :employeeId and ecp.post.id = :postId")
    void deleteByEmployeeAndPost(@Param("employeeId") Long employeeId, @Param("postId") Long postId);

}
