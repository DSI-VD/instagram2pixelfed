package ch.vd.tools.ig2fedi;

import ch.vd.tools.ig2fedi.service.CrossPostingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@EnableCaching
@SpringBootApplication
public class Ig2fediApplication implements CommandLineRunner {

    public static void main(String[] args) {
        log.info("Starting instagram2pixelfed runner");
        SpringApplication.run(Ig2fediApplication.class, args);
    }

    private final CrossPostingService crossPostingService;

    public Ig2fediApplication(CrossPostingService crossPostingService) {
        this.crossPostingService = crossPostingService;
    }

    @Override
    public void run(String... args) {
        crossPostingService.crossPost();
    }
}
