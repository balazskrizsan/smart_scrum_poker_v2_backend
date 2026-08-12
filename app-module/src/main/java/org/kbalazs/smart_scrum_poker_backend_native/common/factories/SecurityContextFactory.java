package org.kbalazs.smart_scrum_poker_backend_native.common.factories;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityContextFactory
{
    public UUID getCurrentUserId()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null)
        {
            throw new IllegalStateException("No authenticated user found in security context");
        }
        return UUID.fromString(authentication.getName());
    }

    public Authentication getCurrentAuthentication()
    {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
        {
            throw new IllegalStateException("No authentication found in security context");
        }
        return authentication;
    }
}
