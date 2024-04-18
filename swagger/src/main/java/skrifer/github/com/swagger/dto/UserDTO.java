package skrifer.github.com.swagger.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 此时我们需要使用@ApiModel来标注实体类，然后在接口中定义入参为实体类即可：
 *
 * @ApiModel：用来标类
 * 常用配置项：
 * value：实体类简称
 * description：实体类说明
 * @ApiModelProperty：用来描述类的字段的意义。
 * 常用配置项：
 * value：字段说明
 * example：设置请求示例（Example Value）的默认值，如果不配置，当字段为string的时候，此时请求示例中默认值为"".
 * name：用新的字段名来替代旧的字段名。
 * allowableValues：限制值得范围，例如{1,2,3}代表只能取这三个值；[1,5]代表取1到5的值；(1,5)代表1到5的值，不包括1和5；还可以使用infinity或-infinity来无限值，比如[1, infinity]代表最小值为1，最大值无穷大。
 * required：标记字段是否必填，默认是false,
 * hidden：用来隐藏字段，默认是false，如果要隐藏需要使用true，因为字段默认都会显示，就算没有@ApiModelProperty。
 */
@ApiModel(value = "用户DTO类", description = "用户DTO类描述")
public class UserDTO {

    @ApiModelProperty(value = "用户名称", example = "空白名称", name = "new_name", allowableValues = "张三,王五", required = true, hidden = false)
    private String name;

    @ApiModelProperty(value = "用户年龄", example = "99", name = "new_age", allowableValues = "1,99", required = true, hidden = false)
    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
