package br.com.erudio.controllers.docs;

import br.com.erudio.data.dto.UploadFileResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "File", description = "Endpoints for Managing Files")
public interface FileControllerDocs {

    @Operation(summary = "Upload a file",
            description = "Uploads a file to the server",
            tags = {"File"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UploadFileResponseDTO.class)
                            )
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    UploadFileResponseDTO uploadFile(@RequestParam("file") MultipartFile file);

    @Operation(summary = "Upload multiple files",
            description = "Uploads multiple files to the server",
            tags = {"File"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    array = @ArraySchema(schema = @Schema(implementation = UploadFileResponseDTO.class))
                            )
                    ),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)
            }
    )
    List<UploadFileResponseDTO> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files);

    @Operation(summary = "Download a file", description = "Downloads a file from the server", tags = {"File"}, responses = {@ApiResponse(description = "Success", responseCode = "200", content = @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)), @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content), @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content), @ApiResponse(description = "Not Found", responseCode = "404", content = @Content), @ApiResponse(description = "Internal Server Error", responseCode = "500", content = @Content)})
    ResponseEntity<Resource> downloadFile(@PathVariable String filename, HttpServletRequest request);
}
