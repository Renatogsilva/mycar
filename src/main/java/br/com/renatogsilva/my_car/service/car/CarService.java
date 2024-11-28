package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;

import java.util.List;

public interface CarService {

    CarResponseDTO create(CarRequestDTO carRequestDTO);
    CarResponseDTO update(CarRequestDTO carRequestDTO, Long id);
    void disable(Long id);
    void enable(Long id);
    List<CarResponseListDTO> findAll();
    CarResponseDTO findById(Long id);
}
