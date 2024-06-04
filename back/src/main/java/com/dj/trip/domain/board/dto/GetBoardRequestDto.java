package com.dj.trip.domain.board.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardRequestDto {

    @NotBlank
    private int sidoId;
    @NotBlank
    private String memberId;

}
