package com.org.ezequielBolzi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDTO {

    private Long id;

    private String title;

    private Integer year;

    private String director;

    private String genre;

    private String duration;

    private String typeContent;
}
