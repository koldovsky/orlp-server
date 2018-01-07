package com.softserve.academy.spaced.repetition.security.authentification;

import com.softserve.academy.spaced.repetition.domain.Account;
import com.softserve.academy.spaced.repetition.domain.Authority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public final class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(Account account) {
        return new JwtUser(
                account.getId(),
                account.getEmail(),
                account.getPassword(),
                mapToGrantedAuthorities(account.getAuthorities()),
                account.getLastPasswordResetDate(),
                account.isDeactivated()
        );
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName().name()))
                .collect(Collectors.toList());
    }
}
