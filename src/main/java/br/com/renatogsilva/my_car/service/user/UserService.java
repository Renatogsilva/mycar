package br.com.renatogsilva.my_car.service.user;

import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;

import java.util.List;

public interface UserService {

    public UserRequestDTO create(UserRequestDTO userRequestDTO);
    public UserRequestDTO update(Long id, UserRequestDTO userRequestDTO);
    public UserResponseDTO get(Long id);
    public List<UserResponseListDTO> getAll();
    public void disable(Long id);
    public void enable(Long id);
}
