package com.dj.trip.domain.board.dto;

import com.dj.trip.domain.board.BoardInfoVo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetBoardResponseDto {
    int nowLocation;
    int boardMemberMapId;
    boolean isIsland;
    int islandCnt;
    List<BoardInfoVo> boardInfoVoList;
}
