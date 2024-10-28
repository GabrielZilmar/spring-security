package com.zilmar.springsecurity.modules.session;

import com.zilmar.springsecurity.database.repositories.UserRepository;
import com.zilmar.springsecurity.modules.session.dto.LoginRequestDTO;
import com.zilmar.springsecurity.modules.session.dto.LoginResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/session")
@AllArgsConstructor
public class SessionController {
    private final JwtEncoder jwtEncoder;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @RequestBody LoginRequestDTO request
    ) {
        var user = this.userRepository.findOneByUsername(request.username());
        System.out.print(user);
        if (user.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "User not found."
            );
        }

        boolean isPasswordCorrect = this.bCryptPasswordEncoder.matches(request.password(), user.get().getPassword());
        if (!isPasswordCorrect) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password is invalid.");
        }


        var scopes = user.get().getRoles()
                .stream()
                .map(role -> role.getName().name())
                .collect(Collectors.joining(" "));

        var now = Instant.now();
        var EXPIRES_IN = 300L;
        var claims = JwtClaimsSet.builder()
                .issuer("mybackend")
                .subject(user.get().getId().toString())
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRES_IN))
                .claim("scope", scopes)
                .build();
        var jwtValue = this.jwtEncoder.encode(JwtEncoderParameters.from(claims));

        return ResponseEntity.ok(new LoginResponseDTO(jwtValue.getTokenValue(), EXPIRES_IN));
    }
}
