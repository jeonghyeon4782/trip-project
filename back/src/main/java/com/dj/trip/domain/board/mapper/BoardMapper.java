package com.dj.trip.domain.board.mapper;

import com.dj.trip.domain.board.BoardInfoVo;
import com.dj.trip.domain.board.BoardVo;
import com.dj.trip.domain.board.MemberBoardMapVo;
import com.dj.trip.domain.board.dto.DiceRequestDto;
import com.dj.trip.domain.board.dto.GetBoardRequestDto;
import com.dj.trip.domain.board.dto.UpdateLocationRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoardMapper {

    MemberBoardMapVo selectMemberBoardMapBySidoIdAndMemberId(GetBoardRequestDto getBoardRequestDto);

    MemberBoardMapVo selectMemberBoardMapByMemberBoardMapId(int memberBoardMapId);

    int insertMemberBoardMap(MemberBoardMapVo memberBoardMapVo);

    void insertBoard(BoardVo boardVo);

    List<BoardInfoVo> selectBoardInfo(int memberBoardMapId);

    void updateMemberBoardMap(DiceRequestDto diceRequestDto);

    int selectNowLocationByMemberBoardMapId(int memberBoardMapId);

    void deleteBoardByMemberBoardMapId(int memberBoardMapId);

    void deleteMemberBoardMapByMemberBoardMapId(int memberBoardMapId);

    void updateNowLocation(UpdateLocationRequestDto updateLocationRequestDto);

    void insertMemberBoardLog(String memberId, int memberBoardMapId, int location);
}
