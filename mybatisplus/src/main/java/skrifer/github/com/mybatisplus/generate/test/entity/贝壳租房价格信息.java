package skrifer.github.com.mybatisplus.generate.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.math.BigDecimal;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author shenjun
 * @since 2024-03-04
 */
@ApiModel(value = "贝壳租房价格信息对象", description = "")
public class 贝壳租房价格信息 implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      @ApiModelProperty("城市")
      private String cityName;

      @ApiModelProperty("行政区域")
      private String district;

      @ApiModelProperty("描述")
      private String desc;

      @ApiModelProperty("第一价格")
      private BigDecimal price1;

      @ApiModelProperty("第二价格")
      private BigDecimal price2;

      @ApiModelProperty("价格单位")
      private String unit;
    
    public Long getId() {
        return id;
    }

      public void setId(Long id) {
          this.id = id;
      }
    
    public String getCityName() {
        return cityName;
    }

      public void setCityName(String cityName) {
          this.cityName = cityName;
      }
    
    public String getDistrict() {
        return district;
    }

      public void setDistrict(String district) {
          this.district = district;
      }
    
    public String getDesc() {
        return desc;
    }

      public void setDesc(String desc) {
          this.desc = desc;
      }
    
    public BigDecimal getPrice1() {
        return price1;
    }

      public void setPrice1(BigDecimal price1) {
          this.price1 = price1;
      }
    
    public BigDecimal getPrice2() {
        return price2;
    }

      public void setPrice2(BigDecimal price2) {
          this.price2 = price2;
      }
    
    public String getUnit() {
        return unit;
    }

      public void setUnit(String unit) {
          this.unit = unit;
      }

    @Override
    public String toString() {
        return "贝壳租房价格信息{" +
              "id = " + id +
                  ", cityName = " + cityName +
                  ", district = " + district +
                  ", desc = " + desc +
                  ", price1 = " + price1 +
                  ", price2 = " + price2 +
                  ", unit = " + unit +
              "}";
    }
}
