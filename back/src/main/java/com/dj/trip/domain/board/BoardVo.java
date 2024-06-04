package com.dj.trip.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardVo {
    private int boardId;
    private int memberBoardMapId;
    private int attractionInfoId;
    private int location;   // 보드판의 자리
}
