package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageExceptions;
import br.com.renatogsilva.my_car.model.exceptions.ObjectDuplicationException;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarValidations {

    private final CarRepository carRepository;

    private void verifyCarDuplicationityCarForSameMark(Long carId, String mark, String version, String engine) {
        if (this.carRepository.findCarDuplication(carId, "%" + mark + "%", "%" + version + "%",
                "%" + engine + "%") != null) {
            throw new ObjectDuplicationException(EnumMessageExceptions.CAR_DUPLICATE.getMessage(),
                    EnumMessageExceptions.CAR_DUPLICATE.getCode());
        }
    }

    public void checkRegistration(CarRequestDTO carRequestDTO) {
        verifyCarDuplicationityCarForSameMark(carRequestDTO.getId(), carRequestDTO.getMark(),
                carRequestDTO.getVersion(), carRequestDTO.getEngine());
    }

    public void checkUpdate(CarRequestDTO carRequestDTO) {
        verifyCarDuplicationityCarForSameMark(carRequestDTO.getId(), carRequestDTO.getMark(),
                carRequestDTO.getVersion(), carRequestDTO.getEngine());
    }
}
