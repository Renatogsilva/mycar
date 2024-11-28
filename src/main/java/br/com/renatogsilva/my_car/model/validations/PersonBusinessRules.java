package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessagePersonExceptions;
import br.com.renatogsilva.my_car.model.exceptions.person.PersonDuplicationException;
import br.com.renatogsilva.my_car.repository.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonBusinessRules {

    private static Logger logger = LoggerFactory.getLogger(PersonBusinessRules.class);

    private final PersonRepository personRepository;

    private void validatePersonDuplicateByPersonIdAndCpfAndEmail(Long personId, String cpf, String email) {
        logger.info("m: validatePersonDuplicateByPersonIdAndCpf - verify person duplication by id {} " +
                "and by cpf {} and by email {}", personId, cpf, email);

        if (this.personRepository.findPersonDuplicateByPersonIdAndCpfOrEmail(personId, cpf, email) != null) {
            logger.error("There is a person registered with this data");

            throw new PersonDuplicationException(EnumMessagePersonExceptions.CAR_DUPLICATE.getMessage(),
                    EnumMessagePersonExceptions.CAR_DUPLICATE.getCode());
        }
    }

    public void validateInclusioRules(PersonRequestDTO personRequestDTO) {
        validatePersonDuplicateByPersonIdAndCpfAndEmail(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail());
    }

    public void validateUpdateRules(PersonRequestDTO personRequestDTO) {
        validatePersonDuplicateByPersonIdAndCpfAndEmail(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail());
    }
}
