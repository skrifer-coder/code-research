package skrifer.github.com.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @TableField(value = "CREATED_BY", fill = FieldFill.INSERT)
    private String createdBy;

    @TableField(value = "CREATED_TIME", fill = FieldFill.INSERT)
    private LocalDateTime createdTime;

    @TableField(value = "UPDATED_BY", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    @TableField(value = "UPDATED_TIME", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedTime;

    /**
     * 是否删除
     * 需要结合 mybatisplus logic-delete* 配置
     * 框架提供的逻辑删除注解。如果使用delete关键字，会报错
     * 如果进行查询，会过滤被逻辑删除的字段！
     *  0 未删除
     *  1 已删除
     */
    @TableField("DELETE_IND")
    @TableLogic
    private Byte deleteInd;

    @TableField("VERSION")
    @Version
    private Long version;
}
