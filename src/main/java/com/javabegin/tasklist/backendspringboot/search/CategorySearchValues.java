package com.javabegin.tasklist.backendspringboot.search;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Schema(name = "Search values for categories")
public class CategorySearchValues {
    @Schema(name = "the text you are looking for", example = "cat")
    private String text;
}
