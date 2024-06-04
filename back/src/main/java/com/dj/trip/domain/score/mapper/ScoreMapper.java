package com.dj.trip.domain.score.mapper;

import com.dj.trip.domain.score.dto.GetScoreResponseDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ScoreMapper {

    void insertScore(int sidoId, String memberId);

    void updateScore(int sidoId, String memberId, int plusScore);

    List<GetScoreResponseDto> selectScoreLimitFive(int sidoId, String memberId);

}
