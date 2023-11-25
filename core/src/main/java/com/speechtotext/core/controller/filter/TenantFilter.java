package com.speechtotext.core.controller.filter;

import com.speechtotext.core.context.TenantContext;
import com.speechtotext.core.domain.Tenant;
import com.speechtotext.core.service.TenantService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(TenantFilter.class);

    private final TenantService tenantService;

    public TenantFilter(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tenant = getTenant(request);
        if (null == tenant) {
            if (permittedURIs(request.getRequestURI())) {
                filterChain.doFilter(request, response);
            } else {
                LOGGER.error("Tenant not found for request accessing non permissible URI {}", request.getRequestURI());
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Tenant not found for non permissible URI");
                response.getWriter().flush();
            }
            return;
        }

        Tenant tenantDao = tenantService.findByUsername(tenant);
        if (null == tenantDao) {
            LOGGER.error("Tenant {} not found", tenant);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Tenant not found");
            response.getWriter().flush();
            return;
        }

        TenantContext.setCurrentTenant(tenant);
        TenantContext.setCurrentTenantId(tenantDao.getId());
        filterChain.doFilter(request, response);
    }

    private boolean permittedURIs(String requestURI) {
        return requestURI.startsWith("/api/v1/tenant/register") || requestURI.startsWith("/api-docs")
                || requestURI.startsWith("/api/v1/tenant/login");
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().startsWith("/webjars/")
                || request.getRequestURI().startsWith("/css/")
                || request.getRequestURI().startsWith("/js/")
                || request.getRequestURI().startsWith("/.ico");

    }

    private String getTenant(HttpServletRequest request) {
        String tenant = request.getHeader("X-Tenant");
        if (null == tenant) {
            tenant = request.getParameter("tenant");
        }
        return tenant;
    }
}
