package skrifer.github.com.mybatisplus.generate.test.mapper;

import skrifer.github.com.mybatisplus.generate.test.entity.TravelAddress;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author shenjun
 * @since 2024-02-28
 */
public interface TravelAddressMapper extends BaseMapper<TravelAddress> {
    List<TravelAddress> getTravelsByCustomSql();
}
