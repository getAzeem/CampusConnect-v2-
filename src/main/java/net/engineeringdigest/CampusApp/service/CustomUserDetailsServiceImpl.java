package net.engineeringdigest.CampusApp.service;

import net.engineeringdigest.CampusApp.repository.UserRepository;
import net.engineeringdigest.CampusApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user= userRepository.findByUsername(username);
        if(user!=null){
            UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .authorities(user.getRole().name())  // <-- Change from roles() to authorities()
                    .build();

            return userDetails;


        }
        throw new UsernameNotFoundException("User not found with enrollmentno: "+username );


    }
}
