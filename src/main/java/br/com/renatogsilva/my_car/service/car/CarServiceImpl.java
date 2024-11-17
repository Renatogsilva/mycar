package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.dto.CarDTO;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService{

    private final CarRepository carRepository;

    @Override
    public CarDTO create(CarDTO carDTO) {

        return null;
    }

    @Override
    public CarDTO update(CarDTO carDTO) {
        return null;
    }

    @Override
    public CarDTO disable(CarDTO carDTO) {
        return null;
    }

    @Override
    public CarDTO enable(CarDTO carDTO) {
        return null;
    }

    @Override
    public List<CarDTO> findAll() {
        return List.of();
    }

    @Override
    public CarDTO findById(Long id) {
        return null;
    }
}