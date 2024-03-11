package skrifer.github.com.selenium.jpadao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import skrifer.github.com.selenium.jpaentity.BKZFDATA;

import java.util.List;

@Repository
public interface BKZFDATARepository extends JpaRepository<BKZFDATA, Long> {

    List<BKZFDATA> findByDistrictLike(String district);
}
