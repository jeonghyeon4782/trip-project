package com.dj.trip.domain.board.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLocationRequestDto {
    private int memberBoardMapId;
    private int location;
}
