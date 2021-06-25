package com.cursor.library;

import com.cursor.library.controller.BookController;
import com.cursor.library.dto.CreateBookDto;
import com.cursor.library.repository.BookRepo;
import com.cursor.library.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.UUID;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {BookController.class, BookRepo.class, BookService.class})
@WebMvcTest
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepo bookRepo;

    private final String URI = "/books";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @ParameterizedTest
    @CsvSource(
            value = "limit:offset:5:0:1000",
            delimiter = ':'
    )
    public void getBooksPaginationTest(
            String limit,
            String offset,
            String limitValue,
            String offsetValue,
            String offsetNotFound
    ) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(limit, limitValue)
                        .param(offset, offsetValue)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(limit, limitValue)
                        .param(offset, offsetNotFound)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @ParameterizedTest
    @CsvSource(
            value = "sort:name:year:notFound",
            delimiter = ':'
    )
    public void getBooksSortTest(
            String sort,
            String name,
            String year,
            String notFound
    ) throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(sort, name)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(sort, year)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(
                MockMvcRequestBuilders
                        .get(URI)
                        .param(sort, notFound)
        ).andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    @ParameterizedTest
    @CsvSource(
            value = "author:Harper Lee:no name",
            delimiter = ':'
    )
    public void getBooksAuthorTest(
            String author,
            String authorValue,
            String authorNotFound
    ) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get(URI)
                .param(author, authorValue)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                .get(URI)
                .param(author, authorNotFound)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @Test
    public void getBookTest() throws Exception {
        String id = bookRepo.getAll().get(0).getId();
        String idNotFound = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders
                .get(URI + "/" + id)
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                .get(URI + "/" + idNotFound)
        ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    @ParameterizedTest
    @CsvSource(
            value = "New Book:New Author:2000:New Genre",
            delimiter = ':'
    )
    public void updateBookTest(
            String newName,
            String newAuthor,
            Integer newYear,
            String newGenre
    ) throws Exception {
        CreateBookDto createBookDto = new CreateBookDto();
        createBookDto.setName(newName);
        createBookDto.setAuthor(newAuthor);
        createBookDto.setYear(newYear);
        createBookDto.setGenre(newGenre);

        String id = bookRepo.getAll().get(0).getId();
        String idNotFound = UUID.randomUUID().toString();

        mockMvc.perform(MockMvcRequestBuilders
                .put(URI + '/' + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(createBookDto))
        ).andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        mockMvc.perform(MockMvcRequestBuilders
                .put(URI + '/' + idNotFound)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsBytes(createBookDto))
        ).andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }
}
