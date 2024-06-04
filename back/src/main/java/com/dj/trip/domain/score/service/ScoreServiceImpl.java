package com.dj.trip.domain.score.service;

import com.dj.trip.domain.score.dto.GetScoreResponseDto;
import com.dj.trip.domain.score.mapper.ScoreMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScoreServiceImpl implements ScoreService{

    private final ScoreMapper scoreMapper;

    @Override
    public List<GetScoreResponseDto> getScore(int sidoId, String memberId) {
        return scoreMapper.selectScoreLimitFive(sidoId, memberId);
    }

    @Override
    public void updateScore(int sidoId, String memberId, int plusScore) {
        scoreMapper.updateScore(sidoId, memberId, plusScore);
    }
}
