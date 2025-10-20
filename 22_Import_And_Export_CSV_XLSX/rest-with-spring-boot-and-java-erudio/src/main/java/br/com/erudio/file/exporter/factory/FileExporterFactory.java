package br.com.erudio.file.exporter.factory;

import br.com.erudio.exception.BadRequestException;
import br.com.erudio.file.exporter.contract.FileExporter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class FileExporterFactory {

    private static final Logger logger = LoggerFactory.getLogger(FileExporterFactory.class);

    @Autowired
    private List<FileExporter> exporters;

    private Map<String, FileExporter> exporterCache;

    @PostConstruct
    public void init() {
        exporterCache = exporters.stream().collect(Collectors.toMap(FileExporter::getMediaType, Function.identity()));
        logger.info("Initialized FileExporterFactory with exporters: {}", exporterCache.keySet());
    }

    public FileExporter getExporter(String mediaType) {
        return Optional.ofNullable(exporterCache.get(mediaType))
            .orElseThrow(() -> new BadRequestException("Invalid File Format: " + mediaType));
    }
}