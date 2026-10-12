package br.com.renatogsilva.my_car.repository.user;

import br.com.renatogsilva.my_car.model.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryQueries {

    @Query(nativeQuery = true, value = "SELECT *FROM tb_user as u WHERE (:id IS NULL OR u.user_id != :id) " +
            "AND u.username = :username")
    public User findUserDuplicateByUserIdAndLogin(@Param("id") Long id, @Param("username") String username);

    public User findUserByUsername(String username);

    @Query(nativeQuery = true, value = "SELECT u.* FROM tb_user u " +
            " WHERE (:status IS NULL OR u.status = :status)" +
            " AND (:typeUser IS NULL OR u.type_user = :typeUser)")
    public Page<User> findAllByFilters(@Param("status") String status, @Param("typeUser") String typeUser, Pageable pageable);
}