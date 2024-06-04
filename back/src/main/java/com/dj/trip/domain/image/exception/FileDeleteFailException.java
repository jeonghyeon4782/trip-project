package com.dj.trip.domain.image.exception;

import com.dj.trip.global.exception.ErrorCode;
import com.dj.trip.global.exception.TripException;

public class FileDeleteFailException extends TripException {
    public static final FileDeleteFailException EXCEPTION = new FileDeleteFailException();

    private FileDeleteFailException() {
        super(ErrorCode.FILE_DELETE_FAIL);
    }
}
