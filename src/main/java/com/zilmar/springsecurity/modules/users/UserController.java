package com.zilmar.springsecurity.modules.users;

import com.zilmar.springsecurity.database.repositories.RoleRepository;
import com.zilmar.springsecurity.database.repositories.UserRepository;
import com.zilmar.springsecurity.modules.roles.entities.Role;
import com.zilmar.springsecurity.modules.roles.enums.RolesValues;
import com.zilmar.springsecurity.modules.users.dto.CreateUserRequestDTO;
import com.zilmar.springsecurity.modules.users.dto.CreateUserResponseDTO;
import com.zilmar.springsecurity.modules.users.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @PostMapping
    public ResponseEntity<CreateUserResponseDTO> createUser(
            @RequestBody CreateUserRequestDTO requestBody
    ) {
        Role basicRole = roleRepository
                .findByName(RolesValues.BASIC)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, RolesValues.BASIC.name() + " not found"));

        var userFromDb = userRepository.findOneByUsername(requestBody.username());
        if (userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "User already exists");
        }

        User newUser = new User();
        newUser.setUsername(requestBody.username());
        newUser.setPassword(requestBody.password());
        newUser.setRoles(Set.of(basicRole));
        userRepository.save(newUser);

        return ResponseEntity.ok(new CreateUserResponseDTO(newUser));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
