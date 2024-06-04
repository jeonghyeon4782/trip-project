package com.dj.trip.domain.board.controller;

import com.dj.trip.domain.board.dto.*;
import com.dj.trip.domain.board.service.BoardService;
import com.dj.trip.global.dto.ResponseDto;
import com.dj.trip.global.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
@Log4j2
public class BoardController {

    private final JWTUtil jwtUtil;
    private final BoardService boardService;

    @GetMapping("/{sidoId}")
    public ResponseDto<GetBoardResponseDto> getBoard(@PathVariable("sidoId") int sidoId, HttpServletRequest request) {
        return new ResponseDto<>(HttpStatus.OK.value(), "보드판 조회 성공", boardService.getBoard(GetBoardRequestDto.builder()
                .sidoId(sidoId)
                .memberId(jwtUtil.getMemberIdByToken(request)).
                build()));
    }

    @DeleteMapping("/{memberBoardMapId}")
    public ResponseDto<String> deleteBoard(@PathVariable("memberBoardMapId") int memberBoardMapId) {
        boardService.deleteBoard(memberBoardMapId);
        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "보드판 삭제 성공", null);
    }

    @PutMapping("/location")
    public ResponseDto<UpdateLocationResponseDto> updateLocation(@RequestBody UpdateLocationRequestDto updateLocationRequestDto) {
        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "자리이동 성공", boardService.updateLocation(updateLocationRequestDto));
    }

    @PostMapping("/dice")
    public ResponseDto<DiceResponseDto> rollDice(@RequestBody DiceRequestDto diceRequestDto, HttpServletRequest request) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "주사위 굴리기 성공", boardService.rollDice(diceRequestDto, memberId));
    }
    
    @PutMapping("/gold-card")
    public ResponseDto<GoldCardResponseDto> drawGoldCard(@RequestBody GoldCardRequestDto goldCardRequestDto, HttpServletRequest request) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        goldCardRequestDto.setMemberId(memberId);
        return new ResponseDto<>(HttpStatus.NO_CONTENT.value(), "황금열쇠 성공", boardService.goldCard(goldCardRequestDto));
    }
}
