package com.speechtotext.core.service;

import com.speechtotext.core.context.TenantContext;
import org.springframework.stereotype.Service;

@Service
public class ThreadLocalService {

    public String getTenantID() {
        return TenantContext.getCurrentTenantId();
    }
}
