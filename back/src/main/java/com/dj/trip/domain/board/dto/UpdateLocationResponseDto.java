package com.dj.trip.domain.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationResponseDto {
    private int nowLocation;
    private int islandCnt;
}
