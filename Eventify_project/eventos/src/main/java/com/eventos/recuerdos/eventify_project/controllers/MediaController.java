package com.eventos.recuerdos.eventify_project.controllers;

import com.eventos.recuerdos.eventify_project.user.domain.UserAccount;
import com.eventos.recuerdos.eventify_project.controllers.StorageService;
import com.eventos.recuerdos.eventify_project.user.domain.UserAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/media")
public class MediaController {
    @Autowired
    private UserAccountService userService;

    @Autowired
    private StorageService storageService;


    @PostMapping("/profile-pic")
    public ResponseEntity<?> uploadProfilePic(@RequestBody MultipartFile file, Principal principal) throws Exception {
        String username = principal.getName();

        String objectKey = "profile-pics/" + username;
        String fileKey = storageService.uploadFile(file, objectKey);

        UserAccount user = userService.findByEmail(username);
        user.setProfilePictureKey(fileKey);
        userService.saveOther(user);

        return ResponseEntity.ok("Profile picture uploaded successfully");
    }


    @GetMapping("/profile-pic")
    public ResponseEntity<String> getProfilePic(Principal principal) {
        UserAccount user = userService.findByEmail(principal.getName());

        if (user.getProfilePictureKey() == null)
            return ResponseEntity.notFound().build();

        String presignedUrl = storageService.generatePresignedUrl(user.getProfilePictureKey());
        return ResponseEntity.ok(presignedUrl);
    }

    @DeleteMapping("/profile-pic")
    public ResponseEntity<String> deleteProfilePic(Principal principal) {
        UserAccount user = userService.findByEmail(principal.getName());

        if (user.getProfilePictureKey() == null)
            return ResponseEntity.badRequest().body("Profile picture not found");

        storageService.deleteFile(user.getProfilePictureKey());
        user.setProfilePictureKey(null);
        userService.save(user);

        return ResponseEntity.ok("Profile picture deleted successfully");
    }
}
