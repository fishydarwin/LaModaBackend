package com.github.fishydarwin.LaModaBackend.controller;

import com.github.fishydarwin.LaModaBackend.domain.User;
import com.github.fishydarwin.LaModaBackend.domain.validator.Validator;
import com.github.fishydarwin.LaModaBackend.repository.UserRepository;
import com.github.fishydarwin.LaModaBackend.repository.memory.InMemoryUserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
public class UserController {

    private final UserRepository repository = new InMemoryUserRepository();

    //TODO: Remove bad security pure User mapping, use ConservativeUser which has only required details.

    @GetMapping("/user/all")
    public Collection<User> all() {
        return repository.all();
    }

    @GetMapping("/user/byId")
    public User byId(@RequestParam(value="id") long id) {
        return repository.byId(id);
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
    public String generateSession(@RequestParam(value="id") long id) {
        //TODO: authenticate user first
        return "\"" + repository.generateSession(repository.byId(id)) + "\"";
    }

    @GetMapping("/user/bySession")
    public User bySession(@RequestParam(value="sessionId") String sessionId) {
        return repository.bySession(sessionId);
    }

    @PostMapping("/user/add")
    public long add(@RequestBody User user) {
        String validation = Validator.validate(user);
        if (!validation.equals("OK"))
            return -1;
        return repository.add(user);
    }

    @GetMapping("/user/anyByEmail")
    public boolean anyByEmail(@RequestParam(value="email") String email) {
        return repository.anyByEmail(email);
    }

}
