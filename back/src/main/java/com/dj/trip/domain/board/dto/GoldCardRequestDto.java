package com.dj.trip.domain.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldCardRequestDto {
    private int memberBoardMapId;
    private int index;
    private int sidoId;
    private String memberId;
}
