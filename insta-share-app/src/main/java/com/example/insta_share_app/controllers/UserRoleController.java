package com.example.insta_share_app.controllers;

import com.example.insta_share_app.dtos.BanRequest;
import com.example.insta_share_app.dtos.UserDTO;
import com.example.insta_share_app.entity.Ban;
import com.example.insta_share_app.entity.User;
import com.example.insta_share_app.entity.UserProfile;
import com.example.insta_share_app.repositories.BanRepository;
import com.example.insta_share_app.repositories.UserProfileRepository;
import com.example.insta_share_app.repositories.UserRepository;
import com.example.insta_share_app.service.MapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class UserRoleController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BanRepository banRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private MapperService mapper;

    // ====== BAN USER ======
    @PutMapping("/ban/{userid}")
    public ResponseEntity<UserDTO> banUser(@PathVariable String userid, Principal principal, @RequestBody BanRequest request) {
        User banUser = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found"));
        User admin = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Ban ban = new Ban();
        ban.setReason(request.getReason() != null ? request.getReason() : "You have been caught for inappropriate behaviour");
        ban.setBannedAt(LocalDateTime.now());
        ban.setBannedBy(admin);
        ban.setUser(banUser);
        ban.setExpiresAt(ban.getBannedAt().plusDays(request.getDaysBanned() != 0 ? request.getDaysBanned() : 7));

        banUser.getBanHistory().add(ban);

        banRepository.save(ban);
        userRepository.save(banUser); // Cascade will handle profile automatically

        return ResponseEntity.ok(mapper.toUserDTO(banUser));
    }

    // ====== UNBAN USER ======
    @PutMapping("/unban/{userid}")
    public ResponseEntity<UserDTO> unbanUser(@PathVariable String userid, Principal principal) {
        User banUser = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found"));
        User admin = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        Optional<Ban> activeBan = banRepository.findByUserId(userid);
        if (activeBan.isPresent()) {
            banUser.setBanHistory(
                    banUser.getBanHistory().stream()
                            .filter(b -> !b.equals(activeBan.get()))
                            .collect(Collectors.toList())
            );
            banRepository.delete(activeBan.get());
            userRepository.save(banUser); // cascade handles profile
        }

        return ResponseEntity.ok(mapper.toUserDTO(banUser));
    }

    // ====== VERIFY USER ======
//    @PutMapping("/verify/{userid}")
//    public ResponseEntity<UserDTO> verifyUser(@PathVariable String userid, Principal principal) {
//        User user = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found"));
//        User admin = userRepository.findByUsername(principal.getName())
//                .orElseThrow(() -> new RuntimeException("Admin not found"));
//
//        user.setVarified(true);
//        user.getProfile().setVarifiedBy(admin); // update existing profile
//
//        userRepository.save(user); // cascade handles profile
//        return ResponseEntity.ok(mapper.toUserDTO(user));
//    }


    @PutMapping("/verify/{userid}")
    public ResponseEntity<UserDTO> verifyUser(@PathVariable String userid, Principal principal) {
        User user = userRepository.findById(userid)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User admin = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new RuntimeException("Admin not found"));

        user.setVarified(true);

        UserProfile profile = user.getProfile();
        if (profile == null) {
            throw new RuntimeException("User profile does not exist for this user");
        }


        // explicitly save profile to avoid re-insert
        userProfileRepository.save(profile);

        return ResponseEntity.ok(mapper.toUserDTO(user));
    }


    // ====== UNVERIFY USER ======
    @PutMapping("/unverify/{userid}")
    public ResponseEntity<UserDTO> unverifyUser(@PathVariable String userid, Principal principal) {
        User user = userRepository.findById(userid).orElseThrow(() -> new RuntimeException("User not found"));

        user.setVarified(false);
        

        userRepository.save(user); // cascade handles profile
        return ResponseEntity.ok(mapper.toUserDTO(user));
    }
}
