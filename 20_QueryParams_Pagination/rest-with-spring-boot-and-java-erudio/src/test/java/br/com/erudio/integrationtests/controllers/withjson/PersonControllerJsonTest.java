package br.com.erudio.integrationtests.controllers.withjson;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
 
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.integrationtests.dto.PersonDTO;
import br.com.erudio.integrationtests.dto.wrapper.WrapperPersonDTO;
import br.com.erudio.integrationtests.testcontainers.AbstractIntegrationTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Order;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import java.util.List;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.MediaType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerJsonTest extends AbstractIntegrationTest {

    private RequestSpecification specification;
    private ObjectMapper objectMapper;

    private PersonDTO person;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        person = new PersonDTO();

        specification = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();
    }

    @Test
    @Order(1)
    void create() throws JsonProcessingException {
        mockPerson(person);

        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
            .when()
                .post()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        PersonDTO createdPerson = objectMapper.readValue(content, PersonDTO.class);
        person = createdPerson;

        assertNotNull(createdPerson.getId());
        assertTrue(createdPerson.getId() > 0);

        assertEquals("Richard", createdPerson.getFirstName());
        assertEquals("Stallman", createdPerson.getLastName());
        assertEquals("New York City - New York - USA", createdPerson.getAddress());
        assertEquals("Male", createdPerson.getGender());
        assertTrue(createdPerson.getEnabled());
    }

    @Test
    @Order(2)    
    void createWithWrongOrigin() throws JsonProcessingException {
        PersonDTO personWithWrongOrigin = new PersonDTO();
        mockPerson(personWithWrongOrigin);

        RequestSpecification specWithWrongOrigin = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content = given(specWithWrongOrigin)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(personWithWrongOrigin)
            .when()
                .post()
            .then()
                .statusCode(403)
            .extract()
                .body()
                    .asString();
        
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void update() throws JsonProcessingException {
        person.setFirstName("Leonardo");
        person.setLastName("da Vinci");
        person.setAddress("Anchiano - Italy");

        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(person)
            .when()
                .put()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        PersonDTO updatedPerson = objectMapper.readValue(content, PersonDTO.class);
        person = updatedPerson;

        assertNotNull(updatedPerson.getId());
        
        assertEquals(person.getId(), updatedPerson.getId());

        assertEquals("Leonardo", updatedPerson.getFirstName());
        assertEquals("da Vinci", updatedPerson.getLastName());
        assertEquals("Anchiano - Italy", updatedPerson.getAddress());
        assertEquals("Male", updatedPerson.getGender());
        assertTrue(updatedPerson.getEnabled());
    }

    @Test
    @Order(4)
    void findById() throws JsonProcessingException {
        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", person.getId())
                .when()
                    .get("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();
        
        PersonDTO foundPerson = objectMapper.readValue(content, PersonDTO.class);

        assertNotNull(foundPerson.getId());
        
        assertEquals(person.getId(), foundPerson.getId());

        assertEquals("Leonardo", foundPerson.getFirstName());
        assertEquals("da Vinci", foundPerson.getLastName());
        assertEquals("Anchiano - Italy", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertTrue(foundPerson.getEnabled());
    }

    @Test
    @Order(5)
    void disablePerson() throws JsonProcessingException {
        var content = given(specification)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .pathParam("id", person.getId())
                .when()
                    .patch("{id}")
                .then()
                    .statusCode(200)
                .extract()
                    .body()
                        .asString();

        PersonDTO patchedPerson = objectMapper.readValue(content, PersonDTO.class);
        person = patchedPerson;

        assertNotNull(patchedPerson.getId());
        
        assertEquals(person.getId(), patchedPerson.getId());

        assertEquals("Leonardo", patchedPerson.getFirstName());
        assertEquals("da Vinci", patchedPerson.getLastName());
        assertEquals("Anchiano - Italy", patchedPerson.getAddress());
        assertEquals("Male", patchedPerson.getGender());
        assertFalse(patchedPerson.getEnabled());
    }

    @Test
    @Order(6)
    void findPersonByName() throws JsonProcessingException {
        var content = given(specification)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParam("firstName", "Leo")
                .when()
                .get("/findPersonByName")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        PersonDTO foundPerson = people.get(0);

        assertNotNull(foundPerson.getId());

        assertEquals(person.getId(), foundPerson.getId());

        assertEquals("Leonardo", foundPerson.getFirstName());
        assertEquals("da Vinci", foundPerson.getLastName());
        assertEquals("Anchiano - Italy", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertFalse(foundPerson.getEnabled());
    }


    @Test
    @Order(7)
    void findAll() throws JsonProcessingException {
        var content = given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .queryParam("page", 0)
            .queryParam("size", 12)
            .queryParam("direction", "asc")
            .when()
                .get()
            .then()
                .statusCode(200)
            .extract()
                .body()
                    .asString();

        WrapperPersonDTO wrapper = objectMapper.readValue(content, WrapperPersonDTO.class);
        List<PersonDTO> people = wrapper.getEmbedded().getPeople();

        assertNotNull(people);
        assertTrue(people.size() >= 10);

        PersonDTO foundPerson = people.stream()
            .filter(p -> p.getId().equals(person.getId()))
            .findFirst()
            .orElse(null);

        assertNotNull(foundPerson);
        assertEquals(person.getId(), foundPerson.getId());
        assertEquals("Leonardo", foundPerson.getFirstName());
        assertEquals("da Vinci", foundPerson.getLastName());
        assertEquals("Anchiano - Italy", foundPerson.getAddress());
        assertEquals("Male", foundPerson.getGender());
        assertFalse(foundPerson.getEnabled());

        PersonDTO personFive = people.stream()
            .filter(p -> p.getId().equals(5L))
            .findFirst()
            .orElse(null);

        assertNotNull(personFive);
        assertEquals(5L, personFive.getId());
        assertEquals("Mahatma", personFive.getFirstName());
        assertEquals("Gandhi", personFive.getLastName());
        assertEquals("Porbandar - India", personFive.getAddress());
        assertEquals("Male", personFive.getGender());
        assertTrue(personFive.getEnabled());
    }

    @Test
    @Order(8)
    void findAllWithWrongOrigin() throws JsonProcessingException {
        RequestSpecification specWithWrongOrigin = new RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
            .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
            .build();

        var content = given(specWithWrongOrigin)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_SEMERU)
            .queryParam("page", 0)
            .queryParam("size", 12)
            .queryParam("direction", "asc")
            .when()
                .get()
            .then()
                .statusCode(403)
            .extract()
                .body()
                    .asString();

        
        assertNotNull(content);
        assertEquals("Invalid CORS request", content);

    }

    @Test
    @Order(9)
    void delete() throws JsonProcessingException {
        given(specification)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
                .pathParam("id", person.getId())
            .when()
                .delete("{id}")
            .then()
                .statusCode(204);
    }

    private void mockPerson(PersonDTO personDTO) {
        personDTO.setFirstName("Richard");
        personDTO.setLastName("Stallman");
        personDTO.setAddress("New York City - New York - USA");
        personDTO.setGender("Male");
        personDTO.setEnabled(true);
    }
}