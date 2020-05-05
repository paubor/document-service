package org.yetanotherorg.core.exceptions;

import static com.codahale.metrics.MetricRegistry.name;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.yetanotherorg.api.RestError;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

public class DocumentServiceExceptionMapper implements ExceptionMapper<DocumentServiceException> {
    private final Meter meter;

    public DocumentServiceExceptionMapper(final MetricRegistry metrics) {
        this.meter = metrics.meter(name(getClass(), "exceptions"));
    }

    @Override public Response toResponse(final DocumentServiceException e) {
        meter.mark();
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new RestError(e.getMessage()))
                .type(MediaType.APPLICATION_JSON_TYPE)
                .build();
    }
}
