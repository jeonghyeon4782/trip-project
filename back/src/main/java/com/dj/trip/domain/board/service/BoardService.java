package com.dj.trip.domain.board.service;

import com.dj.trip.domain.board.dto.*;

public interface BoardService {

    GetBoardResponseDto getBoard(GetBoardRequestDto getBoardRequestDto);

    DiceResponseDto rollDice(DiceRequestDto diceRequestDto, String memberId);

    void deleteBoard(int memberBoardMapId);

    UpdateLocationResponseDto updateLocation(UpdateLocationRequestDto updateLocationRequestDto);

    GoldCardResponseDto goldCard(GoldCardRequestDto goldCardRequestDto);
}
