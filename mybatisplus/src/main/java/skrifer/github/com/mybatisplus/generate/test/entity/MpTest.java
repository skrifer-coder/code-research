package skrifer.github.com.mybatisplus.generate.test.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

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
 * @since 2024-03-11
 */
@TableName("mp_test")
@ApiModel(value = "MpTest对象", description = "")
public class MpTest implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("姓名")
    private String name;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("分数")
    private BigDecimal score;

    @ApiModelProperty("是否删除")
    private Boolean delInd;

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

    public BigDecimal getScore() {
        return score;
    }

    public void setScore(BigDecimal score) {
        this.score = score;
    }

    public Boolean getDelInd() {
        return delInd;
    }

    public void setDelInd(Boolean delInd) {
        this.delInd = delInd;
    }

    @Override
    public String toString() {
        return "MpTest{" +
                "id = " + id +
                ", name = " + name +
                ", age = " + age +
                ", score = " + score +
                ", delInd = " + delInd +
                "}";
    }
}
