package br.com.erudio.integrationtests.controllers.withjson;
import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.erudio.config.TestConfigs;
import br.com.erudio.data.dto.PersonDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
// import io.swagger.v3.oas.models.media.MediaType;
import org.springframework.http.MediaType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PersonControllerTest {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonDTO person;
    
    @BeforeEach
    void setUp() {
        // Setup inicial para os testes
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        person = new PersonDTO();
        
    }
     @Test
    void testCreate() {
        // Teste para o método create
        mockPerson();
        specification = new RequestSpecBuilder()
                    .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.ORIGIN_ERUDIO)
                    .setBasePath("/api/person/v1")
                    .setPort(TestConfigs.SERVER_PORT)
                        .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                        .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                        .build(); 

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

        try {
            objectMapper.readValue(content, PersonDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize response", e);
        }
		assertTrue(content.contains("Swagger UI"));  
        
    }

    private void mockPerson() {
        person.setFirstName("Richard"); 
        person.setLastName("Stallman");
        person.setAddress("New York City, New York, US");
        person.setGender("Male");
    }
    @Test
    void testFindById() {
        // Teste para o método findById
    }

   

    @Test
    void testUpdate() {
        // Teste para o método update
    }

    @Test
    void testDelete() {
        // Teste para o método delete
    }

    @Test
    void testFindAll() {
        // Teste para o método findAll
    }

}