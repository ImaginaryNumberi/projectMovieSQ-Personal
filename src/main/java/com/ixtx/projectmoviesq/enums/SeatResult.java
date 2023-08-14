package com.ixtx.projectmoviesq.enums;

public enum SeatResult {

    // 세션 인덱스 유효기간 만료
    EXPIRED_SESSIONINDEX,

    // 존재하지 않는 좌석일때
    NONEXISTENT_SEAT,

    // 예약된 좌석일 때
    RESERVED_SEAT,

    // 빈좌석일 때
    AVAILABLE_SEAT
}