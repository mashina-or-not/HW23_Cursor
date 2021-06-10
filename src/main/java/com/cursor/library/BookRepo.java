package com.cursor.library;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class BookRepo {

    private final Map<String, Book> books = new HashMap<>();

    public BookRepo() {
        final String id1 = UUID.randomUUID().toString();
        books.put(id1, new Book(id1, "1984", "George Orwell", 1949, "Novel"));
        final String id2 = UUID.randomUUID().toString();
        books.put(id2, new Book(id2, "To Kill a Mockingbird", "Harper Lee", 1960, "Novel"));
        final String id3 = UUID.randomUUID().toString();
        books.put(id3, new Book(id3, "Harry Potter and the Philosopher’s Stone", "J.K. Rowling", 1997, "Fantasy"));
        final String id4 = UUID.randomUUID().toString();
        books.put(id4, new Book(id4, "The Lord of the Rings", "J.R.R. Tolkien", 1964, "Adventure"));
        final String id5 = UUID.randomUUID().toString();
        books.put(id5, new Book(id5, "The Great Gatsby", "F. Scott Fitzgerald", 1925, "Novel"));
    }

    public List<Book> getAll() {
        return new ArrayList<>(books.values());
    }

    public Book saveBook(
            final String name,
            final String author,
            final Integer year,
            final String genre
    ) {
        final Book newBook = new Book(UUID.randomUUID().toString(), name, author, year, genre);
        return books.put(newBook.getId(), newBook);
    }

    public Book findById(String bookId) {
        return books.get(bookId);
    }

    public List<Book> getAll(int limit, int offset) {
        return books.values().stream().skip(offset).limit(limit).collect(Collectors.toList());
    }

    public List<Book> getAllSorted(String sort) {
        ArrayList<Book> booksList = new ArrayList<>(books.values());
        booksList.sort(Comparator.comparing(
                book -> sort.equals("name") ? book.getName() : String.valueOf(book.getYear())));
        return booksList;
    }

    public List<Book> getAllByAuthor(String author) {
        return getAll().stream().
                filter(book -> book.getAuthor().equals(author)).
                collect(Collectors.toList());
    }

    public Book update(String id, CreateBookDto createBook) {
        String name = createBook.getName();
        String author = createBook.getAuthor();
        Integer year = createBook.getYear();
        String genre = createBook.getGenre();
        return books.replace(id, new Book(id, name, author, year, genre));
    }
}
