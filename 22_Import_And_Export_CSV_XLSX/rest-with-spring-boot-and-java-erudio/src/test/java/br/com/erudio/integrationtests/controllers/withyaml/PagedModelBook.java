package br.com.erudio.integrationtests.dto.wrapper.yaml;

import br.com.erudio.integrationtests.dto.BookDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class PagedModelBook {

    @JsonProperty("content")
    private List<BookDTO> content;

    public PagedModelBook() {
    }

    public List<BookDTO> getContent() {
        return content;
    }
}