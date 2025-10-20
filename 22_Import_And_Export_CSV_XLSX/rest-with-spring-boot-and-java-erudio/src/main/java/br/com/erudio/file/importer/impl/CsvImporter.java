package br.com.erudio.file.importer.impl;

import br.com.erudio.exception.BadRequestException;
import br.com.erudio.data.dto.PersonDTO;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import br.com.erudio.file.importer.contract.FileImporter;
import org.springframework.stereotype.Component;

@Component
public class CsvImporter implements FileImporter {
    @Override
    public List<PersonDTO> importFile(InputStream inputStream) {
        try {
            CSVFormat format = CSVFormat.Builder.create(CSVFormat.DEFAULT)
                    .setHeader()
                    .setSkipHeaderRecord(true)
                    .setIgnoreHeaderCase(true)
                    .setTrim(true)
                    .build();

            Iterable<CSVRecord> records = format.parse(new InputStreamReader(inputStream));
            return parseRecordsToPersonDTOs(records);
        } catch (Exception e) {
            throw new BadRequestException("Error processing CSV file: " + e.getMessage());
        }
    }

    private List<PersonDTO> parseRecordsToPersonDTOs(Iterable<CSVRecord> records) {
        List<PersonDTO> people = new ArrayList<>();
        for (CSVRecord record : records) {
            PersonDTO person = new PersonDTO();
            person.setFirstName(record.get("first_name"));
            person.setLastName(record.get("last_name"));
            person.setAddress(record.get("address"));
            person.setGender(record.get("gender"));
            person.setEnabled(true);
            people.add(person);
        }
        return people;
    }

    @Override
    public boolean canImport(String filename) {
        return filename != null && filename.toLowerCase().endsWith(".csv");
    }
}
