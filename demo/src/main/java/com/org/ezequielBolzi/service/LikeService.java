package com.org.ezequielBolzi.service;

import com.org.ezequielBolzi.customException.CustomBadRequestException;
import com.org.ezequielBolzi.customException.CustomInternalServerException;
import com.org.ezequielBolzi.dtos.LikeDTO;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.model.EmployeeContentPostLike;
import com.org.ezequielBolzi.repository.ContentRepository;
import com.org.ezequielBolzi.repository.EmployeeContentPostLikeRepository;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;
import com.org.ezequielBolzi.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LikeService {
    @Autowired
    private EmployeeContentPostRepository employeeContentPostRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private EmployeeContentPostLikeRepository employeeContentPostLikeRepository;

    // Genera un "like" a un post específico, lanzando excepciones si el post o el empleado no se encuentran, o si el empleado intenta "likear" su propio post.
    @Transactional
    public LikeDTO likePost(Long postId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CustomInternalServerException("Empleado no encontrado"));
        EmployeeContentPost post = employeeContentPostRepository.findById(postId)
                .orElseThrow(() -> new CustomInternalServerException("Posteo no encontrado"));

        if (post.getEmployee().getId().equals(employeeId)) {
            throw new CustomBadRequestException("Un empleado no puede liker su propio post");
        }

        if (employee.isHasLiked()) {
            throw new CustomInternalServerException("Ya ha likeado un post");
        }

        post.setLikes(post.getLikes() + 1);
        employee.setHasLiked(true);
        employeeRepository.save(employee);

        EmployeeContentPostLike like = new EmployeeContentPostLike();
        like.setEmployee(employee);
        like.setPost(post);
        employeeContentPostLikeRepository.save(like);

        // Buscar el contenido relacionado y construir el LikeDTO
        Content content = contentRepository.findById(post.getContent().getId())
                .orElseThrow(() -> new CustomInternalServerException("Contenido no encontrado"));

        return new LikeDTO(
                employee.getName(),
                content.getTitle(),
                content.getTypeContent().name()
        );
    }

    // Elimina el "like" a un post específico, lanzando excepciones si el post o el empleado no se encuentran, o si el empleado intenta "dislikear" su propio post.
    @Transactional
    public LikeDTO unLikePost(Long postId, Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new CustomInternalServerException("Empleado no encontrado"));
        EmployeeContentPost post = employeeContentPostRepository.findById(postId)
                .orElseThrow(() -> new CustomInternalServerException("Posteo no encontrado"));

        if (post.getEmployee().getId().equals(employeeId)) {
            throw new CustomBadRequestException("No puede dislikear su propio post");
        }

        if (!employeeContentPostLikeRepository.existsByEmployeeAndPost(employee, post)) {
            throw new CustomBadRequestException("No ha likeado este post");
        }

        post.setLikes(post.getLikes() - 1);
        employee.setHasLiked(false);
        employeeRepository.save(employee);
        employeeContentPostLikeRepository.deleteByEmployeeAndPost(employee.getId(), post.getId());

        employeeContentPostRepository.save(post);
        // Buscar el contenido relacionado y construir el LikeDTO
        Content content = contentRepository.findById(post.getContent().getId())
                .orElseThrow(() -> new CustomInternalServerException("Contenido no encontrado"));

        return new LikeDTO(
                employee.getName(),
                content.getTitle(),
                content.getTypeContent().name()
        );
    }


}

