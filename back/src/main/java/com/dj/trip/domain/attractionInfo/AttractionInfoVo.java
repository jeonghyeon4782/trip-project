package com.dj.trip.domain.attractionInfo;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AttractionInfoVo {
    private int attractionInfoId;
    private int sidoId;
    private String name;
    private String addr;
    private String url;
    private String description;
    private double latitude;
    private double longitude;
}
