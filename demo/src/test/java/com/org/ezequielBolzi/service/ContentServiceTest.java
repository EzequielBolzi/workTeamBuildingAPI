package com.org.ezequielBolzi.service;

import com.org.ezequielBolzi.dtos.ContentDTO;
import com.org.ezequielBolzi.enums.TypeContent;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.repository.ContentRepository;
import com.org.ezequielBolzi.repository.EmployeeContentPostRepository;
import com.org.ezequielBolzi.service.ContentService;
import com.org.ezequielBolzi.service.EmployeeContentPostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


public class ContentServiceTest {

    @Mock
    private ContentRepository contentRepository;
    @Mock
    private EmployeeContentPostService employeeContentPostService;
    @Mock
    private EntityManager entityManager;

    @MockBean
    private EmployeeContentPostRepository employeeContentPostRepository;

    @InjectMocks
    private ContentService contentService;

    private Content content;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void findAll() {
        content = new Content();
        content.setId(1L);
        content.setTitle("Test content 01");
        content.setYear(2020);
        content.setGenre("Drama");
        content.setDuration("101 minutes");
        content.setTypeContent(TypeContent.SERIE);
        when(contentRepository.findAll()).thenReturn(Arrays.asList(content));
        assertNotNull(contentService.getAllContentsAsDTOs());
    }

    @Test
    void getAllMoviesAsDTOs() {
        List<Content> movies = Arrays.asList(
                new Content(1L, "Movie 01", 2020, "Director 01", "Action", "120 minutes", TypeContent.PELICULA),
                new Content(2L, "Movie 02", 2021, "Director 02", "Comedy", "90 minutes", TypeContent.PELICULA)
        );

        when(contentRepository.findByTypeContent(TypeContent.PELICULA)).thenReturn(movies);

        List<ContentDTO> result = contentService.getAllMoviesAsDTOs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Movie 01", result.get(0).getTitle());
        assertEquals(2020, result.get(0).getYear());
        assertEquals("Director 01", result.get(0).getDirector());
        assertEquals("Action", result.get(0).getGenre());
        assertEquals("120 minutes", result.get(0).getDuration());
        assertEquals("Movie 02", result.get(1).getTitle());
        assertEquals(2021, result.get(1).getYear());
        assertEquals("Director 02", result.get(1).getDirector());
        assertEquals("Comedy", result.get(1).getGenre());
        assertEquals("90 minutes", result.get(1).getDuration());
    }

    @Test
    void getAllSeriesAsDTOs() {
        List<Content> series = Arrays.asList(
                new Content(4L, "Series 01", 2022, "Creator 01", "Drama", "60 minutes", TypeContent.SERIE),
                new Content(5L, "Series 02", 2023, "Creator 02", "Sci-Fi", "45 minutes", TypeContent.SERIE)
        );

        when(contentRepository.findByTypeContent(TypeContent.SERIE)).thenReturn(series);

        List<ContentDTO> result = contentService.getAllSeriesAsDTOs();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Series 01", result.get(0).getTitle());
        assertEquals(2022, result.get(0).getYear());
        assertEquals("Creator 01", result.get(0).getDirector());
        assertEquals("Drama", result.get(0).getGenre());
        assertEquals("60 minutes", result.get(0).getDuration());
        assertEquals("Series 02", result.get(1).getTitle());
        assertEquals(2023, result.get(1).getYear());
        assertEquals("Creator 02", result.get(1).getDirector());
        assertEquals("Sci-Fi", result.get(1).getGenre());
        assertEquals("45 minutes", result.get(1).getDuration());
    }

    @Test
    void createMovie() {
        // Preparación del objeto Employee
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Employee Name");

        // Preparación del objeto Content
        Content movie = new Content();
        movie.setId(3L);
        movie.setTitle("New Movie");
        movie.setYear(2022);
        movie.setDirector("New Director");
        movie.setGenre("Thriller");
        movie.setDuration("150 minutes");
        movie.setTypeContent(TypeContent.PELICULA);

        // Simulación de comportamiento de contentRepository
        when(contentRepository.save(any(Content.class))).thenReturn(movie);

        // Simulación de comportamiento de entityManager
        when(entityManager.merge(any(Employee.class))).thenReturn(employee);

        // Simulación de comportamiento de employeeContentPostService
        EmployeeContentPost post = new EmployeeContentPost();
        post.setEmployee(employee);
        post.setContent(movie);
        post.setRegisteredAt(LocalDateTime.now());
        post.setLikes(0);
        when(employeeContentPostService.save(any(EmployeeContentPost.class))).thenReturn(post);

        // Ejecución del método bajo prueba
        ContentDTO result = contentService.createMovie(movie, employee);

        // Verificaciones
        assertNotNull(result);
        assertEquals("New Movie", result.getTitle());
        assertEquals(2022, result.getYear());
        assertEquals("New Director", result.getDirector());
        assertEquals("Thriller", result.getGenre());
        assertEquals("150 minutes", result.getDuration());
    }


    @Test
    void createSerie() {
        // Preparación del objeto Employee
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("Employee Name");

        // Preparación del objeto Content
        Content serie = new Content();
        serie.setId(6L);
        serie.setTitle("New Series");
        serie.setYear(2024);
        serie.setDirector("New Creator");
        serie.setGenre("Fantasy");
        serie.setDuration("30 minutes");
        serie.setTypeContent(TypeContent.SERIE);

        // Simulación de comportamiento de contentRepository
        when(contentRepository.save(any(Content.class))).thenReturn(serie);

        // Simulación de comportamiento de entityManager
        when(entityManager.merge(any(Employee.class))).thenReturn(employee);

        // Simulación de comportamiento de employeeContentPostService
        EmployeeContentPost post = new EmployeeContentPost();
        post.setEmployee(employee);
        post.setContent(serie);
        post.setRegisteredAt(LocalDateTime.now());
        post.setLikes(0);
        when(employeeContentPostService.save(any(EmployeeContentPost.class))).thenReturn(post);

        // Ejecución del método bajo prueba
        ContentDTO result = contentService.createSerie(serie, employee);

        // Verificaciones
        assertNotNull(result);
        assertEquals("New Series", result.getTitle());
        assertEquals(2024, result.getYear());
        assertEquals("New Creator", result.getDirector());
        assertEquals("Fantasy", result.getGenre());
        assertEquals("30 minutes", result.getDuration());
    }

    @Test
    void filterContentsByGenre() {
        List<Content> contents = Arrays.asList(
                new Content(7L, "Content 01", 2025, "Director 01", "Action", "120 minutes", TypeContent.PELICULA),
                new Content(8L, "Content 02", 2026, "Director 02", "Comedy", "90 minutes", TypeContent.PELICULA)
        );

        when(contentRepository.findContentsByGenre("Action")).thenReturn(contents);

        List<ContentDTO> result = contentService.filterContentsByGenre("Action");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Content 01", result.get(0).getTitle());
        assertEquals(2025, result.get(0).getYear());
        assertEquals("Director 01", result.get(0).getDirector());
        assertEquals("Action", result.get(0).getGenre());
        assertEquals("120 minutes", result.get(0).getDuration());
        assertEquals("Content 02", result.get(1).getTitle());
        assertEquals(2026, result.get(1).getYear());
        assertEquals("Director 02", result.get(1).getDirector());
        assertEquals("Comedy", result.get(1).getGenre());
        assertEquals("90 minutes", result.get(1).getDuration());
    }

    @Test
    void filterContentsByYear() {
        List<Content> contents = Arrays.asList(
                new Content(9L, "Content 03", 2027, "Director 03", "Drama", "60 minutes", TypeContent.PELICULA),
                new Content(10L, "Content 04", 2028, "Director 04", "Sci-Fi", "45 minutes", TypeContent.PELICULA)
        );

        when(contentRepository.findContentsByYear(2027)).thenReturn(contents);

        List<ContentDTO> result = contentService.filterContentsByYear(2027);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Content 03", result.get(0).getTitle());
        assertEquals(2027, result.get(0).getYear());
        assertEquals("Director 03", result.get(0).getDirector());
        assertEquals("Drama", result.get(0).getGenre());
        assertEquals("60 minutes", result.get(0).getDuration());
        assertEquals("Content 04", result.get(1).getTitle());
        assertEquals(2028, result.get(1).getYear());
        assertEquals("Director 04", result.get(1).getDirector());
        assertEquals("Sci-Fi", result.get(1).getGenre());
        assertEquals("45 minutes", result.get(1).getDuration());
    }
}
