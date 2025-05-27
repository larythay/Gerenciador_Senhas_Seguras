package tech.buildrun.springsecurity.controller;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import tech.buildrun.springsecurity.controller.dto.CreateUserDto;
import tech.buildrun.springsecurity.entities.Role;
import tech.buildrun.springsecurity.entities.User;
import tech.buildrun.springsecurity.repository.RoleRepository;
import tech.buildrun.springsecurity.repository.UserRepository;
import tech.buildrun.springsecurity.service.PasswordGeneratorService;
import tech.buildrun.springsecurity.service.PasswordValidationService;

import java.security.MessageDigest;
import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final PasswordGeneratorService passwordGeneratorService;
    private final PasswordValidationService passwordValidationService;

    public UserController(UserRepository userRepository,
                          RoleRepository roleRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          PasswordGeneratorService passwordGeneratorService,
                          PasswordValidationService passwordValidationService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordGeneratorService = passwordGeneratorService;
        this.passwordValidationService = passwordValidationService;
    }

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<Void> newUser(@RequestBody CreateUserDto dto) {

        var basicRole = roleRepository.findByName(Role.Values.BASIC.name());
        var userFromDb = userRepository.findByUsername(dto.username());

        if(userFromDb.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY);
        }

        var user = new User();
        user.setUsername(dto.username());
        String senha;
        if (dto.gerarSenhaAleatoria()) {
            senha = passwordGeneratorService.generateStrongPassword(12);
            passwordGeneratorService.gerarSenhaEEnviar(dto.username());
        } else {
            senha = dto.password();
        }

        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = sha1.digest(senha.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02X", b));
            }
            String sha1Hash = sb.toString();
            String prefix = sha1Hash.substring(0, 5);
            String suffix = sha1Hash.substring(5);

            boolean pwned = passwordValidationService.isPasswordPwned(prefix, suffix);
            if (pwned) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Senha já foi exposta em vazamentos. Escolha outra senha.");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao validar senha.", e);
        }

        user.setPassword(passwordEncoder.encode(senha));
        user.setRoles(Set.of(basicRole));

        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('SCOPE_ADMIN')")
    public ResponseEntity<List<User>> listUsers() {
        var users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }
}
