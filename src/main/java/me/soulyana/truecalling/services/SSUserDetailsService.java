package me.soulyana.truecalling.services;

import me.soulyana.truecalling.models.Person;
import me.soulyana.truecalling.models.Role;
import me.soulyana.truecalling.repositories.PersonRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Set;

@Service
public class SSUserDetailsService implements UserDetailsService {
    private PersonRepository personRepository;

    public SSUserDetailsService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Person user = personRepository.findByUsername(username);
            if(user == null) {
                System.out.println("Username not detected for this user: " + user.getUsername());
            return null;
            }
        System.out.println("Detected this user inside SSUserDetailsService... " + user.getUsername());
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), getAuthorities(user));
        } catch (Exception e) {
            throw new UsernameNotFoundException("The user was not found (in SSUserDetailsService)");
        }
    }

    private Set<GrantedAuthority> getAuthorities(Person user) {
        Set<GrantedAuthority> authorities = new HashSet<>();

        for(Role role : user.getRoles()) {
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRole());
            authorities.add(grantedAuthority);
        }
        System.out.println("Detected user authorities in SSUserDetailsService: " + authorities.toString());
        return authorities;
    }
}
