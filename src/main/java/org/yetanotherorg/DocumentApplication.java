package org.yetanotherorg;

import java.nio.file.Path;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.yetanotherorg.core.DocumentService;
import org.yetanotherorg.core.exceptions.DocumentServiceExceptionMapper;
import org.yetanotherorg.core.exceptions.IllegalArgumentExceptionMapper;
import org.yetanotherorg.core.files.FileRepositoryLocal;
import org.yetanotherorg.core.metadata.MetaDataRepositoryException;
import org.yetanotherorg.core.metadata.MetaDataRepositoryLocal;
import org.yetanotherorg.health.FilePathHealthCheck;
import org.yetanotherorg.resources.DocumentResource;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class DocumentApplication extends Application<DocumentApplicationConfig> {

    public static void main(final String[] args) throws Exception {
        new DocumentApplication().run(args);
    }

    @Override
    public String getName() {
        return "DocumentApplication";
    }

    @Override
    public void initialize(final Bootstrap<DocumentApplicationConfig> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<DocumentApplicationConfig>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DocumentApplicationConfig configuration) {
                SwaggerBundleConfiguration swagger = new SwaggerBundleConfiguration();
                swagger.setResourcePackage("org.yetanotherorg.resources");
                return swagger;
            }
        });
    }

    @Override
    public void run(final DocumentApplicationConfig config,
            final Environment environment) {
        environment.healthChecks().register("filePath", new FilePathHealthCheck(config.getFilePath()));

        environment.jersey().register(MultiPartFeature.class);

        environment.jersey().register(new DocumentServiceExceptionMapper(environment.metrics()));
        environment.jersey().register(new IllegalArgumentExceptionMapper(environment.metrics()));

        final DocumentResource component = new DocumentResource(createInMemory(config.getFilePath()));
            environment.jersey().register(component);
    }

    private DocumentService createInMemory(final Path filePath) {
        return new DocumentService(
                new FileRepositoryLocal(filePath),
                new MetaDataRepositoryLocal(filePath)
        );
    }

}
