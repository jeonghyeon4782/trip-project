package com.dj.trip.domain.like.mapper;

import com.dj.trip.domain.like.Like;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LikeMapper {

    int insertLike(Like like);

    boolean isHasLike(Like like);

    boolean isWriteByMe(Like like);

    int deleteLike(Like like);
}
