package skrifer.github.com.jpa.entity;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "mp_test", schema = "lj28s")
@EntityListeners(AuditingEntityListener.class)
public class MP_TEST {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    private BigDecimal score;

    private Boolean delInd;


}
