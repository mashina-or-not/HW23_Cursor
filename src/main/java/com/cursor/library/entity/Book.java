package com.cursor.library.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Book {

    private String id;
    private String name;
    private String author;
    private Integer year;
    private String genre;
}
