package com.dailymuse.microserviceuser.user.settings;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Integer> {
    Settings findByUserId(Long userId);
}
