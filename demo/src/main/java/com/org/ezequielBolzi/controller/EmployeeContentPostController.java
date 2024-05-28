package com.org.ezequielBolzi.controller;

import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.service.ContentService;
import com.org.ezequielBolzi.service.EmployeeContentPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/posts")
public class EmployeeContentPostController {

    @Autowired
    private final EmployeeContentPostService employeeContentPostService;



    public EmployeeContentPostController(EmployeeContentPostService employeeContentPostService) {
        this.employeeContentPostService = employeeContentPostService;
    }

    @Operation(summary = "Eliminar un posteo existente", description = "Elimina un posteo existente basado en su ID.", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable Long postId) {
        try {
            // Obtenemos la autenticación actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Extraemos el empleado del objeto principal de autenticación
            Employee authenticatedEmployee = (Employee) authentication.getPrincipal();

            employeeContentPostService.deleteEmployeeContentPost(postId, authenticatedEmployee.getId());
            return ResponseEntity.ok("Posteo eliminado");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getReason());
        }
    }
    @Operation(summary = "Actualizar un posteo existente", description = "Actualiza un posteo existente basado en su ID y los datos proporcionados.", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update/{postId}")
    public ResponseEntity<String> updateContent(@PathVariable Long postId, @RequestBody Content updatedContent) {
        try {
            // Obtenemos la autenticación actual
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            // Extraemos el empleado del objeto principal de autenticación
            Employee authenticatedEmployee = (Employee) authentication.getPrincipal();

            EmployeeContentPost post = employeeContentPostService.updateEmployeeContentPost(postId, updatedContent, authenticatedEmployee.getId());
            return ResponseEntity.ok("Contenido del posteo modificado");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatus()).body(e.getReason());
        }
    }


}

