package com.dj.trip.domain.score.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateScoreRequestDto {
    private String memeberId;
    private int sidoId;
    private int plusScore;
}
