package com.org.ezequielBolzi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeDTO {
    private String employeePostCreator;
    private String contentTitle;
    private String contentType;
}
