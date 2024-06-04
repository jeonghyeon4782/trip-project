package com.dj.trip.domain.attractionInfo.service;

import com.dj.trip.domain.attractionInfo.Attraction;
import com.dj.trip.domain.attractionInfo.AttractionInfoVo;
import com.dj.trip.domain.attractionInfo.dto.GetAttarctionInfoResponseDto;
import com.dj.trip.domain.attractionInfo.dto.response.GetAttractionInfoByMemberIdResponse;
import com.dj.trip.domain.attractionInfo.dto.response.GetReviewIdResponse;
import com.dj.trip.domain.attractionInfo.mapper.AttractionInfoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AttractionInfoServiceImpl implements AttractionInfoService {

    private final AttractionInfoMapper attractionInfoMapper;
    private final ModelMapper modelMapper;

    // memberBoardMapId로 현재 캐릭터 위치에 있는 관광지 정보 조회
    @Override
    public GetAttarctionInfoResponseDto getAttractionInfoNowLocation(int memberBoardMapId) {
        return modelMapper.map(attractionInfoMapper.selectAttractionInfoByMemberBoardMapId(memberBoardMapId), GetAttarctionInfoResponseDto.class);
    }

    @Override
    public List<GetAttractionInfoByMemberIdResponse> getAttractionInfoByMemberId(String memberId) {
        return attractionInfoMapper.selectAttractionInfoByMemberId(memberId);
    }

    @Override
    public List<AttractionInfoVo> getTop4AttractionInfo() {
        return attractionInfoMapper.selectTop4AttractionInfo();
    }

    @Override
    public GetReviewIdResponse getReviewIdByBoardlogid(int boardlogid, String memberId) {
        return attractionInfoMapper.selectReviewIdByBoardlogid(new Attraction(boardlogid,memberId));
    }


}
