package org.yetanotherorg.resources;

import static java.util.stream.Collectors.toList;

import java.io.InputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.yetanotherorg.api.Document;
import org.yetanotherorg.api.Documents;
import org.yetanotherorg.core.DocumentService;
import org.yetanotherorg.core.entity.FileWrapper;

import com.codahale.metrics.annotation.Timed;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/api/documents")
@Produces(MediaType.APPLICATION_JSON)
@Api("Documents")
public class DocumentResource {

    private final DocumentService service;

    public DocumentResource(final DocumentService service) {
        this.service = service;
    }

    @GET
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "get documents", notes = "Returns a list of documents. Accepts an optional query parameter to filter by one category")
    @ApiResponses(value = { @ApiResponse(code = 200, response = Documents.class, message = "a (possibly empty) list of documents") })
    public Documents get(@ApiParam @QueryParam("category") final String category) {
        return new Documents(service.find(category).collect(toList()));
    }

    @GET
    @Timed
    @Path("/{fileId}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @ApiOperation(value = "get/download document by id", notes = "Downloads a document by a given document id. The name of the downloaded document is the one given when uploaded.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = byte.class, message = "a octet stream document"),
            @ApiResponse(code = 404, message = "File associated with id not found") })
    public Response getById(@ApiParam @PathParam("fileId") final String fileId) {
        final FileWrapper fileW = service.findFileById(fileId);
        final Response.ResponseBuilder responseBuilder = Response.ok(fileW.getFile());
        responseBuilder.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileW.getOriginalFileName());
        return responseBuilder.build();
    }

    @POST
    @Timed
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "upload a file with categories", notes = "File upload. Both file and categories are given as form data params.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, response = Document.class, message = "a octet stream document"),
            @ApiResponse(code = 400, message = "File missing or invalid"),
            @ApiResponse(code = 400, message = "Categories missing or invalid"), })
    public Document uploadFile(@ApiParam @FormDataParam("file") InputStream inputStream,
            @ApiParam @FormDataParam("file") FormDataContentDisposition fileDetail,
            @ApiParam @FormDataParam("categories") String categories) {
        if(fileDetail==null){
            throw new IllegalArgumentException("Input file missing");
        }
        if(StringUtils.isEmpty(categories) || StringUtils.isEmpty(categories.trim())){
            throw new IllegalArgumentException("Categories are missing");
        }
        return service.insert(
                inputStream,
                fileDetail.getFileName(),
                categories.trim());
    }

}
