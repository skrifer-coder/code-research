package skrifer.github.com.mybatisplus.generate.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author shenjun
 * @since 2024-02-28
 */
@ApiModel(value = "贝壳租房各城市查询地址对象", description = "")
public class 贝壳租房各城市查询地址 implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Long id;

      @ApiModelProperty("省份")
      private String province;

      @ApiModelProperty("城市")
      private String city;

      @ApiModelProperty("查询地址")
      private String url;
    
    public Long getId() {
        return id;
    }

      public void setId(Long id) {
          this.id = id;
      }
    
    public String getProvince() {
        return province;
    }

      public void setProvince(String province) {
          this.province = province;
      }
    
    public String getCity() {
        return city;
    }

      public void setCity(String city) {
          this.city = city;
      }
    
    public String getUrl() {
        return url;
    }

      public void setUrl(String url) {
          this.url = url;
      }

    @Override
    public String toString() {
        return "贝壳租房各城市查询地址{" +
              "id = " + id +
                  ", province = " + province +
                  ", city = " + city +
                  ", url = " + url +
              "}";
    }
}
