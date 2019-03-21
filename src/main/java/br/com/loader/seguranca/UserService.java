package br.com.loader.seguranca;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User usuario = this.usuarioRepository.findByEmail(email);

        UserBuilder builder = null;
        if (usuario != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(usuario.getEmail());
            builder.password(usuario.getPassword());

            List<GrantedAuthority> listaDePermissoes = new ArrayList<>();

            usuario.getPermissoes().stream().forEach((permissao) -> {
                GrantedAuthority perm = new SimpleGrantedAuthority("ROLE_" + permissao.getDescription());
                listaDePermissoes.add(perm);
            });

            builder.authorities(listaDePermissoes);
        } else {
            throw new UsernameNotFoundException("Usuário não encontrado!!");
        }

        return builder.build();
    }

}
