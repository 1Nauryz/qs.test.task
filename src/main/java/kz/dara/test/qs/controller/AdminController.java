package kz.dara.test.qs.controller;

import kz.dara.test.qs.model.UserModel;
import kz.dara.test.qs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @GetMapping("/users")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public String indexPage(Model model){
        model.addAttribute("users", userService.getUsers());
        return "admin_page";
    }

    @GetMapping("users/{id}")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public String viewEditPage(@PathVariable(name = "id") Long id, Model model){
        model.addAttribute("user", userService.findUser(id));
        return "edit_user_page";
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public String editUser(@RequestParam(name = "user_full_name") String fullName,
                           @RequestParam(name = "user_email") String email,
                           @RequestParam(name = "user_password") String newPassword,
                           @RequestParam(name = "id") Long id) {
        userService.editUser(fullName, email, newPassword, id);
        return "redirect:/admin-page";
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("isAuthenticated() and hasAnyRole('ROLE_ADMIN')")
    public String deleteUser(@RequestParam(name = "id")Long id){
        userService.deleteUser(id);
        return "redirect:/admin-page";
    }
}
