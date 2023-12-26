package com.dailymuse.microserviceuser.user;

import com.dailymuse.microserviceuser.user.settings.Settings;
import com.dailymuse.microserviceuser.user.settings.SettingsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final SettingsService settingsService;

    @GetMapping(path = "email")
    public ResponseEntity<?> getEmail(
            @RequestHeader("x-user-id") Long userId
    ) {
        String email = userService.getUser(userId).getEmail();
        return ResponseEntity.ok(email);
    }

    @GetMapping(path = "settings")
    public ResponseEntity<?> getSettings(
            @RequestHeader("x-user-id") Long userId
    ) {
        Settings settings  = settingsService.getSettingsByUserId(userId);
        return ResponseEntity.ok(settings);
    }

    @PutMapping(path = "settings/name")
    public ResponseEntity<?> changeUserName(
            @RequestHeader("x-user-id") Long userId,
            @RequestParam String value
            ) {
        settingsService.changeName(
                value,
                userId
        );
        return ResponseEntity.ok("Name is changed");
    }

    @PutMapping(path = "settings/color")
    public ResponseEntity<?> changeUserColor(
            @RequestHeader("x-user-id") Long userId,
            @RequestParam Byte value
    ) {
        settingsService.changeColor(
                value,
                userId
        );
        return ResponseEntity.ok("Color is changed");
    }

    @PutMapping(path = "settings/font_gui")
    public ResponseEntity<?> changeUserFontGui(
            @RequestHeader("x-user-id") Long userId,
            @RequestParam Byte value
    ) {
        settingsService.changeFontGui(
                value,
                userId
        );
        return ResponseEntity.ok("Font for GUI is changed");
    }
    @PutMapping(path = "settings/font_notes")
    public ResponseEntity<?> changeUserNotes(
            @RequestHeader("x-user-id") Long userId,
            @RequestParam Byte value
    ) {
        settingsService.changeFontNotes(
                value,
                userId
        );
        return ResponseEntity.ok("Font for notes is changed");
    }
}
