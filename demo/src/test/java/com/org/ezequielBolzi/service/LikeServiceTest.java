package com.org.ezequielBolzi.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.org.ezequielBolzi.customException.CustomBadRequestException;
import com.org.ezequielBolzi.customException.CustomInternalServerException;
import com.org.ezequielBolzi.dtos.LikeDTO;
import com.org.ezequielBolzi.enums.TypeContent;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.model.EmployeeContentPostLike;
import com.org.ezequielBolzi.repository.ContentRepository;
import com.org.ezequielBolzi.repository.EmployeeContentPostLikeRepository;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;
import com.org.ezequielBolzi.repository.EmployeeRepository;
import com.org.ezequielBolzi.service.LikeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class LikeServiceTest {
    @Mock
    private EmployeeContentPostRepository employeeContentPostRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeContentPostLikeRepository employeeContentPostLikeRepository;

    @Mock
    private ContentRepository contentRepository;

    @InjectMocks
    private LikeService likeService;

    private Employee employee;
    private Employee employeeB;
    private Content content;
    private EmployeeContentPost post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        setUpMocks();
    }

    void setUpMocks() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("Test Employee");
        employee.setHasLiked(false);

        employeeB = new Employee();
        employeeB.setId(2L);
        employeeB.setName("Test EmployeeB");
        employeeB.setHasLiked(false);

        content = new Content();
        content.setId(1L);
        content.setTitle("blabla");
        content.setGenre("bla");
        content.setYear(2222);
        content.setDuration("40 minutes");
        content.setTypeContent(TypeContent.SERIE);

        post = new EmployeeContentPost();
        post.setId(1L);
        post.setEmployee(employeeB);
        post.setContent(content);
        post.setLikes(1);
    }

    @Test
    void likePost() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeContentPostRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(employeeContentPostLikeRepository.existsByEmployeeAndPost(any(Employee.class), any(EmployeeContentPost.class))).thenReturn(false);
        when(contentRepository.findById(anyLong())).thenReturn(Optional.of(content));

        // When
        LikeDTO result = likeService.likePost(post.getId(), employee.getId());

        // Then
        assertEquals(2, post.getLikes(), "El número de likes debe haberse incrementado a 2");
        verify(employeeRepository, times(1)).save(employee); // El empleado debe haber sido guardado
        verify(employeeContentPostLikeRepository, times(1)).save(any(EmployeeContentPostLike.class)); // El objeto EmployeeContentPostLike debe haber sido guardado

        // Verificar que el LikeDTO devuelto tenga los valores esperados
        assertNotNull(result, "El resultado no puede ser nulo");
        assertEquals(employee.getName(), result.getEmployeePostCreator(), "El nombre del empleado debe coincidir");
        assertEquals(post.getContent().getTitle(), result.getContentTitle(), "El título del contenido debe coincidir");
        assertEquals(post.getContent().getTypeContent().name(), result.getContentType(), "El tipo de contenido debe coincidir");
    }
    @Test
    void unlikePost() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeContentPostRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(employeeContentPostLikeRepository.existsByEmployeeAndPost(any(Employee.class), any(EmployeeContentPost.class))).thenReturn(true);
        when(contentRepository.findById(anyLong())).thenReturn(Optional.of(content));

        // When
        LikeDTO result = likeService.unLikePost(post.getId(), employee.getId());

        // Then
        assertEquals(0, post.getLikes(), "El número de likes debe haberse decrementado a 0");
        verify(employeeRepository, times(1)).save(employee); // El empleado debe haber sido guardado
        verify(employeeContentPostLikeRepository, times(1)).deleteByEmployeeAndPost(employee.getId(), post.getId()); // El objeto EmployeeContentPostLike debe haber sido eliminado
        verify(employeeContentPostRepository, times(1)).save(post); // El post debe haber sido guardado

        // Verificar que el LikeDTO devuelto tenga los valores esperados
        assertNotNull(result, "El resultado no puede ser nulo");
        assertEquals(employee.getName(), result.getEmployeePostCreator(), "El nombre del empleado debe coincidir");
        assertEquals(post.getContent().getTitle(), result.getContentTitle(), "El título del contenido debe coincidir");
        assertEquals(post.getContent().getTypeContent().name(), result.getContentType(), "El tipo de contenido debe coincidir");
    }


    @Test
    void unlikePost_EmpleadoNoEncontrado() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(CustomInternalServerException.class, () -> likeService.unLikePost(post.getId(), employee.getId()), "Debe lanzarse una CustomInternalServerException");
    }

    @Test
    void unlikePost_PosteoNoEncontrado() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeContentPostRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Then
        assertThrows(CustomInternalServerException.class, () -> likeService.unLikePost(post.getId(), employee.getId()), "Debe lanzarse una CustomInternalServerException");
    }

    @Test
    void unlikePost_NoHaLikeadoEstePost() {
        // Given
        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));
        when(employeeContentPostRepository.findById(anyLong())).thenReturn(Optional.of(post));
        when(employeeContentPostLikeRepository.existsByEmployeeAndPost(any(Employee.class), any(EmployeeContentPost.class))).thenReturn(false);

        // Then
        assertThrows(CustomBadRequestException.class, () -> likeService.unLikePost(post.getId(), employee.getId()), "Debe lanzarse una CustomBadRequestException");
    }
}
