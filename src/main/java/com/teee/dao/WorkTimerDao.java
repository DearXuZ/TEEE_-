package com.teee.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.teee.domain.works.WorkTimer;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WorkTimerDao extends BaseMapper<WorkTimer> {
}
