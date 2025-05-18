package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.car.CarRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageCarExceptions;
import br.com.renatogsilva.my_car.model.exceptions.car.CarDuplicationException;
import br.com.renatogsilva.my_car.repository.car.CarRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CarBusinessRules {

    private static Logger logger = LoggerFactory.getLogger(CarBusinessRules.class);

    private final CarRepository carRepository;

    private void verifyCarDuplicationByIdAndMarkAndVersionAndEngine(Long carId, String mark, String version, String engine) {
        logger.info("m: verifyCarDuplicationByIdAndMarkAndVersionAndEngine - Verify car duplication by id {} " +
                "and mar {} and version {} and engine {}", carId, mark, version, engine);

        if (this.carRepository.findCarDuplicatorByIdAndMarkAndVersionAndEngine(carId, mark, version, engine) != null) {
            logger.error("There is a car registered with this data");

            throw new CarDuplicationException(EnumMessageCarExceptions.CAR_DUPLICATE.getMessage(),
                    EnumMessageCarExceptions.CAR_DUPLICATE.getCode());
        }
    }

    public void validateInclusionRules(CarRequestDTO carRequestDTO) {
        verifyCarDuplicationByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(), carRequestDTO.getMark(),
                carRequestDTO.getVersion(), carRequestDTO.getEngine());
    }

    public void validateUpdateRules(CarRequestDTO carRequestDTO) {
        verifyCarDuplicationByIdAndMarkAndVersionAndEngine(carRequestDTO.getCarId(), carRequestDTO.getMark(),
                carRequestDTO.getVersion(), carRequestDTO.getEngine());
    }
}
