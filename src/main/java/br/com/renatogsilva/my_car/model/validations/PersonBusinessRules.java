package br.com.renatogsilva.my_car.model.validations;

import br.com.renatogsilva.my_car.model.dto.person.PersonRequestDTO;
import br.com.renatogsilva.my_car.model.enumerators.EnumMessagePersonExceptions;
import br.com.renatogsilva.my_car.model.exceptions.person.PersonDuplicationException;
import br.com.renatogsilva.my_car.repository.person.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PersonBusinessRules {

    private final PersonRepository personRepository;

    private void validatePersonDuplicateByPersonIdAndCpf(Long personId, String cpf, String email) {
        if (this.personRepository.findPersonDuplicateByPersonIdAndCpfOrEmail(personId, cpf, email) != null) {
            throw new PersonDuplicationException(EnumMessagePersonExceptions.CAR_DUPLICATE.getMessage(),
                    EnumMessagePersonExceptions.CAR_DUPLICATE.getCode());
        }
    }

    public void validateInclusioRules(PersonRequestDTO personRequestDTO) {
        validatePersonDuplicateByPersonIdAndCpf(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail());
    }

    public void validateUpdateRules(PersonRequestDTO personRequestDTO) {
        validatePersonDuplicateByPersonIdAndCpf(personRequestDTO.getPersonId(),
                personRequestDTO.getCpf(), personRequestDTO.getEmail());
    }
}
