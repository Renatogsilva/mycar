package br.com.renatogsilva.my_car.api.config;

import br.com.renatogsilva.my_car.model.domain.Person;
import br.com.renatogsilva.my_car.model.domain.Phone;
import br.com.renatogsilva.my_car.model.domain.User;
import br.com.renatogsilva.my_car.model.enumerators.EnumSex;
import br.com.renatogsilva.my_car.model.enumerators.EnumStatus;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypePhone;
import br.com.renatogsilva.my_car.model.enumerators.EnumTypeUser;
import br.com.renatogsilva.my_car.repository.person.PersonRepository;
import br.com.renatogsilva.my_car.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
public class AdminUserConfig implements CommandLineRunner {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final PersonRepository personRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        User user = new User();
        Person person = new Person();
        user.setPerson(person);

        user.setUsername("teste.teste");
        user.setPassword(bCryptPasswordEncoder.encode("355.137.120-24"));
        user.setTypeUser(EnumTypeUser.ADMIN);
        user.setStatus(EnumStatus.ACTIVE);
        user.setPrimaryAccess(true);
        user.setCreationDate(LocalDate.now());
        user.getPerson().setFirstName("First");
        user.getPerson().setLastName("Last Name");
        user.getPerson().setSex(EnumSex.MALE);
        user.getPerson().setCpf("355.137.120-24");
        user.getPerson().setEmail("teste.com@gmail.com");
        user.getPerson().setBirthDate(LocalDate.now());
        user.getPerson().getPhones().add(new Phone(null, "(62)98420-5789", EnumTypePhone.CELL_PHONE, true, user.getPerson()));

        this.personRepository.save(user.getPerson());
        this.userRepository.save(user);
    }
}
