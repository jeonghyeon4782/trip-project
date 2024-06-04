package com.dj.trip.domain.sido.service;

import com.dj.trip.domain.image.service.ImageServiceUtils;
import com.dj.trip.domain.sido.dto.GetSidoListReponseDto;
import com.dj.trip.domain.sido.mapper.SidoMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class SidoServiceImpl implements SidoService {

    private final SidoMapper sidoMapper;
    private final ModelMapper modelMapper;
    private final ImageServiceUtils imageServiceUtils;

    @Override
    public List<GetSidoListReponseDto> getSidoList() {
        return sidoMapper.selectSidoList().stream()
                .peek(sidoVo -> {
                    String originalImageUrl = sidoVo.getImageUrl();
                    String modifiedImageUrl = modifyImageUrl(originalImageUrl);
                    sidoVo.setImageUrl(modifiedImageUrl);
                })
                .map(sidoVo -> modelMapper.map(sidoVo, GetSidoListReponseDto.class))
                .collect(Collectors.toList());
    }

    private String modifyImageUrl(String originalImageUrl) {
        if (originalImageUrl != null) {
            return imageServiceUtils.getImageUrl(originalImageUrl);
        }
        return null;
    }
}
