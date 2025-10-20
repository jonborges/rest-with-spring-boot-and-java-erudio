package br.com.erudio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Objects;

@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageConfig {


    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public FileStorageConfig() {
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }


    public FileStorageConfig(String uploadDir) {
        this.uploadDir = uploadDir;
    }

    public FileStorageConfig uploadDir(String uploadDir) {
        setUploadDir(uploadDir);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof FileStorageConfig)) {
            return false;
        }
        FileStorageConfig fileStorageConfig = (FileStorageConfig) o;
        return Objects.equals(uploadDir, fileStorageConfig.uploadDir);
    }

    @Override
    public int hashCode() { 
        return Objects.hash(uploadDir);
    }

    @Override
    public String toString() {
        return "{" +
            " uploadDir='" + getUploadDir() + "'" +
            "}";
    }
    
}
