package com.dj.trip.domain.score.service;

import com.dj.trip.domain.score.dto.GetScoreResponseDto;

import java.util.List;

public interface ScoreService {
    List<GetScoreResponseDto> getScore(int sidoId, String memberId);

    void updateScore(int sidoId, String memberId, int plusScore);
}
