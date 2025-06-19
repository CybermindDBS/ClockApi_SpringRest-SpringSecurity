package com.example.springrest_refresher.restclient.clockwebapp.runners;

import com.example.springrest_refresher.restclient.clockwebapp.model.entity.MyUser;
import com.example.springrest_refresher.restclient.clockwebapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DBDataRunner implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Override
    public void run(String... args) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

        //Note: Roles are dummy for rest client, just included for an example.

        MyUser myUser = new MyUser();
        myUser.setUsername("user1");
        myUser.setPassword(bCryptPasswordEncoder.encode("pass123"));
        myUser.setRoles(Set.of("ROLE_VIEW_DATETIME", "ROLE_UPDATE_DATETIME"));
        myUser.setEnabled(true);
        userRepository.save(myUser);

        MyUser myUser2 = new MyUser();
        myUser2.setUsername("user2");
        myUser2.setPassword(bCryptPasswordEncoder.encode("pass123"));
        myUser2.setRoles(Set.of("ROLE_VIEW_DATETIME"));
        myUser2.setEnabled(true);
        userRepository.save(myUser2);

        MyUser myUser3 = new MyUser();
        myUser3.setUsername("user3");
        myUser3.setPassword(bCryptPasswordEncoder.encode("pass123"));
        myUser3.setRoles(Set.of("ROLE_UPDATE_DATETIME"));
        myUser3.setEnabled(true);
        userRepository.save(myUser3);
    }
}
