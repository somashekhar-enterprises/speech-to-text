package com.speechtotext.core.converter;

import com.speechtotext.core.domain.Tenant;
import com.speechtotext.core.dto.response.TenantResponse;
import org.springframework.stereotype.Component;

@Component
public class TenantToTenantResponseConverter {

    public TenantResponse convert(Tenant source) {
        TenantResponse response = new TenantResponse();
        response.setName(source.getName());
        response.setEmail(source.getEmail());
        response.setUsername(source.getUsername());
        response.setPhone(source.getPhone());
        return response;
    }
}
