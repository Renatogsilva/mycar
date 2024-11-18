package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.dto.CarDTO;

import java.util.List;

public interface CarService {

    public CarDTO create(CarDTO carDTO);
    public CarDTO update(CarDTO carDTO, Long id);
    public void disable(Long id);
    public void enable(Long id);
    public List<CarDTO> findAll();
    public CarDTO findById(Long id);
}
