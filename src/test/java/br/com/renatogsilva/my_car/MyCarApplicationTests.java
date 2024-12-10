package br.com.renatogsilva.my_car;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class MyCarApplicationTests {

	@Test
	void contextLoads() {
	}

}
