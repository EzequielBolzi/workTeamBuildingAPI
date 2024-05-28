package com.org.ezequielBolzi.controller;

import com.org.ezequielBolzi.dtos.ContentDTO;
import com.org.ezequielBolzi.dtos.EmployeeContentPostDTO;
import com.org.ezequielBolzi.dtos.LikeDTO;
import com.org.ezequielBolzi.model.Content;
import com.org.ezequielBolzi.model.Employee;
import com.org.ezequielBolzi.repository.ContentRepository;
import com.org.ezequielBolzi.service.ContentService;
import com.org.ezequielBolzi.service.EmployeeContentPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    @Autowired
    private final ContentRepository contentRepository;
    @Autowired
    private ContentService contentService;
    @Autowired
    private EmployeeContentPostService employeeContentPostService;

    public ContentController(ContentRepository contentRepository, ContentService contentService, EmployeeContentPostService employeeContentPostService) {
        this.contentRepository = contentRepository;
        this.contentService = contentService;
        this.employeeContentPostService = employeeContentPostService;
    }

    @Operation(summary = "Obtener todos los contenidos", description = "Recupera una lista de todos los contenidos.",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/all")
    public List<ContentDTO> getAllContents() {
        return contentService.getAllContentsAsDTOs();
    }

    @Operation(summary = "Filtrar contenidos por género", description = "Recupera una lista de contenidos filtrados por género.",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/filter/genre")
    public List<ContentDTO> filterContentGenre(@RequestParam(required = false) String genre) {
        return contentService.filterContentsByGenre(genre);
    }

    @Operation(summary = "Filtrar contenidos por año", description = "Recupera una lista de contenidos filtrados por año.",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/filter/year")
    public List<ContentDTO> filterContentYear(@RequestParam(required = false) Integer year) {
        return contentService.filterContentsByYear(year);
    }

    @Operation(summary = "Obtener contenidos con un mínimo de 'me gusta'", description = "Recupera una lista de contenidos con un número mínimo de 'me gusta'.",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/filter/likes/mayorIgual-a")
    public List<EmployeeContentPostDTO> getContentsWithMinimumLikes(@RequestParam int minLikes) {
        return contentService.getContentsWithMinimumLikes(minLikes);
    }

    @Operation(summary = "Obtener contenidos con menos de un mínimo de 'me gusta'", description = "Recupera una lista de contenidos con menos de un número mínimo de 'me gusta'.",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/filter/likes/menor-a")
    public List<EmployeeContentPostDTO> getContentsWithLessThanMinimumLikes(@RequestParam int minLikes) {
        return contentService.getContentsWithLessThanMinimumLikes(minLikes);
    }

    @Operation(summary = "Obtener todas las películas", description = "Recupera una lista de todas las películas.",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/movies")
    public List<ContentDTO> getAllMovies() {
        return contentService.getAllMoviesAsDTOs();
    }

    @Operation(summary = "Crear una nueva película", description = "Crea una nueva película con los detalles proporcionados." +
            "Cuando se crea un contenido, el mismo se asocia al empleado originario y se guarda en la base de datos de posteos.",security = @SecurityRequirement(name = "bearerAuth"))


    @PostMapping("/movies/create")
    public ContentDTO createMovie(@RequestBody Content movie) {
        // Obtenemos la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Extraemos el empleado del objeto principal de autenticación
        Employee employee = (Employee) authentication.getPrincipal();


        return  contentService.createMovie(movie, employee);
    }

    @Operation(summary = "Obtener todas las series", description = "Recupera una lista de todas las series.",security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/series")
    public List<ContentDTO> getAllSeries() {
        return contentService.getAllSeriesAsDTOs();
    }

    @Operation(summary = "Crear una nueva serie",
            description = "Crea una nueva serie con los detalles proporcionados." +
                    "Cuando se crea un contenido, el mismo se asocia al empleado originario y se guarda en la base de datos de posteos.",
            security = {@SecurityRequirement(name = "bearerAuth")})
    @PostMapping("/series/create")
    public ContentDTO createSerie(@RequestBody Content series) {
        // Obtenemos la autenticación actual
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Extraemos el empleado del objeto principal de autenticación
        Employee employee = (Employee) authentication.getPrincipal();


        return  contentService.createSerie(series, employee);
    }


}
