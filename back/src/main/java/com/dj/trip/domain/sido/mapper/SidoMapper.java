package com.dj.trip.domain.sido.mapper;

import com.dj.trip.domain.sido.SidoVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SidoMapper {

    List<SidoVo> selectSidoList();

}
