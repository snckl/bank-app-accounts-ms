package com.cnsn.accounts.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

// It is string cuz of updatedBy and createdBy
@Component("auditAwareImpl")
public class AuditAwareImpl implements AuditorAware<String> {

    // To return Auditor.
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("ACCOUNTS_MS");
    }
}
