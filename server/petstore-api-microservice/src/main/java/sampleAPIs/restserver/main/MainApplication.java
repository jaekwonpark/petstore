package sampleAPIs.restserver.main;

import com.github.krr.devplatform.messages.context.impl.FilesystemNamespaceResourcesLoader;
import com.github.krr.devplatform.messages.loader.AppMessages;
import com.github.krr.devplatform.messages.loader.NamespaceResourcesLoader;
import com.nutanix.api.request.aspect.ValidateRequestAndResponseAspect;
import com.nutanix.api.validation.validator.impl.OpenApiSpecificationBasedValidator;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import sampleAPIs.restserver.config.CustomWebSecurityConfigurerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import sampleAPIs.restserver.config.ApplicationConfig;
import sampleAPIs.restserver.controllers.PetServiceController;
import sampleAPIs.restserver.repositories.PetRepository;
import sampleAPIs.restserver.services.impl.PetServiceImpl;

import java.io.IOException;


@Slf4j
@SpringBootApplication(scanBasePackageClasses = {
    CustomWebSecurityConfigurerAdapter.class,
    PetServiceImpl.class,
    //PetRepository.class,
    PetServiceController.class
},
    exclude = {
})

@EnableMongoRepositories(basePackageClasses = PetRepository.class)

public class MainApplication {

  @Autowired
  private ApplicationConfig appConfig;

  public static void main(String[] args) {
    SpringApplication.run(MainApplication.class, args);
  }

  @Configuration
  @EnableWebMvc
  @EnableAspectJAutoProxy(proxyTargetClass = true)
  @Import({
      ValidateRequestAndResponseAspect.class,
      OpenApiSpecificationBasedValidator.class
  })

  public class MainApplicationConfiguration {

    @Bean
    public AppMessages appMessages() {
      return new AppMessages(namespaceResourcesLoader());
    }

    @Bean
    public NamespaceResourcesLoader namespaceResourcesLoader() {
      try {
        return new FilesystemNamespaceResourcesLoader(appConfig.getBasePathForYamls());
      }
      catch (IOException e) {
        log.error("Could not find {}", appConfig.getBasePathForYamls(), e);
        throw new IllegalStateException(e);
      }
    }
  }
}
