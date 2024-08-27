package org.delivery.db.store.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum StoreStatus {
    REGISTERED("등록"),
    UNREGISTERED("해지"),
    ;
    /**
     *
     * 등록, 대기, 해지 신청, 해지 이런 식으로 고민해보자*/
    private final String description;
}
