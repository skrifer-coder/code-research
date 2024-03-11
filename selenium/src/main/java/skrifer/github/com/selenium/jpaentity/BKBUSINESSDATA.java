package skrifer.github.com.selenium.jpaentity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "贝壳写字楼出租价格")
@EntityListeners(AuditingEntityListener.class)
public class BKBUSINESSDATA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cityName;

    private String district;
    private String address;

    @Column(name = "`desc`")
    private String desc;

    private BigDecimal area;

    private BigDecimal avgPrice;
    private String avgPriceUnit;

    private BigDecimal totalPrice;
    private String totalPriceUnit;

    private String lat;
    private String lng;

    private String dataSource;


}
