package com.dailymuse.microserviceauthentication.config;

import com.dailymuse.microserviceauthentication.user.User;
import com.dailymuse.microserviceauthentication.user.settings.Settings;

public class DefaultSettings {
    public static Settings getDefaultSettings(User user) {
        return Settings
                .builder()
                .user(user)
                .name("Владелец")
                .color((byte) 1)
                .fontGui((byte) 1)
                .fontNotes((byte) 1)
                .build();
    }
}
