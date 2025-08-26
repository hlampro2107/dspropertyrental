package hua.dt.ds.work2025.propertyrental.service;

import hua.dt.ds.work2025.propertyrental.entities.Role;
import hua.dt.ds.work2025.propertyrental.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }
}
