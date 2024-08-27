package org.delivery.storeadmin.domain.sse.connection.ifs;

import org.delivery.storeadmin.domain.sse.connection.model.UserSseConnection;

public interface ConnectionPoolIfs<T,R> {
    void addSession(T key, R session);
    R getSession(T uniqueKey);

    void onCompletionCallBack(R session);
}
