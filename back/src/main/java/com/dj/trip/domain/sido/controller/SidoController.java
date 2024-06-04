package com.dj.trip.domain.sido.controller;

import com.dj.trip.domain.member.dto.FindPasswordRequestDto;
import com.dj.trip.domain.sido.dto.GetSidoListReponseDto;
import com.dj.trip.domain.sido.service.SidoService;
import com.dj.trip.global.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sido")
@Log4j2
public class SidoController {

    private final SidoService sidoService;

    @GetMapping()
    public ResponseEntity<ResponseDto<List<GetSidoListReponseDto>>> getSidoList() throws Exception{
        return ResponseEntity.status(HttpStatus.OK.value())
                .body(new ResponseDto<>(HttpStatus.OK.value(), "시도 정보 조회 성공", sidoService.getSidoList()));
    }

}
