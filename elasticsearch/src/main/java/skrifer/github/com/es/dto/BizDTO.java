package skrifer.github.com.es.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import skrifer.github.com.es.annotation.ESLongField;
import skrifer.github.com.es.annotation.ESTextField;


/**
 * 统计分析 课堂实录 课程开闭 主体数据
 */
@Data
@NoArgsConstructor
public class BizDTO extends ESBaseDTO {

    public BizDTO(Long id, String uuid, String context){
        this.id = id;
        super.setUuid(uuid);
        this.context = context;
    }

    public BizDTO(String uuid, String context){
        this(0L, uuid, context);
    }

    @ESLongField
    private Long id;

    @ESTextField
    private String context;

    @Override
    public String getKey() {
        return this.getUuid() + "." + this.id;
    }
}
