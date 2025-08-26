package hua.dt.ds.work2025.propertyrental.service;

import hua.dt.ds.work2025.propertyrental.entities.Role;
import hua.dt.ds.work2025.propertyrental.entities.User;
import hua.dt.ds.work2025.propertyrental.repositories.PropertyForRentRepository;
import hua.dt.ds.work2025.propertyrental.repositories.RentalApplicationRepository;
import hua.dt.ds.work2025.propertyrental.repositories.RoleRepository;
import hua.dt.ds.work2025.propertyrental.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private BCryptPasswordEncoder passwordEncoder;

    private RentalApplicationRepository rentalApplicationRepository;

    private PropertyForRentRepository propertyForRentRepository;


    public UserService(UserRepository userRepository, RoleRepository roleRepository,
                       RentalApplicationRepository rentalApplicationRepository,
                       PropertyForRentRepository propertyForRentRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.rentalApplicationRepository = rentalApplicationRepository;
        this.propertyForRentRepository = propertyForRentRepository;
    }

    @Transactional
    public Long saveUser(User user, @RequestParam("roleId") Integer roleId) {
        String passwd= user.getPassword();
        String encodedPassword = passwordEncoder.encode(passwd);
        user.setPassword(encodedPassword);

        Role selectedRole = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(selectedRole);
        System.out.println("Password length: " + user.getPassword().length());
        user = userRepository.save(user);
        return user.getId();
    }

    @Transactional
    public Long updateUser(User user) {
        user = userRepository.save(user);
        return user.getId();
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> opt = userRepository.findByUsername(username);

        if(opt.isEmpty())
            throw new UsernameNotFoundException("User with username: " + username + " not found !");

        User user = opt.get();

        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singleton(authority) // μοναδικός ρόλος
        );

    }

    @Transactional
    public Object getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public User getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    @Transactional
    public Optional<User> getUserById(Long userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
    public void deleteUser(long Id) {
        rentalApplicationRepository.deleteByTenantId(Id);

        userRepository.deleteById(Id);
    }

    public List<User> getUsersByRoleId(Long roleId) {
        return userRepository.findByRoleId(roleId);
    }

}
