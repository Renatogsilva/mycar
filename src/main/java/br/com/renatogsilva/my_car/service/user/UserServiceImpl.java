package br.com.renatogsilva.my_car.service.user;

import br.com.renatogsilva.my_car.model.converters.UserMapper;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.dto.user.UserRequestDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseDTO;
import br.com.renatogsilva.my_car.model.dto.user.UserResponseListDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessageUserExceptions;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
import br.com.renatogsilva.my_car.model.exceptions.user.UserNotFoundException;
import br.com.renatogsilva.my_car.model.validations.PersonBusinessRules;
import br.com.renatogsilva.my_car.model.validations.UserBusinessRules;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import br.com.renatogsilva.my_car.service.person.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PersonService personService;
    private final UserRepository userRepository;
    private final UserBusinessRules userBusinessRules;
    private final PersonBusinessRules personBusinessRules;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        userBusinessRules.validateInclusioRules(userRequestDTO);
        personBusinessRules.validateInclusioRules(userRequestDTO.getPersonRequestDTO());

        User user = userMapper.toUser(userRequestDTO);
        user.setStatus(EnumStatus.ACTIVE);
        user.setCreationDate(LocalDate.now());
        user.setPrimaryAccess(true);
        user.setPassword(bCryptPasswordEncoder.encode(userRequestDTO.getPersonRequestDTO().getCpf()));
        user.setTypeUser(EnumTypeUser.ADMIN);

        user.setPerson(this.personService.create(user.getPerson()));
        this.userRepository.save(user);

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    @Override
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        userBusinessRules.validateUpdateRules(userRequestDTO);
        personBusinessRules.validateUpdateRules(userRequestDTO.getPersonRequestDTO());

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        user = userMapper.toUser(user, userRequestDTO);

        this.personService.update(user.getPerson());
        this.userRepository.save(user);

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO findById(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseListDTO> findAll() {
        List<User> users = this.userRepository.findAll();

        return userMapper.toUserResponseListDTO(users);
    }

    @Transactional
    @Override
    public void enable(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        user.setStatus(EnumStatus.ACTIVE);

        this.userRepository.save(user);
    }

    @Transactional
    @Override
    public void disable(Long id) {
        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        user.setStatus(EnumStatus.INACTIVE);

        this.userRepository.save(user);
    }
}
