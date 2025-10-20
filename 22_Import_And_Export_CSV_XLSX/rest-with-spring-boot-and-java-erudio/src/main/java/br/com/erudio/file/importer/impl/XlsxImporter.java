package br.com.erudio.file.importer.impl;

import br.com.erudio.exception.BadRequestException;
import br.com.erudio.data.dto.PersonDTO;
import br.com.erudio.file.importer.contract.FileImporter;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class XlsxImporter implements FileImporter {

    @Override
    public List<PersonDTO> importFile(InputStream inputStream) {
        List<PersonDTO> people = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();
            DataFormatter dataFormatter = new DataFormatter();

            // Skip the header row
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
 
                String firstName = dataFormatter.formatCellValue(row.getCell(0));
                if (firstName.isBlank()) {
                    continue;
                }
 
                PersonDTO person = new PersonDTO();
                person.setFirstName(firstName);
                person.setLastName(dataFormatter.formatCellValue(row.getCell(1)));
                person.setAddress(dataFormatter.formatCellValue(row.getCell(2)));
                person.setGender(dataFormatter.formatCellValue(row.getCell(3)));
                person.setEnabled(true);
                people.add(person);
            }
        } catch (Exception e) {
            throw new BadRequestException("Error processing XLSX file: " + e.getMessage());
        }
        return people;
    }

    @Override
    public boolean canImport(String filename) {
        return filename != null && filename.toLowerCase().endsWith(".xlsx");
    }
}
