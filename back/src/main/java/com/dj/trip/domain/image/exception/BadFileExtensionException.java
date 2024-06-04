package com.dj.trip.domain.image.exception;

import com.dj.trip.global.exception.ErrorCode;
import com.dj.trip.global.exception.TripException;

public class BadFileExtensionException extends TripException {

    public static final BadFileExtensionException EXCEPTION = new BadFileExtensionException();

    private BadFileExtensionException() {
        super(ErrorCode.NOT_FOUND);
    }
}
