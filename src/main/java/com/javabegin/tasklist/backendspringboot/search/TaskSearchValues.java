package com.javabegin.tasklist.backendspringboot.search;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Schema(name = "Search values for tasks")
public class TaskSearchValues {
    private String title;
    private Boolean completed;
    private Integer priorityId;
    private Integer categoryId;

    // Постраничность
    @Schema(description = "Number of page", example = "1")
    private Integer pageNumber;
    @Schema(description = "Count of tasks per page", example = "12")
    private Integer pageSize;

    // Сортировка
    @Schema(description = "Sort by parameter", example = "title")
    private String sortColumn;
    @Schema(description = "Direction of sorting", example = "ASC", defaultValue = "DESC")
    private String sortDirection;
}
