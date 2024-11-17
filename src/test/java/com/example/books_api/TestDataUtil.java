package com.example.books_api;

import com.example.books_api.domain.dto.AuthorDto;
import com.example.books_api.domain.dto.BookDto;
import com.example.books_api.domain.entities.AuthorEntity;
import com.example.books_api.domain.entities.BookEntity;

import java.util.List;

public final class TestDataUtil {
    private TestDataUtil() {}

    public static AuthorEntity createTestAuthor() {
        return AuthorEntity.builder().id(1L).name("Jane Foster").age(49).build();
    }

    public static List<AuthorEntity> createTestAuthors() {
        return List.of(
                AuthorEntity.builder().id(1L).name("Jane Foster").age(49).build(),
                AuthorEntity.builder().id(2L).name("Kim Dokja").age(28).build(),
                AuthorEntity.builder().id(3L).name("Cross Shakti").age(54).build()
        );
    }

    public static BookEntity createTestBook(AuthorEntity authorEntity) {
        return BookEntity.builder()
                .isbn("978-1-2345-6789-0")
                .title("Three Ways to Survive in the Ruined World")
                .authorEntity(authorEntity)
                .build();
    }

    public static List<BookEntity> createTestBooks(AuthorEntity authorEntity) {
        return List.of(
                BookEntity.builder()
                        .isbn("978-1-2345-6789-0")
                        .title("Three Ways to Survive in the Ruined World")
                        .authorEntity(authorEntity)
                        .build(),
                BookEntity.builder()
                        .isbn("897-2-3456-7890-1")
                        .title("Three Ways to Survive in Apocalypse")
                        .authorEntity(authorEntity)
                        .build(),
                BookEntity.builder()
                        .isbn("798-3-2456-7890-3")
                        .title("Ways of Survival")
                        .authorEntity(authorEntity)
                        .build()
        );
    }

    public static BookDto createTestBookDto(AuthorDto authorDto) {
        return BookDto.builder()
                .isbn("978-1-2345-6789-0")
                .title("Three Ways to Survive in the Ruined World")
                .author(authorDto)
                .build();
    }
}
