package com.speechtotext.core.service;

import com.speechtotext.core.dao.TenantRepository;
import com.speechtotext.core.domain.Tenant;
import com.speechtotext.core.dto.request.TenantRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.util.HashSet;
import java.util.UUID;

import static java.lang.String.format;

@Service
public class TenantService implements UserDetailsService {

    @Inject
    private TenantRepository tenantRepository;

    @Inject
    @Qualifier("passwordEncoder")
    private PasswordEncoder passwordEncoder;

    public Tenant findById(String tenantId) {
        return tenantRepository.findById(UUID.fromString(tenantId)).orElseThrow(RuntimeException::new);
    }

    private void checkIfTenantExists(String username) {
        if (null != findByUsername(username)) {
            throw new BadRequestException(format("Tenant with username %s already exists", username));
        }
    }

    public Tenant register(TenantRequest request) {
        checkIfTenantExists(request.getUsername());
        Tenant toSave = new Tenant();
        toSave.setName(request.getName());
        toSave.setEmail(request.getEmail());
        toSave.setUsername(request.getUsername());
        toSave.setPhone(request.getPhone());
        toSave.setPassword(passwordEncoder.encode(request.getPassword()));
        toSave.setPatients(new HashSet<>());
        toSave.setCreated(Date.from(Instant.now()));
        return tenantRepository.save(toSave);
    }

    public Tenant findByUsername(String username) {
        return tenantRepository.findByUsername(username)
                .orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Tenant tenant = findByUsername(username);
        if (null == tenant) {
            throw new UsernameNotFoundException(format("Tenant with username %s not found", username));
        }
        return new User(tenant.getUsername(), tenant.getPassword(), new HashSet<>());
    }
}
