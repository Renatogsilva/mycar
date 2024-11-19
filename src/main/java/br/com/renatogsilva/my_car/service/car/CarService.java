package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;

import java.util.List;

public interface CarService {

    public CarResponseDTO create(CarRequestDTO carRequestDTO);
    public CarResponseDTO update(CarRequestDTO carRequestDTO, Long id);
    public void disable(Long id);
    public void enable(Long id);
    public List<CarResponseListDTO> findAll();
    public CarResponseDTO findById(Long id);
}
