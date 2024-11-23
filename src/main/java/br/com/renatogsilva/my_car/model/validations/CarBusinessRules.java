package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageCarExceptions;
import br.com.renatogsilva.my_car.model.exceptions.car.CarDuplicationException;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarBusinessRules {

    private final CarRepository carRepository;

    private void verifyCarDuplicationityCarForSameMark(Long carId, String mark, String version, String engine) {
        if (this.carRepository.findCarDuplicatorByIdAndMarkAndVersionAndEngine(carId, mark, version, engine) != null) {
            throw new CarDuplicationException(EnumMessageCarExceptions.CAR_DUPLICATE.getMessage(),
                    EnumMessageCarExceptions.CAR_DUPLICATE.getCode());
        }
    }

    public void checkRegistration(CarRequestDTO carRequestDTO) {
        verifyCarDuplicationityCarForSameMark(carRequestDTO.getCarId(), carRequestDTO.getMark(),
                carRequestDTO.getVersion(), carRequestDTO.getEngine());
    }

    public void checkUpdate(CarRequestDTO carRequestDTO) {
        verifyCarDuplicationityCarForSameMark(carRequestDTO.getCarId(), carRequestDTO.getMark(),
                carRequestDTO.getVersion(), carRequestDTO.getEngine());
    }
}
