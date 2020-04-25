package pl.plagodzinski.eurortvagdtask.user.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    @Query("select u from User u join u.moneyAccountList ma on ma.user.pesel = u.pesel where u.pesel = :pesel")
    User getByPesel(@Param("pesel") String pesel);

    @Query("select COUNT(u) from User u where u.pesel = :pesel")
    long countByPesel(@Param("pesel") String pesel);
}
