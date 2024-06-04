package com.dj.trip.domain.image.exception;

import com.dj.trip.global.exception.ErrorCode;
import com.dj.trip.global.exception.TripException;

public class FileSizeException extends TripException {

    public static final FileSizeException EXCEPTION = new FileSizeException();

    private FileSizeException() {
        super(ErrorCode.IMAGE_SIZE_EXTENSION);
    }
}
