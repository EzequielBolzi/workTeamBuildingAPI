package com.org.ezequielBolzi.service;

import com.org.ezequielBolzi.dtos.ContentDTO;
import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.enums.TypeContent;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.model.EmployeeContentPost;
import com.org.ezequielBolzi.repository.ContentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {
    @Autowired
    private ContentRepository contentRepository;
    @Autowired
    private EmployeeContentPostService employeeContentPostService;
    @Autowired
    private EntityManager entityManager;

    // Obtiene todos los contenidos (peliculas y series) de la base de datos.
    private List<Content> getAllContents() {
        return contentRepository.findAll();
    }

    // Convierte todos los contenidos obtenidos en DTOs (Data Transfer Objects) para su uso en la capa de presentación.
    public List<ContentDTO> getAllContentsAsDTOs() {
        List<Content> contents = getAllContents();
        return contents.stream()
                .map(content -> new ContentDTO(
                        content.getId(),
                        content.getTitle(),
                        content.getYear(),
                        content.getDirector(),
                        content.getGenre(),
                        content.getDuration(),
                        content.getTypeContent().name()
                ))
                .collect(Collectors.toList());
    }

    // Obtiene todos los contenidos de tipo película como DTOs.
    public List<ContentDTO> getAllMoviesAsDTOs() {
        List<Content> movies = contentRepository.findByTypeContent(TypeContent.PELICULA);
        return movies.stream()
                .map(movie -> new ContentDTO(
                        movie.getId(),
                        movie.getTitle(),
                        movie.getYear(),
                        movie.getDirector(),
                        movie.getGenre(),
                        movie.getDuration(),
                        movie.getTypeContent().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ContentDTO createMovie(Content movie, Employee employee) {
        // Assuming contentRepository is injected via constructor or field injection
        Content savedMovie = contentRepository.save(movie);
        // Fetching the employee within the same session
        Employee mergedEmployee = entityManager.merge(employee); // Use EntityManager to merge the employee
        EmployeeContentPost post = new EmployeeContentPost();
        post.setEmployee(mergedEmployee); // Use the merged employee
        post.setContent(savedMovie);
        post.setRegisteredAt(LocalDateTime.now());
        post.setLikes(0);
        employeeContentPostService.save(post);
        return new ContentDTO(
                savedMovie.getId(),
                savedMovie.getTitle(),
                savedMovie.getYear(),
                savedMovie.getDirector(),
                savedMovie.getGenre(),
                savedMovie.getDuration(),
                savedMovie.getTypeContent().name()
        );
    }

    // Obtiene todos los contenidos de tipo serie como DTOs.
    public List<ContentDTO> getAllSeriesAsDTOs() {
        List<Content> movies = contentRepository.findByTypeContent(TypeContent.SERIE);
        return movies.stream()
                .map(serie -> new ContentDTO(
                        serie.getId(),
                        serie.getTitle(),
                        serie.getYear(),
                        serie.getDirector(),
                        serie.getGenre(),
                        serie.getDuration(),
                        serie.getTypeContent().name()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public ContentDTO createSerie(Content serie, Employee employee) {
        Content savedSerie = contentRepository.save(serie);
        // Fetching the employee within the same session
        Employee mergedEmployee = entityManager.merge(employee); // Use EntityManager to merge the employee
        EmployeeContentPost post = new EmployeeContentPost();
        post.setEmployee(mergedEmployee); // Use the merged employee
        post.setContent(savedSerie);
        post.setRegisteredAt(LocalDateTime.now());
        post.setLikes(0);
        employeeContentPostService.save(post);
        return new ContentDTO(
                savedSerie.getId(),
                savedSerie.getTitle(),
                savedSerie.getYear(),
                savedSerie.getDirector(),
                savedSerie.getGenre(),
                savedSerie.getDuration(),
                savedSerie.getTypeContent().name()
        );
    }

    // Filtra los contenidos por género y devuelve los resultados como DTOs.
    public List<ContentDTO> filterContentsByGenre(String genre) {
        return contentRepository.findContentsByGenre(genre).stream()
                .map(content -> new ContentDTO(
                        content.getId(),
                        content.getTitle(),
                        content.getYear(),
                        content.getDirector(),
                        content.getGenre(),
                        content.getDuration(),
                        content.getTypeContent().name()
                ))
                .collect(Collectors.toList());
    }

    // Filtra los contenidos por año y devuelve los resultados como DTOs.
    public List<ContentDTO> filterContentsByYear(Integer year) {
        return contentRepository.findContentsByYear(year).stream()
                .map(content -> new ContentDTO(
                        content.getId(),
                        content.getTitle(),
                        content.getYear(),
                        content.getDirector(),
                        content.getGenre(),
                        content.getDuration(),
                        content.getTypeContent().name()
                ))
                .collect(Collectors.toList());
    }

    // Obtiene los contenidos con un cantidad de likes mayorIgual a la de minLikes.
    public List<EmployeeContentPostDTO> getContentsWithMinimumLikes(int minLikes) {
        return employeeContentPostService.getContentsWithMinimumLikes(minLikes);
    }

    // Obtiene los contenidos con un cantidad de likes menor a la de minLikes.
    public List<EmployeeContentPostDTO> getContentsWithLessThanMinimumLikes(int minLikes) {
        return employeeContentPostService.getContentsWithLessThanMinimumLikes(minLikes);
    }



}