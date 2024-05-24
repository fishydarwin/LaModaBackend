package com.github.fishydarwin.LaModaBackend.controller;

import com.github.fishydarwin.LaModaBackend.domain.Article;
import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.UserRole;
import com.github.fishydarwin.LaModaBackend.domain.validator.Validator;
import com.github.fishydarwin.LaModaBackend.manager.UserSessionManager;
import com.github.fishydarwin.LaModaBackend.repository.UserRepository;
import com.github.fishydarwin.LaModaBackend.repository.faker.UserFaker;
import com.github.fishydarwin.LaModaBackend.repository.hibernate.HUserRepository;
import com.github.fishydarwin.LaModaBackend.repository.hibernate.JPAUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;

@CrossOrigin
@RestController
public class UserController {

    private final UserRepository repository;

    public UserController(HUserRepository autowiredRepository) {
        repository = new JPAUserRepository(autowiredRepository);
        UserSessionManager.init();
    }

    @GetMapping("/user/all")
    public Collection<User> all() {
        return User.obfuscateAll(repository.all());
    }

    @GetMapping("/user/byId")
    public User byId(@RequestParam(value="id") long id) {
        return repository.byId(id).obfuscated();
    }

    @GetMapping("/user/any")
    public boolean any(@RequestParam(value="id") long id) {
        return repository.any(id);
    }

    @GetMapping("/user/idByDetails")
    public long idByDetails(@RequestParam(value="email") String email,
                            @RequestParam(value="passwordObfuscated") String passwordObfuscated) {
        return repository.idByDetails(email, passwordObfuscated);
    }

    @GetMapping("/user/generateSession")
    public String generateSession(@RequestParam(value="id") long id,
                                  @RequestParam(value="email") String email,
                                  @RequestParam(value="passwordObfuscated") String passwordObfuscated) {

        long foundId = repository.idByDetails(email, passwordObfuscated);
        if (foundId == -1)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        if (id != foundId)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        return "\"" + repository.generateSession(repository.byId(id)) + "\"";
    }

    @GetMapping("/user/bySession")
    public User bySession(@RequestParam(value="sessionId") String sessionId) {
        return repository.bySession(sessionId); // only place where we don't obfuscate!
    }

    @PostMapping("/user/add")
    public long add(@RequestBody User user) {
        String validation = Validator.validate(user);
        if (!validation.equals("OK"))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, validation);
        return repository.add(user);
    }

    @GetMapping("/user/anyByEmail")
    public boolean anyByEmail(@RequestParam(value="email") String email) {
        return repository.anyByEmail(email);
    }

    @PutMapping("/user/changeModerator/{email}")
    public boolean changeModerator(@PathVariable String email, @RequestParam String sessionId) {

        User sessionUser = UserSessionManager.bySession(sessionId);
        if (sessionUser == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        if (sessionUser.role() != UserRole.ADMIN)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");

        User user = repository.byEmail(email);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No known user with that email.");
        }
        if (sessionUser.id() == user.id())
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid operation! Cannot modify self role!");

        UserRole currentRole = repository.byId(user.id()).role();
        return repository.updateUserRole(user.id(), currentRole == UserRole.USER ? UserRole.MODERATOR : UserRole.USER);
    }

}
