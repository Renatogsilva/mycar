package br.com.renatogsilva.my_car;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MyCarApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyCarApplication.class, args);

		System.out.println("PG_URL: " + System.getenv("PG_URL"));
		System.out.println("PG_USERNAME: " + System.getenv("PG_USERNAME"));
		System.out.println("PG_PASSWORD: " + System.getenv("PG_PASSWORD"));
	}
}