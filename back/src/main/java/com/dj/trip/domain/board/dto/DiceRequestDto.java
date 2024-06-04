package com.dj.trip.domain.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DiceRequestDto {
    private int memberBoardMapId;
    private int diceVal1;
    private int diceVal2;
    private int islandCnt;
}
