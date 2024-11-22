package br.com.renatogsilva.my_car.api.controller;

import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDTO create(@RequestBody UserRequestDTO userRequestDTO) {
        return this.userService.create(userRequestDTO);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO update(@PathVariable Long id, @RequestBody UserRequestDTO userRequestDTO) {
        return this.userService.update(id, userRequestDTO);
    }

    @DeleteMapping("/active/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void enable(@PathVariable Long id) {
        this.userService.enable(id);
    }

    @DeleteMapping("/desactive/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable Long id) {
        this.userService.disable(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserResponseDTO findById(@PathVariable long id) {
        return this.userService.findById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserResponseListDTO> findAll() {
        return this.userService.findAll();
    }
}