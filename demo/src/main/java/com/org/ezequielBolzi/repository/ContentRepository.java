package com.org.ezequielBolzi.repository;

import com.org.ezequielBolzi.enums.TypeContent;
import com.org.ezequielBolzi.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {
    List<Content> findByTypeContent(TypeContent typeContent);
    List<Content> findAll();

    List<Content> findByGenre(String genre);
    List<Content> findByYear(Integer year);

    // Consulta personalizada para filtrar por género
    @Query("SELECT c FROM Content c WHERE (:genre IS NULL OR c.genre = :genre)")
    List<Content> findContentsByGenre(@Param("genre") String genre);

    // Consulta personalizada para filtrar por año
    @Query("SELECT c FROM Content c WHERE (:year IS NULL OR c.year = :year)")
    List<Content> findContentsByYear(@Param("year") Integer year);
}

