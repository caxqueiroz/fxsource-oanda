package io.cax.fx.ingester;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FxSourceApp implements CommandLineRunner{

	@Autowired
	TickSourceOanda source;

	public static void main(String[] args) {
		SpringApplication.run(FxSourceApp.class, args);
	}

	@Override
	public void run(String... strings) throws Exception {
		source.start();
	}
}
