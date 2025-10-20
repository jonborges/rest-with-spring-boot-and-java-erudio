package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.exception.BadRequestException;
import br.com.erudio.exception.FileStorageException;
import br.com.erudio.exception.RequiredObjectIsNullException;
import br.com.erudio.exception.ResourceNotFoundException;
import static br.com.erudio.mapper.ObjectMapper.parseObject;
import static br.com.erudio.mapper.ObjectMapper.parseListObjects;
import br.com.erudio.file.importer.contract.FileImporter;
import br.com.erudio.file.exporter.contract.FileExporter;
import br.com.erudio.file.exporter.factory.FileExporterFactory;
import br.com.erudio.file.importer.factory.FileImporterFactory;
import br.com.erudio.model.Person;
import br.com.erudio.repository.PersonRepository;
import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.Pageable;
import org.springframework.core.io.Resource;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class PersonServices {

    private Logger logger = LoggerFactory.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    FileImporterFactory importer;

    @Autowired
    FileExporterFactory exporterFactory;

    @Autowired
    PagedResourcesAssembler<PersonDTO> assembler;


    public PagedModel<EntityModel<PersonDTO>> findAll(Pageable pageable) {

        logger.info("Finding all People!");

        var people = repository.findAll(pageable);

        var peopleWithLinks = people.map(this::convertToDtoWithLinks); 

        Link findAllLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
            .findAll(pageable.getPageNumber(), pageable.getPageSize(), "asc", "firstName"))
            .withSelfRel();
       return assembler.toModel(peopleWithLinks, findAllLink);
    }

    /**
     * @param firstName
     * @param pageable
     * @return
     */
    public PagedModel<EntityModel<PersonDTO>> findPeopleByName(String firstName, Pageable pageable) {

        logger.info("Finding people by name!");

        var people = repository.findPeopleByName(firstName, pageable);

        var peopleWithLinks = people.map(this::convertToDtoWithLinks);

        Link findByNameLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(PersonController.class)
                        .findPersonByName(firstName, pageable.getPageNumber(), pageable.getPageSize(), "asc", "firstName"))
                .withSelfRel();
        return assembler.toModel(peopleWithLinks, findByNameLink);
    }

    public PersonDTO findById(Long id) {
        logger.info("Finding one Person!");

        var entity = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        return convertToDtoWithLinks(entity);
    }

    public PersonDTO create(PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Creating one Person!");
        var entity = parseObject(person, Person.class);

        return convertToDtoWithLinks(repository.save(entity));
    }

    @Transactional
    public List<PersonDTO> massCreation(MultipartFile file) {
        logger.info("Importing People from file!");

        if (file == null || file.isEmpty()) {
            throw new BadRequestException("The file is empty or null!");
        }

        try (InputStream inputStream = file.getInputStream()) {
            String filename = Optional.ofNullable(file.getOriginalFilename())
                .orElseThrow(() -> new BadRequestException("File name cannot be null or empty!"));

            FileImporter fileImporter = this.importer.getImporter(filename);
            List<PersonDTO> personDTOs = fileImporter.importFile(inputStream);
            List<Person> entitiesToSave = parseListObjects(personDTOs, Person.class);
            List<Person> savedEntities = repository.saveAll(entitiesToSave);

            return savedEntities.stream().map(this::convertToDtoWithLinks).toList();
        } catch (Exception e) {
            throw new FileStorageException("Error processing the file: " + e.getMessage(), e);
        }
    }

    public Resource exportPeopleToSheet(String mediaType) throws Exception {
        logger.info("Exporting people to a sheet");

        List<Person> people = repository.findAll();
        List<PersonDTO> personDTOs = parseListObjects(people, PersonDTO.class);

        FileExporter exporter = exporterFactory.getExporter(mediaType);
        return exporter.exportFile(personDTOs);
    }

    public PersonDTO update(PersonDTO person) {

        if (person == null) throw new RequiredObjectIsNullException();

        logger.info("Updating one Person!");
        Person entity = repository.findById(person.getId())
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        return convertToDtoWithLinks(repository.save(entity));
    }

    @Transactional
    public PersonDTO disablePerson(Long id) {

        logger.info("Disabling one Person!");


        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        entity.setEnabled(false);
        
        return convertToDtoWithLinks(repository.save(entity));
    }

    public void delete(Long id) {

        logger.info("Deleting one Person!");

        Person entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }

    private PersonDTO convertToDtoWithLinks(Person entity) {
        var dto = parseObject(entity, PersonDTO.class);
        addHateoasLinks(dto);
        return dto;
    }

    private void addHateoasLinks(PersonDTO dto) {
        dto.add(linkTo(methodOn(PersonController.class).findById(dto.getId())).withSelfRel().withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findAll(0, 12, "asc", "firstName")).withRel("findAll").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).findPersonByName("", 0, 12, "asc", "firstName")).withRel("findByName").withType("GET"));
        dto.add(linkTo(methodOn(PersonController.class).create(dto)).withRel("create").withType("POST"));
        dto.add(linkTo(methodOn(PersonController.class).update(dto)).withRel("update").withType("PUT"));
        dto.add(linkTo(methodOn(PersonController.class).disablePerson(dto.getId())).withRel("disable").withType("PATCH"));
        dto.add(linkTo(methodOn(PersonController.class).delete(dto.getId())).withRel("delete").withType("DELETE"));
    }
}