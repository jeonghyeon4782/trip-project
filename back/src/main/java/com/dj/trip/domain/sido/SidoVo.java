package com.dj.trip.domain.sido;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class SidoVo {

    private int sidoId;
    private String name;
    private String imageUrl;

    public void setImageUrl(String modifiedImageUrl) {
        this.imageUrl=modifiedImageUrl;
    }
}
