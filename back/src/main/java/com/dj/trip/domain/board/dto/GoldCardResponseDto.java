package com.dj.trip.domain.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GoldCardResponseDto {
    private int nowLocation;
    private String msg;
}
