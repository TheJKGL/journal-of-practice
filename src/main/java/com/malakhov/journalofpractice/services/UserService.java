package com.malakhov.journalofpractice.services;

import com.malakhov.journalofpractice.exception.ResourceAlreadyExistException;
import com.malakhov.journalofpractice.exception.ResourceNotFoundException;
import com.malakhov.journalofpractice.models.Credential;
import com.malakhov.journalofpractice.models.JwtResponse;
import com.malakhov.journalofpractice.models.Role;
import com.malakhov.journalofpractice.models.User;
import com.malakhov.journalofpractice.models.requests.SignUpRequest;
import com.malakhov.journalofpractice.repositories.CredentialsRepository;
import com.malakhov.journalofpractice.repositories.UserRepository;
import com.malakhov.journalofpractice.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private JwtUtils jwtUtils;
    private AuthenticationManager authenticationManager;
    private CredentialsRepository credentialsRepository;
    private PasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    public JwtResponse authenticate(Credential loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                ));

        String token = jwtUtils.generateJwtToken(authentication);
        
        Credential existedCredential = credentialsRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Credential does not exist"));

        return new JwtResponse(token, existedCredential.getEmail());
    }

    public void register(SignUpRequest signUpRequest) throws ResourceAlreadyExistException {
        checkIfEmailExist(signUpRequest.getEmail());

        Credential credential = new Credential();
        credential.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        credential.setEmail(signUpRequest.getEmail());

        User user = new User();
        user.setCredentials(credential);
        user.setRole(Role.ROLE_STUDENT);
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setSurname(signUpRequest.getSurname());

        credential.setUser(user);

        userRepository.save(user);
        credentialsRepository.save(credential);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void checkIfEmailExist(String email) throws ResourceAlreadyExistException {
        Optional<Credential> credential = credentialsRepository.findByEmail(email);
        if (credential.isPresent()) {
            throw new ResourceAlreadyExistException(
                    String.format("User with the same email {%s} already exist", email)
            );
        }
    }
}
