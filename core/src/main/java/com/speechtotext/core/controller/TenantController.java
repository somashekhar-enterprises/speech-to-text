package com.speechtotext.core.controller;

import com.speechtotext.core.context.TenantContext;
import com.speechtotext.core.converter.TenantToTenantResponseConverter;
import com.speechtotext.core.domain.Tenant;
import com.speechtotext.core.dto.request.LoginRequest;
import com.speechtotext.core.dto.request.TenantRequest;
import com.speechtotext.core.dto.response.LoginResponse;
import com.speechtotext.core.dto.response.TenantResponse;
import com.speechtotext.core.service.TenantService;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.ws.rs.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/tenant")
public class TenantController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantController.class);

    private final SecurityContextHolderStrategy securityContextHolderStrategy =
            SecurityContextHolder.getContextHolderStrategy();

    private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

    @Inject
    private TenantService tenantService;

    @Inject
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @Inject
    private TenantToTenantResponseConverter tenantToTenantResponseConverter;

    @PostMapping("/register")
    TenantResponse register(@Valid @RequestBody TenantRequest request) {
        LOGGER.info("Registering tenant with email {}", request.getEmail());
        Tenant tenant = tenantService.register(request);
        return tenantToTenantResponseConverter.convert(tenant);
    }

    @PostMapping("/login")
    LoginResponse login(@Valid @RequestBody LoginRequest request, HttpServletRequest servletRequest,
                        HttpServletResponse servletResponse) {
        LOGGER.info("Logging in tenant with username {}", request.getUsername());

        try {

            Authentication authentication =
                    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),
                            request.getPassword()));
            SecurityContext context = securityContextHolderStrategy.createEmptyContext();
            context.setAuthentication(authentication);
            securityContextHolderStrategy.setContext(context);

            securityContextRepository.saveContext(context, servletRequest, servletResponse);
        } catch (Exception e) {
            LOGGER.error("Error while authenticating tenant with username {}", request.getUsername(), e);
            throw new BadRequestException("Error while authenticating tenant");
        }

        LoginResponse response = new LoginResponse();
        response.setUsername(request.getUsername());
        response.setMessage("Login successful");
        return response;
    }

    @GetMapping("/me")
    TenantResponse get() {
        String tenant = TenantContext.getCurrentTenantId();
        LOGGER.info("Getting tenant with username {}", tenant);
        return tenantToTenantResponseConverter.convert(tenantService.findById(tenant));
    }
}
