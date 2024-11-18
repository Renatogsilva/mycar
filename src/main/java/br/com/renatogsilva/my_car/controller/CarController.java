package br.com.renatogsilva.my_car.controller;

import br.com.renatogsilva.my_car.model.dto.CarDTO;
import br.com.renatogsilva.my_car.service.car.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public CarDTO create(@RequestBody CarDTO carDTO) {
        return this.carService.create(carDTO);
    }

    @PutMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO update(@PathVariable Long id, @RequestBody CarDTO carDTO) {
        return this.carService.update(carDTO, id);
    }

    @GetMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CarDTO findById(@PathVariable Long id) {
        return this.carService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CarDTO> findAll() {
        return this.carService.findAll();
    }

    @DeleteMapping("/active/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        this.carService.enable(id);
    }

    @DeleteMapping("/desactive/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable Long id) {
        this.carService.disable(id);
    }
}
