package com.dj.trip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

public enum ErrorCode {

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    NOT_FOUND(404, "해당하는 정보를 찾을 수 없습니다."),
    BAD_FILE_EXTENSION(404, "파일 확장자 오류입니다."),
    IMAGE_SIZE_EXTENSION(404, "파일 크기 오류입니다."),
    FILE_EMPTY(404, "해당 파일이 비어 있습니다."),
    FILE_UPLOAD_FAIL(404, "파일 업로드에 실패하였습니다."),
    FILE_DELETE_FAIL(404, "파일 삭제를 실패하였습니다.");

    private final int status;
    private final String reason;
}
