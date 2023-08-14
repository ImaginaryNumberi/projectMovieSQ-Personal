package com.ixtx.BDHBOX.enums;

public enum ReserveResult {
    // 로그인 여부
    FAILURE_NOT_LOGIN,
    // 인덱스 변조 및 기한지난 것
    FAILURE_INVALID_INDEX,
    // 전좌석 현황
    FAILURE_FULL_SEAT,
    // 아무 문제 없음
    SUCCESS
}
