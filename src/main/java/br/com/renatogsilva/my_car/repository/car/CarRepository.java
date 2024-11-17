package br.com.renatogsilva.my_car.repository.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long>, CarRepositoryQueries {
}
