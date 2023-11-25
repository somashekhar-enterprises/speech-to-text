package com.speechtotext.core.context;

public class TenantContext {

    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    private static final ThreadLocal<String> CURRENT_TENANT_ID = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static String getCurrentTenantId() {
        return CURRENT_TENANT_ID.get();
    }

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }

    public static void setCurrentTenantId(String tenantId) {
        CURRENT_TENANT_ID.set(tenantId);
    }
}
