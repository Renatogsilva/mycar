package br.com.renatogsilva.my_car.repository.person;

import br.com.renatogsilva.my_car.model.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Long> {

    @Query(nativeQuery = true, value = "SELECT *FROM tb_person as p " +
            "WHERE (:personId IS NULL OR p.person_id != :personId) " +
            "AND (p.cpf = :cpf OR p.email = :email)")
    public Person findPersonDuplicateByPersonIdAndCpfOrEmail(@Param("personId") Long personId,
                                                             @Param("cpf") String cpf,
                                                             @Param("email") String email);
}
