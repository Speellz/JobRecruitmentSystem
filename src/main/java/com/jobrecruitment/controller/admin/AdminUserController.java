package com.jobrecruitment.controller.admin;

import com.jobrecruitment.model.User;
import com.jobrecruitment.service.common.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        List<User.Role> roles = Arrays.asList(User.Role.values());
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "admin/users";
    }

    @PostMapping("/users/create")
    public String createUser(@RequestParam String name,
                             @RequestParam String email,
                             @RequestParam String password,
                             @RequestParam String role,
                             RedirectAttributes redirectAttributes) {
        try {
            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(User.Role.valueOf(role));
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "User created successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create user.");
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/delete/{id}")
    @Transactional
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);  // artık direkt Long alıyor
            redirectAttributes.addFlashAttribute("success", "User deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete user.");
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        List<User.Role> roles = Arrays.asList(User.Role.values());
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "admin/edit_user";
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute User user,
                             @RequestParam(required = false) String newPassword,
                             RedirectAttributes redirectAttributes) {
        try {
            if (newPassword != null && !newPassword.isBlank()) {
                user.setPassword(passwordEncoder.encode(newPassword));
            } else {
                User existing = userService.getUserById(user.getId());
                user.setPassword(existing.getPassword());
            }
            userService.saveUser(user);
            redirectAttributes.addFlashAttribute("success", "User updated successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update user.");
        }
        return "redirect:/admin/users";
    }
}
