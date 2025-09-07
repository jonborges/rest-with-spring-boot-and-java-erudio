package br.com.erudio.services;

import br.com.erudio.data.dto.BookDTO;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.exception.ResourceNotFoundException;
import br.com.erudio.model.Book;
import br.com.erudio.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServicesTest {

    @InjectMocks
    private BookServices service;

    @Mock
    private BookRepository repository;

    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        book = new Book();
        book.setId(1L);
        book.setAuthor("Autor Teste");
        book.setTitle("Título Teste");
        book.setPrice(50.0);
        book.setLaunchDate(new Date());

        // Supondo que você tenha um parseObject entre Book e BookDTO
        bookDTO = new BookDTO();
        bookDTO.setId(1L);
        bookDTO.setAuthor("Autor Teste");
        bookDTO.setTitle("Título Teste");
        bookDTO.setPrice(50.0);
        bookDTO.setLaunchDate(new Date());
    }

    @Test
    void testFindByIdSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));

        BookDTO result = service.findById(1L);
        assertNotNull(result);
        assertEquals(book.getId(), result.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.findById(1L));
    }

    @Test
    void testCreateBookSuccess() {
        when(repository.save(any(Book.class))).thenReturn(book);

        BookDTO result = service.create(bookDTO);
        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
    }

    @Test
    void testCreateBookNull() {
        assertThrows(RequiredObjectIsNullException.class, () -> service.create(null));
    }

    @Test
    void testUpdateBookSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        when(repository.save(any(Book.class))).thenReturn(book);

        BookDTO result = service.update(bookDTO);
        assertNotNull(result);
        assertEquals(bookDTO.getTitle(), result.getTitle());
    }

    @Test
    void testDeleteBookSuccess() {
        when(repository.findById(1L)).thenReturn(Optional.of(book));
        doNothing().when(repository).delete(book);

        assertDoesNotThrow(() -> service.delete(1L));
        verify(repository, times(1)).delete(book);
    }
}
