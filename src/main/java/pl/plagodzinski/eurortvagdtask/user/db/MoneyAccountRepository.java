package pl.plagodzinski.eurortvagdtask.user.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MoneyAccountRepository extends JpaRepository<MoneyAccount, Long> {
    @Query("select umc from MoneyAccount umc join umc.user u where umc.currencyCode = :currencyCode and u.pesel = :pesel")
    MoneyAccount findByUserAndCurrencyName(@Param("pesel") String pesel, @Param("currencyCode") String currencyCode);
}
