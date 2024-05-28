package com.org.ezequielBolzi.dtos;

import com.org.ezequielBolzi.enums.TypeContent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeContentPostDTO {
    private String name;
    private String email;
    private String title;
    private TypeContent typeContent;
    private Integer likes;

}