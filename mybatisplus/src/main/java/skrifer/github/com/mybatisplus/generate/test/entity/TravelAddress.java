package skrifer.github.com.mybatisplus.generate.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("travel_address")
@ApiModel(value = "TravelAddress对象", description = "")
public class TravelAddress implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("地址")
      private String address;

        @ApiModelProperty("省份")
        private String province;

      @ApiModelProperty("城市")
      private String city;

      @ApiModelProperty("区")
      private String district;

      @ApiModelProperty("经度")
      private String lng;

      @ApiModelProperty("纬度")
      private String lat;
    
    public Integer getId() {
        return id;
    }

      public void setId(Integer id) {
          this.id = id;
      }
    
    public String getAddress() {
        return address;
    }

      public void setAddress(String address) {
          this.address = address;
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
    
    public String getDistrict() {
        return district;
    }

      public void setDistrict(String district) {
          this.district = district;
      }
    
    public String getLng() {
        return lng;
    }

      public void setLng(String lng) {
          this.lng = lng;
      }
    
    public String getLat() {
        return lat;
    }

      public void setLat(String lat) {
          this.lat = lat;
      }

    @Override
    public String toString() {
        return "TravelAddress{" +
              "id = " + id +
                  ", address = " + address +
                  ", city = " + city +
                  ", district = " + district +
                  ", lng = " + lng +
                  ", lat = " + lat +
              "}";
    }
}
