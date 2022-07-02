package com.javabegin.tasklist.backendspringboot.service;

import com.javabegin.tasklist.backendspringboot.entity.UserEntity;
import com.javabegin.tasklist.backendspringboot.repo.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    public UserDetailsServiceImpl(UserRepository userRepository,BCryptPasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        return findUserByLogin(userName);
    }

    public UserEntity findUserByLogin(String login){
        Optional<UserEntity> user = userRepository.findByLogin(login);

        user.orElseThrow(() -> new UsernameNotFoundException(login + "not found"));

        return user.get();
    }

    public UserEntity add(UserEntity user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public UserEntity update(UserEntity user){
        user.setPassword(encoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public void deleteByID(Integer id){
        userRepository.deleteById(id);
    }

    public List<UserEntity> findAll(){
        return userRepository.findAll();
    }

    public Boolean existByLogin(String login){
        return userRepository.existsUserEntityByLogin(login);
    }

    public void deleteByLogin(String login){
        userRepository.deleteByLogin(login);
    }

    public void changePassword(String login, String password){
        userRepository.changePassword(login, encoder.encode(password));
    }

    public void changeRole(String login, String role){
        userRepository.changeRole(login, role);
    }

}
