package br.com.renatogsilva.my_car.repository.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long>, CarRepositoryQueries {

    @Query(nativeQuery = true, value = "SELECT car.* FROM tb_car car" +
            " WHERE (:id IS NULL OR car.car_id != :id)" +
            " AND car.mark = :mark" +
            " AND car.version = :version" +
            " AND car.engine = :engine")
    public Car findCarDuplicatorByIdAndMarkAndVersionAndEngine(@Param("id") Long id, @Param("mark") String mark,
                                                               @Param("version") String version,
                                                               @Param("engine") String engine);

    @Query(nativeQuery = true, value = "SELECT car.* FROM tb_car car" +
            " WHERE (:status IS NULL OR car.status = :status)" +
            " AND (:exchange IS NULL OR car.exchange = :exchange)")
    public Page<Car> findAllByFilters(@Param("status") String status, @Param("exchange") String exchange, Pageable pageable);
}