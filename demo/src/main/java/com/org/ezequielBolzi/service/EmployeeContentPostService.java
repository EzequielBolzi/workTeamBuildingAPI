package com.org.ezequielBolzi.service;

import com.org.ezequielBolzi.customException.CustomBadRequestException;
import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class EmployeeContentPostService {

    @Autowired
    private final EmployeeContentPostRepository employeeContentPostRepository;
    @Autowired
    private EntityManager entityManager;


    // Constructor Injection
    public EmployeeContentPostService(EmployeeContentPostRepository employeeContentPostRepository) {
        this.employeeContentPostRepository = employeeContentPostRepository;
    }

    @Transactional
    // Guarda un nuevo EmployeeContentPost en la base de datos, lanzando una excepción si ya existe uno para el mismo empleado.
    public EmployeeContentPost save(EmployeeContentPost post) {
        if (existsByEmployee(post.getEmployee())) {
            throw new CustomBadRequestException("Un empleado no puede crear más de un post");
        }
        return employeeContentPostRepository.save(post);
    }

    // Verifica si ya existe un EmployeeContentPost para el empleado especificado.
    public boolean existsByEmployee(Employee employee) {
        return employeeContentPostRepository.existsByEmployee(employee);
    }

    @Transactional
    // Actualiza un EmployeeContentPost existente con nuevos datos de contenido, lanzando excepciones si el post no se encuentra o si el empleado autenticado no tiene permisos.
    public EmployeeContentPost updateEmployeeContentPost(Long postId, Content updatedContent, Long authenticatedEmployeeId) {
        EmployeeContentPost post = employeeContentPostRepository.findById(postId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Posteo no encontrado"));

        if (!post.getEmployee().getId().equals(authenticatedEmployeeId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para modificar este post");
        }

        post.getContent().setTitle(updatedContent.getTitle());
        post.getContent().setYear(updatedContent.getYear());
        post.getContent().setDirector(updatedContent.getDirector());
        post.getContent().setGenre(updatedContent.getGenre());
        post.getContent().setDuration(updatedContent.getDuration());
        post.getContent().setTypeContent(updatedContent.getTypeContent());

        return employeeContentPostRepository.save(post);
    }

    @Transactional
    public void deleteEmployeeContentPost(Long postId, Long authenticatedEmployeeId) {
        // Busca el posteo en la base de datos utilizando EntityManager
        EmployeeContentPost post = entityManager.find(EmployeeContentPost.class, postId);

        if (post == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Posteo no encontrado");
        }

        // Verifica si el empleado autenticado es el propietario del posteo
        if (!post.getEmployee().getId().equals(authenticatedEmployeeId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene permisos para eliminar este post");
        }

        // Elimina el posteo utilizando EntityManager
        entityManager.remove(post);
    }


    // Obtiene una lista de DTOS de Posts con un cantidad de likes mayorIgual a la de minLikes.
    public List<EmployeeContentPostDTO> getContentsWithMinimumLikes(int minLikes) {
        return employeeContentPostRepository.findContentsWithMinimumLikes(minLikes);
    }

    // Obtiene una lista de DTOS de Posts con un cantidad de likes menor a la de minLikes.
    public List<EmployeeContentPostDTO> getContentsWithLessThanMinimumLikes(int minLikes) {
        return employeeContentPostRepository.findContentsWithLessThanMinimumLikes(minLikes);
    }

}

