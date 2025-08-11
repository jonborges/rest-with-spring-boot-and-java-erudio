package br.com.erudio.mapper.custom;

import br.com.erudio.data.dto.v2.PersonDTOV2;
import br.com.erudio.model.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonDTOV2 convertEntityToDTO(Person person) {
        PersonDTOV2 dto = new PersonDTOV2();
        dto.setId(person.getId());
        dto.setLastName(person.getLastName());
        dto.setAddress(person.getAddress());
        dto.setGender(person.getGender());
        dto.setFirstName(person.getFirstName());
        dto.setBirthday(new Date());
        return dto;
    }

    public Person convertDTOtoEntity(PersonDTOV2 person) {
        Person entity = new Person();
        entity.setId(person.getId());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());
        entity.setFirstName(person.getFirstName());
        // entity.setBirthday(new Date());
        return entity;
    }
}
