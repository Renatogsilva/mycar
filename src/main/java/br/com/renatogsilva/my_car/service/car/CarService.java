package br.com.renatogsilva.my_car.service.car;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseDTO;
import br.com.renatogsilva.my_car.model.dto.car.CarResponseListDTO;
import br.com.renatogsilva.my_car.model.enums.EnumExchange;
import br.com.renatogsilva.my_car.model.enums.EnumStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CarService {

    CarResponseDTO create(CarRequestDTO carRequestDTO);
    CarResponseDTO update(CarRequestDTO carRequestDTO, Long id);
    void disable(Long id);
    void enable(Long id);
    Page<CarResponseListDTO> findAllByFilters(EnumStatus status, EnumExchange exchange, Pageable pageable);
    CarResponseDTO findById(Long id);
}