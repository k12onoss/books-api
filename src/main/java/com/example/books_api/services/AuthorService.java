package com.example.books_api.services;

import com.example.books_api.domain.entities.AuthorEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface AuthorService {
    AuthorEntity saveAuthor(AuthorEntity authorEntity);

    List<AuthorEntity> findAll();

    Page<AuthorEntity> findAll(Pageable pageable);

    Optional<AuthorEntity> findOne(Long id);

    boolean isPresent(Long id);

    AuthorEntity partialUpdateAuthor(Long id, AuthorEntity authorEntity);

    void delete(Long id);
}
