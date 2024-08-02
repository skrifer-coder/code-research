package skrifer.github.com.es.dto;

import lombok.Data;
import skrifer.github.com.es.annotation.ESDateTimeField;
import skrifer.github.com.es.annotation.ESIntegerField;
import skrifer.github.com.es.annotation.ESTextField;

@Data
public abstract class ESBaseDTO {

    private String uuid;

    @ESIntegerField
    private Integer version;

    @ESTextField
    private String insertBy;

    @ESDateTimeField
    private String insertTime;

    @ESTextField
    private String updateBy;

    @ESDateTimeField
    private String updateTime;

    public abstract String getKey();
}
