package skrifer.github.com.selenium.jpaentity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "贝壳租房各城市查询地址")
@EntityListeners(AuditingEntityListener.class)
public class BKZFINFO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String province;

    private String city;

    private String url;

}
