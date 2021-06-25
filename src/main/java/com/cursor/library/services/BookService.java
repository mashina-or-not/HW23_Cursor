package com.cursor.library.services;

import com.cursor.library.entity.Book;
import com.cursor.library.repository.BookRepo;
import com.cursor.library.dto.CreateBookDto;
import com.cursor.library.exception.CreateBookException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final BookRepo bookRepo;

    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public Book createBook(
            final String name,
            final String author,
            final Integer year,
            final String genre
    ) throws CreateBookException {
        if (name == null) {
            throw new CreateBookException("Name field cannot be null");
        }
        if (year == null || year > 2021) {
            throw new CreateBookException("Illegal year argument");
        }
        return bookRepo.saveBook(
                name.trim(),
                author == null ? null : author.trim(),
                year,
                genre == null ? null : genre.trim());
    }

    public List<Book> getAll() {
        return bookRepo.getAll();
    }

    public Book findById(String bookId) {
        return bookRepo.findById(bookId);
    }

    public List<Book> getAll(int limit, int offset) {
        return bookRepo.getAll(limit, offset);
    }

    public List<Book> getAllSorted(String sort) {
        return bookRepo.getAllSorted(sort);
    }

    public List<Book> getAllBooksByAuthor(String author) {
        return bookRepo.getAllByAuthor(author);
    }

    public Book updateBook(String id, CreateBookDto createBookDto) {
        return bookRepo.update(id, createBookDto);
    }
}
