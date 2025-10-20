package br.com.erudio.file.importer.factory;

import br.com.erudio.exception.BadRequestException;
import br.com.erudio.file.importer.contract.FileImporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FileImporterFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileImporterFactory.class);

    @Autowired
    private List<FileImporter> importers;

    public FileImporter getImporter(String filename) {
        return importers.stream()
            .filter(importer -> importer.canImport(filename))
            .findFirst()
            .orElseThrow(() -> new BadRequestException("Unsupported file extension for: " + filename));
    }
}
