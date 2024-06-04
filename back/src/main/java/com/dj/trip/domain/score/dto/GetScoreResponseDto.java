package com.dj.trip.domain.score.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetScoreResponseDto {
    private String memberId;
    private int score;
}
