package com.dj.trip.domain.attractionInfo.service;

import com.dj.trip.domain.attractionInfo.AttractionInfoVo;
import com.dj.trip.domain.attractionInfo.dto.GetAttarctionInfoResponseDto;
import com.dj.trip.domain.attractionInfo.dto.response.GetAttractionInfoByMemberIdResponse;
import com.dj.trip.domain.attractionInfo.dto.response.GetReviewIdResponse;

import java.util.List;

public interface AttractionInfoService {
    GetAttarctionInfoResponseDto getAttractionInfoNowLocation(int memberBoardMapId);

    List<GetAttractionInfoByMemberIdResponse> getAttractionInfoByMemberId(String memberId);

    List<AttractionInfoVo> getTop4AttractionInfo();

    GetReviewIdResponse getReviewIdByBoardlogid(int boardlogid, String memberId);
}
