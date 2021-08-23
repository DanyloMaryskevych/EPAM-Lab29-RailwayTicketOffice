package com.epam.user.controller;

import com.epam.user.entity.User;
import com.epam.user.service.UserService;
import com.epam.user.util.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public String newUser(@RequestBody User user) {
        userService.saveUser(user);
        return jwtUtil.generateToken(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return jwtUtil.generateToken(user);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable("id") Long id, @RequestBody User user) {
        user.setId(id);
        userService.saveUser(user);
        return user;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello!";
    }
}
