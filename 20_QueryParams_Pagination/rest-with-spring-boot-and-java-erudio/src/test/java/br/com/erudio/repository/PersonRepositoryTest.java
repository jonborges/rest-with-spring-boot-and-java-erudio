package br.com.erudio.repository;

import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.erudio.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersonRepositoryTest extends AbstractIntegrationTest {

    @Autowired
    private PersonRepository repository;

    private Person person;

    @BeforeEach
    public void setup() {
        // Given
        repository.deleteAll();
        person = new Person("Leandro", "Costa", "leandro@erudio.com.br", "Uberlândia - Minas Gerais - Brasil", "Male");
    }

    @DisplayName("JUnit test for find people by name")
    @Test
    void testFindPeopleByName() {

        // Given
        repository.save(person);

        // When
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "firstName"));
        var people = repository.findPeopleByName("lea", pageable);

        // Then
        assertNotNull(people);
        assertEquals(1, people.getContent().size());
        assertEquals(1, people.getTotalElements());
        assertEquals(1, people.getTotalPages());

        var foundPerson = people.getContent().get(0);

        assertNotNull(foundPerson);
        assertEquals(person.getId(), foundPerson.getId());
        assertEquals("Leandro", foundPerson.getFirstName());
        assertEquals("Costa", foundPerson.getLastName());
        assertEquals("leandro@erudio.com.br", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertTrue(foundPerson.getEnabled());
    }

    @DisplayName("JUnit test for disable person")
    @Test
    void testDisablePerson() {

        // Given
        repository.save(person);

        // When
        repository.disablePerson(person.getId());

        // Then
        var disabledPerson = repository.findById(person.getId()).orElseThrow();

        assertNotNull(disabledPerson);
        assertFalse(disabledPerson.getEnabled());
    }
}