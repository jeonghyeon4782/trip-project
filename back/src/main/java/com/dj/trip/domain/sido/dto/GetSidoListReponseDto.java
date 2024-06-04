package com.dj.trip.domain.sido.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetSidoListReponseDto {

    private int sidoId;
    private String name;
    private String imageUrl;

}
