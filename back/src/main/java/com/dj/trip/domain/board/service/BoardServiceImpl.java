package com.dj.trip.domain.board.service;

import com.dj.trip.domain.attractionInfo.AttractionInfoVo;
import com.dj.trip.domain.attractionInfo.mapper.AttractionInfoMapper;
import com.dj.trip.domain.board.BoardInfoVo;
import com.dj.trip.domain.board.MemberBoardMapVo;
import com.dj.trip.domain.board.BoardVo;
import com.dj.trip.domain.board.dto.*;
import com.dj.trip.domain.board.mapper.BoardMapper;
import com.dj.trip.domain.score.mapper.ScoreMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardServiceImpl implements BoardService {

    private final BoardMapper boardMapper;
    private final AttractionInfoMapper attractionInfoMapper;
    private final ScoreMapper scoreMapper;
    private final ModelMapper modelMapper;

    @Override
    public GetBoardResponseDto getBoard(GetBoardRequestDto getBoardRequestDto) {
        MemberBoardMapVo memberBoardMapVo = boardMapper.selectMemberBoardMapBySidoIdAndMemberId(getBoardRequestDto);

        if (memberBoardMapVo == null) {
            // 맵 등록
            memberBoardMapVo = MemberBoardMapVo.builder()
                    .memberId(getBoardRequestDto.getMemberId())
                    .sidoId(getBoardRequestDto.getSidoId())
                    .build();
            boardMapper.insertMemberBoardMap(memberBoardMapVo);

            List<AttractionInfoVo> attractionInfoVoList = attractionInfoMapper.selectAttractionInfoListBySidoId(getBoardRequestDto.getSidoId());
            int totalDataCount = attractionInfoVoList.size();
            int targetDataCount = 28;

            for (int i = totalDataCount - 1; i > 0; i--) {
                int index = ThreadLocalRandom.current().nextInt(i + 1);
                AttractionInfoVo temp = attractionInfoVoList.get(index);
                attractionInfoVoList.set(index, attractionInfoVoList.get(i));
                attractionInfoVoList.set(i, temp);
            }

            for (int i = 0; i < targetDataCount; i++) {
                boardMapper.insertBoard(BoardVo.builder()
                        .memberBoardMapId(memberBoardMapVo.getMemberBoardMapId())
                        .attractionInfoId(attractionInfoVoList.get(i).getAttractionInfoId())
                        .location(i)
                        .build());
            }

            scoreMapper.insertScore(getBoardRequestDto.getSidoId(), getBoardRequestDto.getMemberId());
        }

        // 맵 조회
        List<BoardInfoVo> boardInfoVoList = boardMapper.selectBoardInfo(memberBoardMapVo.getMemberBoardMapId());
        GetBoardResponseDto getBoardResponseDto = GetBoardResponseDto.builder()
                .nowLocation(memberBoardMapVo.getNowLocation())
                .boardMemberMapId(memberBoardMapVo.getMemberBoardMapId())
                .isIsland(memberBoardMapVo.isIsland())
                .islandCnt(memberBoardMapVo.getIslandCnt())
                .boardInfoVoList(boardInfoVoList)
                .build();
        return getBoardResponseDto;
    }

    @Override
    public DiceResponseDto rollDice(DiceRequestDto diceRequestDto, String memberId) {
        MemberBoardMapVo memberBoardMapVo = boardMapper.selectMemberBoardMapByMemberBoardMapId(diceRequestDto.getMemberBoardMapId());
        if (memberBoardMapVo.getNowLocation() == 7) {
            if (diceRequestDto.getDiceVal1() == diceRequestDto.getDiceVal2()) {
                boardMapper.updateMemberBoardMap(diceRequestDto);
            } else {
                if (memberBoardMapVo.getIslandCnt() < 2) {
                    diceRequestDto.setDiceVal1(0);
                    diceRequestDto.setDiceVal2(0);
                    diceRequestDto.setIslandCnt(memberBoardMapVo.getIslandCnt() + 1);
                    boardMapper.updateMemberBoardMap(diceRequestDto);
                } else {
                    diceRequestDto.setIslandCnt(0);
                    boardMapper.updateMemberBoardMap(diceRequestDto);
                }
            }
        } else {
            boardMapper.updateMemberBoardMap(diceRequestDto);
            boardMapper.insertMemberBoardLog(memberId, diceRequestDto.getMemberBoardMapId(), boardMapper.selectNowLocationByMemberBoardMapId(diceRequestDto.getMemberBoardMapId()));
        }
        memberBoardMapVo = boardMapper.selectMemberBoardMapByMemberBoardMapId(diceRequestDto.getMemberBoardMapId());
        DiceResponseDto diceResponseDto = modelMapper.map(memberBoardMapVo, DiceResponseDto.class);

        return diceResponseDto;
    }

    @Override
    public void deleteBoard(int memberBoardMapId) {
        boardMapper.deleteBoardByMemberBoardMapId(memberBoardMapId);
        boardMapper.deleteMemberBoardMapByMemberBoardMapId(memberBoardMapId);
    }

    @Override
    public UpdateLocationResponseDto updateLocation(UpdateLocationRequestDto updateLocationRequestDto) {
        boardMapper.updateNowLocation(updateLocationRequestDto);
        return modelMapper.map(boardMapper.selectMemberBoardMapByMemberBoardMapId(updateLocationRequestDto.getMemberBoardMapId()), UpdateLocationResponseDto.class);
    }

    @Override
    public GoldCardResponseDto goldCard(GoldCardRequestDto goldCardRequestDto) {
        int nowLocation = boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId());
        switch (goldCardRequestDto.getIndex()) {
            case 1:
                boardMapper.updateNowLocation(new UpdateLocationRequestDto(goldCardRequestDto.getMemberBoardMapId(), (nowLocation + 4) % 28));
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "앞으로 4칸 이동!!!");
            case 2:
                int newLocation = (nowLocation - 4) % 28;
                if (newLocation < 0) {
                    newLocation += 28;
                }
                boardMapper.updateNowLocation(new UpdateLocationRequestDto(goldCardRequestDto.getMemberBoardMapId(), newLocation));
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "뒤로 4칸 이동!!!");
            case 3:
                boardMapper.updateNowLocation(new UpdateLocationRequestDto(goldCardRequestDto.getMemberBoardMapId(), 21));
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "우주여행으로 이동!!!");
            case 4:
                boardMapper.updateNowLocation(new UpdateLocationRequestDto(goldCardRequestDto.getMemberBoardMapId(), 7));
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "무인도로 이동!!!");
            case 5:
                scoreMapper.updateScore(goldCardRequestDto.getSidoId(), goldCardRequestDto.getMemberId(), 300);
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "300점 획득!!!");
            case 6:
                scoreMapper.updateScore(goldCardRequestDto.getSidoId(), goldCardRequestDto.getMemberId(), 100);
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "100점 획득!!!");
            case 7:
                scoreMapper.updateScore(goldCardRequestDto.getSidoId(), goldCardRequestDto.getMemberId(), -50);
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "50점을 잃었습니다!!!");
            default:
                scoreMapper.updateScore(goldCardRequestDto.getSidoId(), goldCardRequestDto.getMemberId(), -300);
                return new GoldCardResponseDto(boardMapper.selectNowLocationByMemberBoardMapId(goldCardRequestDto.getMemberBoardMapId()), "300점을 잃었습니다!!!");
        }
    }
}
