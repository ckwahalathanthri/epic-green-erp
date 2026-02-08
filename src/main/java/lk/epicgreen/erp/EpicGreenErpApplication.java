package lk.epicgreen.erp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "lk.epicgreen.erp")
@EnableJpaRepositories(basePackages = "lk.epicgreen.erp")
@EntityScan(basePackages = "lk.epicgreen.erp")
public class EpicGreenErpApplication {

	public static void main(String[] args) {
		SpringApplication.run(EpicGreenErpApplication.class, args);
	}

}
