package com.javabegin.tasklist.backendspringboot.search;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskSearchValues {
    private String title;
    private Boolean completed;
    private Integer priorityId;
    private Integer categoryId;

    // Постраничность
    private Integer pageNumber;
    private Integer pageSize;

    // Сортировка
    private String sortColumn;
    private String sortDirection;
}
