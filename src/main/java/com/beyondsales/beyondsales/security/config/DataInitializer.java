package com.beyondsales.beyondsales.security.config;

import com.beyondsales.beyondsales.entity.Role;
import com.beyondsales.beyondsales.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        createRoleIfNotExists("ROLE_ADMIN", "Administrateur du système");
        createRoleIfNotExists("ROLE_USER", "Utilisateur standard");
    }

    private void createRoleIfNotExists(String roleName, String description) {
        roleRepository.findByName(roleName).or(() -> {
            Role role = new Role();
            role.setName(roleName);
            role.setDescription(description);
            roleRepository.save(role);
            return java.util.Optional.of(role);
        });
    }
}
