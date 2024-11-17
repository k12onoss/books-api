package com.example.books_api.controllers;

import com.example.books_api.TestDataUtil;
import com.example.books_api.domain.dto.BookDto;
import com.example.books_api.domain.entities.BookEntity;
import com.example.books_api.services.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {
    private final MockMvc mockMvc;
    private final BookService bookService;
    private final ObjectMapper objectMapper;

    @Autowired
    public BookControllerIntegrationTest(MockMvc mockMvc, BookService bookService) {
        this.mockMvc = mockMvc;
        this.bookService = bookService;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsHttp201Created() throws Exception {
        BookDto book = TestDataUtil.createTestBookDto(null);
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void testThatCreateBookSuccessfullyReturnsSavedBook() throws Exception {
        BookDto book = TestDataUtil.createTestBookDto(null);
        String bookJson = objectMapper.writeValueAsString(book);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value("Three Ways to Survive in the Ruined World")
        );
    }

    @Test
    public void testThatListBooksSuccessfullyReturnsHttp200Ok() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatListBooksSuccessfullyReturnsSavedBooks() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.saveBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].isbn").value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].title")
                        .value("Three Ways to Survive in the Ruined World")
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsHttp200OkWhenExists() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.saveBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsHttp404NotFoundWhenDoesntExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatGetBookSuccessfullyReturnsSavedBook() throws Exception {
        BookEntity book = TestDataUtil.createTestBook(null);
        bookService.saveBook(book.getIsbn(), book);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/books/" + book.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value("978-1-2345-6789-0")
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title")
                        .value("Three Ways to Survive in the Ruined World")
        );
    }

    @Test
    public void testThatFullUpdateBookSuccessfullyReturnsHttp200OkWhenBookExits() throws Exception {
        List<BookEntity> bookEntities = TestDataUtil.createTestBooks(null);
        bookService.saveBook(bookEntities.getFirst().getIsbn(), bookEntities.getFirst());

        String bookJson = objectMapper.writeValueAsString(bookEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntities.getFirst().getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatFullUpdateBookSuccessfullyReturnsUpdatedBook() throws Exception {
        List<BookEntity> bookEntities = TestDataUtil.createTestBooks(null);
        bookService.saveBook(bookEntities.getFirst().getIsbn(), bookEntities.getFirst());

        String bookJson = objectMapper.writeValueAsString(bookEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.put("/books/" + bookEntities.getFirst().getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntities.getFirst().getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntities.get(1).getTitle())
        );
    }

    @Test
    public void testThatPartialUpdateBookSuccessfullyReturnsHttp200OkWhenBookExits() throws Exception {
        List<BookEntity> bookEntities = TestDataUtil.createTestBooks(null);
        bookService.saveBook(bookEntities.getFirst().getIsbn(), bookEntities.getFirst());

        String bookJson = objectMapper.writeValueAsString(bookEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntities.getFirst().getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void testThatPartialUpdateBookSuccessfullyReturnsHttp404NotFoundWhenBookDoesntExits() throws Exception {
        List<BookEntity> bookEntities = TestDataUtil.createTestBooks(null);

        String bookJson = objectMapper.writeValueAsString(bookEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntities.getFirst().getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void testThatPartialUpdateBookSuccessfullyReturnsUpdatedBook() throws Exception {
        List<BookEntity> bookEntities = TestDataUtil.createTestBooks(null);
        bookService.saveBook(bookEntities.getFirst().getIsbn(), bookEntities.getFirst());

        String bookJson = objectMapper.writeValueAsString(bookEntities.get(1));

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/books/" + bookEntities.getFirst().getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.isbn").value(bookEntities.getFirst().getIsbn())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.title").value(bookEntities.get(1).getTitle())
        );
    }

    @Test
    public void testThatDeleteBookSuccessfullyReturnsHttp200OkForExistingBook() throws Exception {
        BookEntity bookEntity = TestDataUtil.createTestBook(null);
        bookService.saveBook(bookEntity.getIsbn(), bookEntity);

        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/" + bookEntity.getIsbn())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }

    @Test
    public void testThatDeleteBookSuccessfullyReturnsHttp404NotFoundForNonExistingBook() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/books/99")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}
