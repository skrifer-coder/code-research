package skrifer.github.com.selenium.service.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Objects;

@Data
public class 贝壳租房价格信息实体DTO {
    private String name;

    private BigDecimal price1;
    private BigDecimal price2;
    private String unit;

    private String desc;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        贝壳租房价格信息实体DTO that = (贝壳租房价格信息实体DTO) o;
        return Objects.equals(name, that.name) && Objects.equals(desc, that.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, desc);
    }
}
