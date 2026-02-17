package com.challangersop.challanger_sop.controllers;

import com.challangersop.challanger_sop.entities.UserEntity;
import com.challangersop.challanger_sop.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static class CreateUserRequest {
        private String name;

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @PostMapping
    public UserEntity create(@RequestBody CreateUserRequest body) {
        UserEntity user = new UserEntity(body.getName());
        return userRepository.save(user);
    }

    @GetMapping
    public List<UserEntity> list() {
        return userRepository.findAll();
    }
}
