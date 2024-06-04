package com.dj.trip.domain.image.exception;

import com.dj.trip.global.exception.ErrorCode;
import com.dj.trip.global.exception.TripException;

public class FileEmptyException extends TripException {

    public static final FileEmptyException EXCEPTION = new FileEmptyException();

    private FileEmptyException() {
        super(ErrorCode.BAD_FILE_EXTENSION);
    }
}
