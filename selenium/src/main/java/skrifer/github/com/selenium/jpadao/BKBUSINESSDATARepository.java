package skrifer.github.com.selenium.jpadao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skrifer.github.com.selenium.jpaentity.BKBUSINESSDATA;

@Repository
public interface BKBUSINESSDATARepository extends JpaRepository<BKBUSINESSDATA, Long> {

}
