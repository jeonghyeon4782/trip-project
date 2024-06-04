package com.dj.trip.domain.attractionInfo.dto.response;

public record GetAttractionInfoByMemberIdResponse(
        int boardLogId,
        int attractionInfoId,
        String name,
        String imageUrl,
        String sidoName,
        String createDate,
        boolean isWrite
) {
}
