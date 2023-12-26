package com.dailymuse.microserviceuser.user.settings;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SettingsService {
    private final SettingsRepository settingsRepository;

    public Settings getSettingsByUserId(Long userId) {
        return settingsRepository.findByUserId(userId);
    }

    public void changeName(String name, Long userId) {
        Settings settings = settingsRepository.findByUserId(userId);
        settings.setName(name);
        settingsRepository.save(settings);
    }

    public void changeColor (Byte color, Long userId) {
        Settings settings = settingsRepository.findByUserId(userId);
        settings.setColor(color);
        settingsRepository.save(settings);
    }

    public void changeFontGui (Byte fontGui, Long userId) {
        Settings settings = settingsRepository.findByUserId(userId);
        settings.setFontGui(fontGui);
        settingsRepository.save(settings);
    }

    public void changeFontNotes (Byte fontNotes, Long userId) {
        Settings settings = settingsRepository.findByUserId(userId);
        settings.setFontNotes(fontNotes);
        settingsRepository.save(settings);
    }
}
