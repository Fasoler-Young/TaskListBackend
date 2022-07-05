package com.javabegin.tasklist.backendspringboot.exсeption;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Error custom response")
public class Response {
    @Schema(description = "Error message")
    private String message;
}
