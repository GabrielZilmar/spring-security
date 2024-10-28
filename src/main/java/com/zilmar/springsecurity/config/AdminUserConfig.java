package com.zilmar.springsecurity.config;

import com.zilmar.springsecurity.database.repositories.RoleRepository;
import com.zilmar.springsecurity.database.repositories.UserRepository;
import com.zilmar.springsecurity.modules.roles.enums.RolesValues;
import com.zilmar.springsecurity.modules.users.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Configuration
@AllArgsConstructor
public class AdminUserConfig implements CommandLineRunner {
    private RoleRepository roleRepository;
    private UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        var roleAdmin = this.roleRepository
                .findByName(RolesValues.ADMIN)
                .orElseThrow(() -> new Exception(RolesValues.ADMIN.name() + " not found"));


        var userAdmin = userRepository.findOneByUsername("admin");
        if (userAdmin.isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setPassword("123");
            user.setRoles(Set.of(roleAdmin));
            userRepository.save(user);
        }
    }
}
