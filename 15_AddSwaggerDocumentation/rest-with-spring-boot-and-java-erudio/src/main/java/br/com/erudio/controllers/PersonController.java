package br.com.erudio.controllers;

import br.com.erudio.controllers.docs.PersonControllerDocs;
import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.services.PersonServices;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for managing people")
public class PersonController implements PersonControllerDocs {

    private final PersonServices service;

    public PersonController(PersonServices service) {
        this.service = service;
    }
    @GetMapping(produces = {
            MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_XML_VALUE,
            MediaType.APPLICATION_YAML_VALUE})
    @Override
    public ResponseEntity<List<PersonDTO>> findAll() {
        List<PersonDTO> people = service.findAll();
        if (people.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(people);
    }
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<PersonDTO> findById(@PathVariable("id") Long id) {
        PersonDTO person = service.findById(id);
        return ResponseEntity.ok(person);
    }
    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )
    @Override
    public ResponseEntity<PersonDTO> create(@RequestBody PersonDTO person) {
        PersonDTO created = service.create(person);
        URI location = URI.create(String.format("/api/person/v1/%s", created.getId()));
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_YAML_VALUE}
    )

    @Override
    public ResponseEntity<PersonDTO> update(@RequestBody PersonDTO person) {
        PersonDTO updated = service.update(person);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/{id}")

    @Override
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
