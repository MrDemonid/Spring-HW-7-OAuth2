package mr.demonid.auth.server.configs;

import lombok.AllArgsConstructor;
import mr.demonid.auth.server.domain.User;
import mr.demonid.auth.server.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Сервис для получения данных о пользователе из БД.
 */
@Service
@AllArgsConstructor
public class UserDetailsServiceJpa implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new UsernameNotFoundException(String.format("Пользователь '%s' не найден!", username)));
    }

}

