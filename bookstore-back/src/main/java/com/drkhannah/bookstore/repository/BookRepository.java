package com.drkhannah.bookstore.repository;

import com.drkhannah.bookstore.model.Book;
import com.drkhannah.bookstore.util.NumberGenerator;
import com.drkhannah.bookstore.util.TextUtil;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

import static javax.transaction.Transactional.TxType.SUPPORTS;
import static javax.transaction.Transactional.TxType.REQUIRED;

@Transactional(SUPPORTS)
public class BookRepository {

    @PersistenceContext(unitName = "bookStorePU")
    private EntityManager em;

    @Inject
    private TextUtil textUtil;
    @Inject
    private NumberGenerator generator;

    // Constructor
    public BookRepository() { }

    public Book find(@NotNull Long id) {
        return em.find(Book.class, id);
    }

    public List<Book> findAll() {
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b ORDER BY b.title", Book.class);
        return query.getResultList();
    }

    public Long countAll() {
        TypedQuery<Long> query = em.createQuery("SELECT COUNT(b) FROM Book b", Long.class);
        return query.getSingleResult();
    }

    @Transactional(REQUIRED)
    public Book create(@NotNull Book book) {
        book.setTitle(textUtil.sanitize(book.getTitle()));
        book.setIsbn(generator.generateNumber());
        em.persist(book);
        return book;
    }

    @Transactional(REQUIRED)
    public void delete(@NotNull Long id) {
        em.remove(em.getReference(Book.class, id));
    }
}