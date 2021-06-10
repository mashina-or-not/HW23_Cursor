package com.cursor.library;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(
            value = "/books",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Book> createBook(
            @RequestBody final CreateBookDto createBookDto
    ) throws CreateBookException {
        final Book book = bookService.createBook(createBookDto.getName(), createBookDto.getAuthor(), createBookDto.getYear(), createBookDto.getGenre());
        return ResponseEntity.ok(book);
    }

    @GetMapping(
            value = "/books",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getBooks() {
        return new ResponseEntity<>(bookService.getAll(), HttpStatus.OK);
    }

    @GetMapping(
            value = "/books",
            params = {"limit", "offset"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getBooksPagination(
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset) {
        List<Book> bookList = bookService.getAll(limit, offset);
        if (bookList == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bookList);
    }

    @GetMapping(
            value = "/books",
            params = {"sort"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getBooksSort(
            @RequestParam(value = "sort", defaultValue = "name") String sort) {
        List<Book> allSorted = bookService.getAllSorted(sort);
        if (allSorted == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allSorted);
    }

    @GetMapping(
            value = "/books/{bookId}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Book> getBook(@PathVariable("bookId") String bookId) {
        final Book result = bookService.findById(bookId);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping(
            value = "/books",
            params = {"author"},
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<Book>> getBooksAuthor(
            @RequestParam(value = "author") String author) {
        List<Book> allBooksByAuthor = bookService.getAllBooksByAuthor(author);
        if (allBooksByAuthor == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allBooksByAuthor);
    }

    @PutMapping(
            value = "/books/{bookId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Book> updateBook(
            @PathVariable(value = "bookId") String id,
            @RequestBody CreateBookDto createBookDto) throws CreateBookException {
        Book updateBook = bookService.updateBook(id, createBookDto);
        if (updateBook == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updateBook);
    }

}
