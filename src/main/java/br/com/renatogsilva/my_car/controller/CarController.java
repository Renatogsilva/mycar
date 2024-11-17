package br.com.renatogsilva.my_car.controller;

import br.com.renatogsilva.my_car.model.dto.CarDTO;
import br.com.renatogsilva.my_car.service.car.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/v1/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CarDTO create(@RequestBody CarDTO carDTO) {
        return carService.create(carDTO);
    }

    @GetMapping(value = "/{id}")
    public CarDTO findById(@PathVariable Long id) {
        return this.carService.findById(id);
    }
}
