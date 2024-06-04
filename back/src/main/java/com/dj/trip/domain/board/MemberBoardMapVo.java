package com.dj.trip.domain.board;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MemberBoardMapVo {
    private int memberBoardMapId;
    private String memberId;
    private int sidoId;
    private int nowLocation;
    private boolean isIsland;
    private int islandCnt;
}

