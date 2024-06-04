package com.dj.trip.domain.image.exception;

import com.dj.trip.global.exception.ErrorCode;
import com.dj.trip.global.exception.TripException;

public class FileUploadFailException extends TripException {

    public static final FileUploadFailException EXCEPTION = new FileUploadFailException();

    private FileUploadFailException() {
        super(ErrorCode.FILE_UPLOAD_FAIL);
    }
}
