package com.org.ezequielBolzi.model;

import com.org.ezequielBolzi.enums.TypeContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "content")
public class Content {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "director", nullable = false, length = 255)
    private String director;

    @Column(name = "genre", nullable = false, length = 255)
    private String genre;

    @Column(name = "duration", nullable = false, length = 255)
    private String duration;

    @Column(name = "type_content", nullable = false, length = 255)
    @Enumerated(EnumType.STRING)
    private TypeContent typeContent;
}