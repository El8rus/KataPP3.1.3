package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listUsers(Model model, Principal principal, @ModelAttribute("newUser") User user) {
        model.addAttribute("users", userService.listUsers());
        model.addAttribute("admin", userService.findByUsername(principal.getName()));
        model.addAttribute("roles", roleRepository.findAll());
        return "admin_panel";
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute("newUser") User user,
                          @RequestParam("roles") Set<Role> roles) {
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @PatchMapping("/updateUser/{id}")
    public String saveUser(User user, @RequestParam("roles") Set<Role> roles) {
        user.setRoles(roles);
        userService.save(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/deleteUser/{id}")
    public String deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
