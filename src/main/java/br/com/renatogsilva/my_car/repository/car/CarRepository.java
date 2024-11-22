package br.com.renatogsilva.my_car.repository.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CarRepository extends JpaRepository<Car, Long>, CarRepositoryQueries {

    @Query(nativeQuery = true, value = "SELECT *FROM tb_car as car" +
            " WHERE (:id IS NULL OR car.car_id != :id)" +
            " AND car.mark = :mark" +
            " AND car.version = :version" +
            " AND car.engine = :engine")
    public Car findCarDuplicatorByIdAndMarkAndVersionAndEngine(@Param("id") Long id, @Param("mark") String mark,
                                                               @Param("version") String version,
                                                               @Param("engine") String engine);
}
