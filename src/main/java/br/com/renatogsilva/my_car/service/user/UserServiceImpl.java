package br.com.renatogsilva.my_car.service.user;

import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Override
    public UserRequestDTO create(UserRequestDTO userRequestDTO) {
        return null;
    }

    @Override
    public UserRequestDTO update(Long id, UserRequestDTO userRequestDTO) {
        return null;
    }

    @Override
    public UserResponseDTO get(Long id) {
        return null;
    }

    @Override
    public List<UserResponseListDTO> getAll() {
        return List.of();
    }

    @Override
    public void disable(Long id) {

    }

    @Override
    public void enable(Long id) {

    }
}
