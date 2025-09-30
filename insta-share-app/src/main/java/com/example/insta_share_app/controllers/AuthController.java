package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.UserDTO;
import com.example.insta_share_app.entity.User;
import com.example.insta_share_app.entity.UserProfile;
import com.example.insta_share_app.repositories.UserProfileRepository;
import com.example.insta_share_app.repositories.UserRepository;
import com.example.insta_share_app.service.MapperService;
import com.example.insta_share_app.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class AuthController {
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    @Autowired
    private UserProfileRepository userProfileRepository;
 @Autowired
    private MapperService mapper;

    public AuthController(UserRepository userRepo, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<UserDTO> register(@RequestBody User user) {
        Optional<User> isExist=userRepo.findByUsername(user.getUsername());
        if (isExist.isPresent()){
            throw new RuntimeException("User exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // default role
        if(user.getRoles() == null || user.getRoles().isEmpty()) {
            user.getRoles().add("ROLE_USER");
        }
        // save user first
        User savedUser = userRepo.save(user);

        // create and attach profile
        UserProfile profile = new UserProfile();
        profile.setUser(savedUser);
        profile.setUserBio("New here 👋"); // optional default bio, dp, etc.
        profile.setProfileImageUrl("");
        // link both sides+        savedUser.setProfile(profile);

        // save again (because cascade might not trigger automatically)
        userProfileRepository.save(profile);


        return ResponseEntity.ok(mapper.toUserDTO(user));
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> req) {
        User user = userRepo.findByUsername(req.get("username"))
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (passwordEncoder.matches(req.get("password"), user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());


            return ResponseEntity.ok(Map.of(
                    "token", token,
                    "roles", user.getRoles()
            ));

        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
}
