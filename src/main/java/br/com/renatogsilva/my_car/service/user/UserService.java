package br.com.renatogsilva.my_car.service.user;

import br.com.renatogsilva.my_car.model.dto.user.UserProfileRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.model.enums.EnumStatus;
import br.com.renatogsilva.my_car.model.enums.EnumTypeUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    UserResponseDTO create(UserRequestDTO userRequestDTO);
    UserResponseDTO update(Long id, UserRequestDTO userRequestDTO);
    void update(Long id, UserProfileRequestDTO userProfileRequestDTO);
    UserResponseDTO findById(Long id);
    Page<UserResponseListDTO> findAllByFilters(EnumStatus status, EnumTypeUser typeUser, Pageable pageable);
    void disable(Long id);
    void enable(Long id);
}
