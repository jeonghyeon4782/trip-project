package com.dj.trip.domain.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiceResponseDto {
    private int memberBoardMapId;
    private int nowLocation;
    private int islandCnt;
}
