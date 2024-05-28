package com.org.ezequielBolzi.controller;

import com.org.ezequielBolzi.customException.CustomBadRequestException;
import com.org.ezequielBolzi.customException.CustomEmployeeNoteDeletableException;
import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.dtos.EmployeeDTO;
import com.org.ezequielBolzi.dtos.LikeDTO;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.service.EmployeeService;
import com.org.ezequielBolzi.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {
    @Autowired
    private final EmployeeService employeeService;
    @Autowired
    private LikeService likeService;

    public EmployeeController(EmployeeService employeeService, LikeService likeService) {
        this.employeeService = employeeService;
        this.likeService = likeService;
    }

    @Operation(summary = "Obtener todos los empleados", description = "Recupera una lista de todos los empleados.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> employees = employeeService.findAllEmployees();
        return ResponseEntity.ok(employees);
    }

    @Operation(summary = "Obtener el empleado con más likes", description = "Encuentra y devuelve el empleado con más likes.", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/most-likes")
    public ResponseEntity<List<EmployeeContentPostDTO>> getEmployeeWithMostLikes() {
        List<EmployeeContentPostDTO> employeeWithMostLikes = employeeService.findEmployeeWithMostLikes();
        return ResponseEntity.ok(employeeWithMostLikes);
    }

    @Operation(summary = "Dar like a un post", description = "Añade un like a un post específico.", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/like/{postId}")
    public ResponseEntity<LikeDTO> likePost(@PathVariable Long postId) {
        // Obtenemos la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Extraemos el empleado del objeto principal de autenticación
        Employee employee = (Employee) authentication.getPrincipal();

        LikeDTO likeDTO = likeService.likePost(postId, employee.getId());
        return ResponseEntity.ok(likeDTO);
    }

    @Operation(summary = "Quitar like de un post", description = "Retira un like de un post específico.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/unlike/{postId}")
    public ResponseEntity<LikeDTO> unLikePost(@PathVariable Long postId) {
        // Obtenemos la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Extraemos el empleado del objeto principal de autenticación
        Employee employee = (Employee) authentication.getPrincipal();

        LikeDTO likeDTO = likeService.unLikePost(postId, employee.getId());
        return ResponseEntity.ok(likeDTO);
    }

    @Operation(summary = "Eliminar un empleado", description = "Elimina un empleado específico basado en su ID. Siempre y cuando sea un usuario tipo ADMIN", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id){
        // Obtenemos la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Extraemos el empleado del objeto principal de autenticación
        Employee employee = (Employee) authentication.getPrincipal();
        if("EMPLOYEE".equalsIgnoreCase(String.valueOf(employee.getTypeUser()))){
            throw new CustomBadRequestException("Un usuario tipo empleado no tiene los permisos para eliminar un empleado");
        } else if (employee.getId().equals(id)) {
            throw new CustomEmployeeNoteDeletableException("No se puede eliminar un usuario asimismo"); //Agrego esta excepcion
        } else {
            boolean deleted = employeeService.deleteEmployee(id);
            if (deleted){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.notFound().build();
        }
    }
}
