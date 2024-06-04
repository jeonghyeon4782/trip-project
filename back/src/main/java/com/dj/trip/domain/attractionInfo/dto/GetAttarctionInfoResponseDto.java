package com.dj.trip.domain.attractionInfo.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAttarctionInfoResponseDto {
    private int attractionInfoId;
    private String name;
    private String addr;
    private String url;
    private String description;
    private double latitude;
    private double longitude;
}
