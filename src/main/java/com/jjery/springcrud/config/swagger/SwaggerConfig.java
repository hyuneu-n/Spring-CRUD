package com.jjery.springcrud.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
  @Value("${server.servlet.context-path:}")
  private String contextPath;

  @Bean
  public OpenAPI customOpenAPI() {
    Server localServer = new Server();
    localServer.setUrl(contextPath);
    localServer.setDescription("Local Server");

    return new OpenAPI()
            .addServersItem(localServer)
            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
            .components(
                    new Components()
                            .addSecuritySchemes(
                                    "bearerAuth",
                                    new SecurityScheme()
                                            .type(SecurityScheme.Type.HTTP)
                                            .scheme("bearer")
                                            .bearerFormat("JWT")))
            .info(
                    new Info()
                            .title("Spring CRUD API")
                            .version("1.0")
                            .description(
                                    "API Test용 ID : testdev Token : eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJ0ZXN0ZGV2Iiwic3ViIjoi7Z6Y7LCsIOy9lOuBvOumrCIsImlhdCI6MTcyNjMzNjk2MywiZXhwIjoxNzI2NDE5NzYzfQ.lBce-P8E9lutlFrfJ41qn5glcJ8ZzWzgkZTE6f5364E "));
  }

  @Bean
  public GroupedOpenApi customGroupedOpenApi() {
    return GroupedOpenApi.builder().group("api").pathsToMatch("/**").build();
  }
}
