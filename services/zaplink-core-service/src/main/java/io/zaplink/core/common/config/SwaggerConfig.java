package io.zaplink.core.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig
{
        @Bean
        public OpenAPI zaplinkOpenAPI()
        {
                return new OpenAPI().info( new Info().title( "Zaplink Core Service API" )
                                .description( "Core service for handling URL shortening, QR code generation, and related operations." )
                                .version( "1.0.0" )
                                .contact( new Contact().name( "Zaplink Team" ).email( "support@zaplink.io" ) )
                                .license( new License().name( "MIT" ).url( "https://opensource.org/licenses/MIT" ) ) )
                                .servers( List.of( new Server().url( "http://localhost:8081" )
                                                .description( "Development Server" ) ) );
        }
}
