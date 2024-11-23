package br.com.renatogsilva.my_car.repository.user;

import br.com.renatogsilva.my_car.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQueries {

    @Query(nativeQuery = true, value = "SELECT *FROM tb_user as u WHERE (:id IS NULL OR u.user_id != :id) " +
            "AND u.username = :username")
    public User findUserDuplicateByUserIdAndLogin(@Param("id") Long id, @Param("username") String username);
}
