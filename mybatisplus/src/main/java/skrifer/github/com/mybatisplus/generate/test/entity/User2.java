package skrifer.github.com.mybatisplus.generate.test.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 
 * </p>
 *
 * @author shenjun
 * @since 2024-02-20
 */
@ApiModel(value = "User2对象", description = "")
public class User2 implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("主键ID")
        private Long id;

      @ApiModelProperty("姓名")
      private String name;

      @ApiModelProperty("年龄")
      private Integer age;

      @ApiModelProperty("邮箱")
      private String email;
    
    public Long getId() {
        return id;
    }

      public void setId(Long id) {
          this.id = id;
      }
    
    public String getName() {
        return name;
    }

      public void setName(String name) {
          this.name = name;
      }
    
    public Integer getAge() {
        return age;
    }

      public void setAge(Integer age) {
          this.age = age;
      }
    
    public String getEmail() {
        return email;
    }

      public void setEmail(String email) {
          this.email = email;
      }

    @Override
    public String toString() {
        return "User2{" +
              "id = " + id +
                  ", name = " + name +
                  ", age = " + age +
                  ", email = " + email +
              "}";
    }
}
