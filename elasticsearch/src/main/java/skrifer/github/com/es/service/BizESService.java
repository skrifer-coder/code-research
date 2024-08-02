package skrifer.github.com.es.service;


import org.springframework.stereotype.Service;
import skrifer.github.com.es.Enum.ESIndexEnum;
import skrifer.github.com.es.dto.BizDTO;

/**
 * 课堂实录统计课程开闭汇总
 */
@Service
public class BizESService extends ESClientService<BizDTO, ESIndexEnum> {

}
