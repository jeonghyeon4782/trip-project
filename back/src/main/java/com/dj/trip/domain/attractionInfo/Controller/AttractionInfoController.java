package com.dj.trip.domain.attractionInfo.Controller;

import com.dj.trip.domain.attractionInfo.dto.GetAttarctionInfoResponseDto;
import com.dj.trip.domain.attractionInfo.service.AttractionInfoService;
import com.dj.trip.global.dto.ResponseDto;
import com.dj.trip.global.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/attraction-info")
public class AttractionInfoController {

    private final AttractionInfoService attractionInfoService;
    private final JWTUtil jwtUtil;

    // memberBoardMapId로 현재 캐릭터 위치에 있는 관광지 정보 조회
    @GetMapping("/{memberBoardMapId}")
    public ResponseDto<GetAttarctionInfoResponseDto> getAttractionInfoNowLocation(@PathVariable("memberBoardMapId") int memberBoardMapId) {
        return new ResponseDto<>(HttpStatus.OK.value(), "관광지 조회 성공", attractionInfoService.getAttractionInfoNowLocation(memberBoardMapId));
    }

    @GetMapping
    public ResponseEntity<ResponseDto<?>> getAttractionInfoByMemberId(HttpServletRequest request) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                new ResponseDto<>(HttpStatus.OK.value(), "사용자가 간 관광지 조회 성공",
                        attractionInfoService.getAttractionInfoByMemberId(memberId)));
    }

    @GetMapping("/top")
    public ResponseEntity<ResponseDto<?>> getTop4AttractionInfo() {
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                new ResponseDto<>(HttpStatus.OK.value(), "좋아요 순으로 4개의 관광지 조회 성공",
                        attractionInfoService.getTop4AttractionInfo()));
    }

    @GetMapping("/boardlogid/{boardlogid}")
    public ResponseEntity<ResponseDto<?>> getReviewIdByBoardlogid(HttpServletRequest request,
                                                                  @PathVariable("boardlogid") int boardlogid) {
        String memberId = jwtUtil.getMemberIdByToken(request);
        return ResponseEntity.status(HttpStatus.OK.value()).body(
                new ResponseDto<>(HttpStatus.OK.value(), "사용자가 간 관광지 조회 성공",
                        attractionInfoService.getReviewIdByBoardlogid(boardlogid, memberId)));
    }

}
