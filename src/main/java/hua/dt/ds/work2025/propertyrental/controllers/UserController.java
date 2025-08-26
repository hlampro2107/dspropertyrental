package hua.dt.ds.work2025.propertyrental.controllers;

import hua.dt.ds.work2025.propertyrental.entities.Role;
import hua.dt.ds.work2025.propertyrental.entities.User;
import hua.dt.ds.work2025.propertyrental.repositories.RoleRepository;
import hua.dt.ds.work2025.propertyrental.service.RoleService;
import hua.dt.ds.work2025.propertyrental.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;
    private final RoleService  roleService;

    public UserController(UserService userService, RoleRepository roleRepository, RoleService roleService) {
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @GetMapping("/admin-dashboard")
    public String showAllUsers(Model model){
        List<Role> roles =  roleService.getRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("users", userService.getUsers());
        return "/auth/users-dashboard";
    }

    @GetMapping("/filteruserrole")
    public String listUsers(@RequestParam(required = false) Long roleId, Model model) {
        List<User> users;
        if (roleId == null) {
            users = (List<User>) userService.getUsers();
        } else {
            users = userService.getUsersByRoleId(roleId);
        }
        List<Role> roles =  roleService.getRoles();
        model.addAttribute("users", users);
        model.addAttribute("roles", roles);
        return "/auth/users-dashboard";
    }

    @GetMapping("/register")
    public String register(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        // Λίστα με όλους τους διαθέσιμους ρόλους από τη βάση εκτός του admin
        List<Role> roles = roleRepository.findAll()
                .stream()
                .filter(role -> !role.getName().equals("ROLE_ADMIN"))
                .collect(Collectors.toList());
        model.addAttribute("roles", roles);
        return "auth/register";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute User user, @RequestParam("roleId") Integer roleId, Model model){
        System.out.println("Roles: " + user.getRole());
        Long id = userService.saveUser(user, roleId);
        String message = "User '"+id+"' saved successfully !";

        model.addAttribute("msg", message);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getPrincipal())) {

            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

            if (isAdmin) {
                return "redirect:/user/admin-dashboard";
            } else {
                return "redirect:/home";
            }

        } else {
            return "redirect:/home";
        }

    }

    @GetMapping("/new")
    public String createUser(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        List<Role> roles = roleRepository.findAll().stream().toList();
        model.addAttribute("roles", roles);
        model.addAttribute("showPasswordField", true);
        model.addAttribute("showRoles", true);
        model.addAttribute("formAction", "/user/saveUser");
        return "auth/userform";
    }

    // Επεξεργασία user
    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        model.addAttribute("showPasswordField", false);
        model.addAttribute("showRoles", false);
        model.addAttribute("formAction", "/user/update/" + id);
        return "/auth/userform";
    }

    // Διαγραφή user
    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/user/admin-dashboard";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@Valid @ModelAttribute("user") User user, Long id,
                             BindingResult userBindingResult){
        User existingUser = userService.getUser(id);
        user.setPassword(existingUser.getPassword());
        user.setRole(existingUser.getRole());
        userService.updateUser(user);
        return "redirect:/user/admin-dashboard";
    }

    @GetMapping("/tenant-dashboard")
    public String tenantDashboard(Model model,
                                  @AuthenticationPrincipal org.springframework.security.core.userdetails.User springUser) {

        if (springUser == null) {
            System.out.println("No user logged in!");
            return "redirect:/login";
        }

        User loggedInUser = userService.findByUsername(springUser.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        model.addAttribute("firstname", loggedInUser.getFirstname());
        model.addAttribute("surname", loggedInUser.getSurname());

        return "/auth/tenant-dashboard";
    }

}