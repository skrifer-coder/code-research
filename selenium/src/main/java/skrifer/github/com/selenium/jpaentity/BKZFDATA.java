package skrifer.github.com.selenium.jpaentity;

import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "贝壳租房价格信息")
@EntityListeners(AuditingEntityListener.class)
public class BKZFDATA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cityName;

    private String district;

    @Column(name = "`desc`")
    private String desc;

    private BigDecimal price1;

    private BigDecimal price2;

    private String unit;

    private int hall;
    private int toilet;
    private BigDecimal area;


}
