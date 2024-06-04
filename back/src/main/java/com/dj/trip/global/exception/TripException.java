package com.dj.trip.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TripException extends RuntimeException {
    private ErrorCode errorCode;
}
