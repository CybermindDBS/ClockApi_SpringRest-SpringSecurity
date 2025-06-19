package com.example.springrest_refresher.restclient.clockwebapp.security;

import com.example.springrest_refresher.restclient.clockwebapp.model.entity.MyUser;
import com.example.springrest_refresher.restclient.clockwebapp.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyUser myUser = userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found : " + username));
        return new MyUserDetails(myUser);
    }
}
