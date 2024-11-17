package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.dto.CarDTO;

import java.util.List;

public interface CarService {

    public CarDTO create(CarDTO carDTO);
    public CarDTO update(CarDTO carDTO);
    public CarDTO disable(CarDTO carDTO);
    public CarDTO enable(CarDTO carDTO);
    public List<CarDTO> findAll();
    public CarDTO findById(Long id);
}
