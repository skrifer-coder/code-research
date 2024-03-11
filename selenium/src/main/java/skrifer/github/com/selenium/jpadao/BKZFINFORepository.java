package skrifer.github.com.selenium.jpadao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skrifer.github.com.selenium.jpaentity.BKZFINFO;

@Repository
public interface BKZFINFORepository extends JpaRepository<BKZFINFO, Long> {
}
