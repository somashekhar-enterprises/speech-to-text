package com.speechtotext.core.domain.model;

import com.speechtotext.core.domain.Tenant;

public interface TenantAwareModel {

    Tenant getTenant();
}
