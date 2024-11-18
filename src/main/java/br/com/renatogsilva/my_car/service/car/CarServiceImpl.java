package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.domain.Car;
import br.com.renatogsilva.my_car.model.dto.CarDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService{

    private final CarRepository carRepository;

    @Override
    public CarDTO create(CarDTO carDTO) {
        Car car = new Car(carDTO);
        car.setCreationDate(LocalDate.now());
        car.setStatus(EnumStatus.ACTIVE);

        this.carRepository.save(car);

        return new CarDTO(car);
    }

    @Override
    public CarDTO update(CarDTO carDTO, Long id) {
        Car car = this.carRepository.findById(id).get();
        car.setEngine(carDTO.getEngine());
        car.setColor(carDTO.getColor());
        car.setExchange(carDTO.getExchange());
        car.setMark(carDTO.getMark());
        car.setVersion(carDTO.getVersion());
        car.setBodyStyle(carDTO.getBodyStyle());
        car.setYearOfManufacture(carDTO.getYearOfManufacture());

        this.carRepository.save(car);
        return new CarDTO(car);
    }

    @Override
    public void disable(Long id) {
        Car car = this.carRepository.findById(id).get();
        car.setStatus(EnumStatus.INACTIVE);
        car.setExclusionDate(LocalDate.now());
        this.carRepository.save(car);
    }

    @Override
    public void enable(Long id) {
        Car car = this.carRepository.findById(id).get();
        car.setStatus(EnumStatus.ACTIVE);
        car.setExclusionDate(null);
        this.carRepository.save(car);
    }

    @Override
    public List<CarDTO> findAll() {
        List<Car> cars = this.carRepository.findAll();
        List<CarDTO> carDTOs = new ArrayList<>();
        for (Car car : cars) {
            carDTOs.add(new CarDTO(car));
        }
        return carDTOs;
    }

    @Override
    public CarDTO findById(Long id) {
        Car car = this.carRepository.findById(id).get();

        return new CarDTO(car);
    }
}