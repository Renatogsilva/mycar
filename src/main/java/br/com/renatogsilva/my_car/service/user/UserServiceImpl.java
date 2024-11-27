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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final PersonService personService;
    private final UserRepository userRepository;
    private final UserBusinessRules userBusinessRules;
    private final PersonBusinessRules personBusinessRules;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final UserMapper userMapper;

    @Transactional
    @Override
    public UserResponseDTO create(UserRequestDTO userRequestDTO) {
        logger.info("m: create - Creating a new user {}", userRequestDTO);

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

        logger.info("m: create - User created successfully");

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional
    @Override
    public UserResponseDTO update(Long id, UserRequestDTO userRequestDTO) {
        logger.info("m: update - Updating a user {}", userRequestDTO);

        userBusinessRules.validateUpdateRules(userRequestDTO);
        personBusinessRules.validateUpdateRules(userRequestDTO.getPersonRequestDTO());

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        user = userMapper.toUser(user, userRequestDTO);

        this.personService.update(user.getPerson());
        this.userRepository.save(user);

        logger.info("m: update - User with ID {} updated successfully", id);

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public UserResponseDTO findById(Long id) {
        logger.info("m: findById - Attempting to find user by ID {}", id);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        logger.info("m: findById - User with id {} found successfully", id);

        return userMapper.toUserResponseDTO(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserResponseListDTO> findAll() {
        logger.info("m: findAll - Finding all users");

        List<User> users = this.userRepository.findAll();

        logger.info("m: findAll - Users found successfully");

        return userMapper.toUserResponseListDTO(users);
    }

    @Transactional
    @Override
    public void enable(Long id) {
        logger.info("m: enable - Enabling existing user by id {}", id);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        if (user.getStatus().equals(EnumStatus.ACTIVE)) {
            logger.warn("m: enable - User with id {} is already active", id);
            return;
        }

        user.setStatus(EnumStatus.ACTIVE);

        this.userRepository.save(user);

        logger.info("m: enable - User with id {} successfully enabled", id);
    }

    @Transactional
    @Override
    public void disable(Long id) {
        logger.info("m: disable - Disabling existing user by id {}", id);

        User user = this.userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(EnumMessageUserExceptions.USER_NOT_FOUND.getMessage(),
                        EnumMessageUserExceptions.USER_NOT_FOUND.getCode()));

        if (user.getStatus().equals(EnumStatus.INACTIVE)) {
            logger.warn("m: disable - User with id {} is already active", id);
            return;
        }
        user.setStatus(EnumStatus.INACTIVE);

        this.userRepository.save(user);

        logger.info("m: disable - User with id {} successfully disabled", id);
    }
}
