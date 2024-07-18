package kz.dara.test.qs.beans;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;

@Configuration
public class FileBean {
    @Bean(name = "resourceLoaderBean")
    public ResourceLoader resourceLoader() {
        return new DefaultResourceLoader();
    }
}
