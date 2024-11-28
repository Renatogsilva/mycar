package br.com.renatogsilva.my_car.service.user;

import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;

import java.util.List;

public interface UserService {

    UserResponseDTO create(UserRequestDTO userRequestDTO);
    UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);
    void update(Long id, UserProfileRequestDTO userProfileRequestDTO);
    UserResponseDTO findById(Long id);
    List<UserResponseListDTO> findAll();
    void disable(Long id);
    void enable(Long id);
}
