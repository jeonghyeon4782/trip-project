package com.dj.trip.domain.mail.mapper;

import com.dj.trip.domain.mail.MailVo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MailMapper {
    void insertMail(MailVo mailVo);
    MailVo selectMailByEmail(String email);
    void deleteMailByEmail(String email);
}
