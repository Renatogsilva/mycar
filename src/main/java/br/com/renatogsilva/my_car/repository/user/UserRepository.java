package br.com.renatogsilva.my_car.repository.user;

import br.com.renatogsilva.my_car.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQueries {

    @Query(nativeQuery = true, value = "SELECT *FROM tb_user as user WHERE (:id IS NULL OR user.tb_id != :id) " +
            "AND user.username = :username")
    public User findUserDuplicatorUserByIdAndUsername(@Param("id") Long id, @Param("username") String username);
}
