package com.nuptsast.service;

import com.nuptsast.data.UserRepository;
import com.nuptsast.model.User;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private Logger logger = Logger.getLogger(getClass());

    private UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User register(User user) {
        String encodedPass = new BCryptPasswordEncoder()
            .encode(user.getPassword());
        user.setPassword(encodedPass);
        //    User newUser = new User(user.getUsername(), encodedPass, user.getTargetDepartment(), user.getPhoneNumber());
        logger.info(user);
        return userRepository.save(user);
    }

    @Override
    public User getUser(String username) {
        return userRepository.findOneByUsername(username);
    }

    @Override
    public User updateUser(User user) {
        logger.info("Saving User " + user);
        return userRepository.saveAndFlush(user);
    }
}
