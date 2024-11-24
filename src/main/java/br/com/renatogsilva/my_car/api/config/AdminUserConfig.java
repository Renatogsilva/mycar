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

        user.setUsername("renato.silva");
        user.setPassword(bCryptPasswordEncoder.encode("041.423.631-90"));
        user.setTypeUser(EnumTypeUser.ADMIN);
        user.setStatus(EnumStatus.ACTIVE);
        user.setPrimaryAccess(true);
        user.setCreationDate(LocalDate.now());
        user.getPerson().setFirstName("Renato");
        user.getPerson().setLastName("Gomes Silva");
        user.getPerson().setSex(EnumSex.MALE);
        user.getPerson().setCpf("041.423.631-90");
        user.getPerson().setEmail("silvarenato180@gmail.com");
        user.getPerson().setBirthDate(LocalDate.now());
        user.getPerson().getPhones().add(new Phone(null, "(62)98495-2709", EnumTypePhone.CELL_PHONE, true, user.getPerson()));

        this.personRepository.save(user.getPerson());
        this.userRepository.save(user);
    }
}
