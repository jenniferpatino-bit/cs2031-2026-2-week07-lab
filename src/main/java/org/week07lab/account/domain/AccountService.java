package org.week07lab.account.domain;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.week07lab.account.dto.RegisterRequest;
import org.week07lab.account.dto.RegisterResponse;
import org.week07lab.account.infrastructure.AccountRepository;
import org.week07lab.exception.ConflictException;

@Service
public class AccountService implements UserDetailsService {

    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(AccountRepository accountRepository, PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (accountRepository.existsByEmail(email)) {
            throw new ConflictException("Ya existe un usuario registrado con ese email");
        }
        Account account = accountRepository.save(new Account(
                email,
                passwordEncoder.encode(request.password()),
                request.firstName().trim(),
                request.lastName().trim(),
                Role.USER
        ));
        return new RegisterResponse(account.getId());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return accountRepository.findByEmail(username.trim().toLowerCase())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
