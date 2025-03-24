package com.ticketingSystem.backend.controller;

import com.ticketingSystem.backend.model.ConfigurationEntity;
import com.ticketingSystem.backend.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/configs")
public class ConfigurationController {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @GetMapping("/getAll")
    public ResponseEntity<List<ConfigurationEntity>> getAllConfigurations() {
        List<ConfigurationEntity> configurations = configurationRepository.findAll();
        return ResponseEntity.ok(configurations);
    }

    @PostMapping("/save")
    public ResponseEntity<ConfigurationEntity> addConfiguration(@RequestBody ConfigurationEntity config) {
        try {
            ConfigurationEntity savedConfig = configurationRepository.save(config);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedConfig);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Modified deleteConfiguration method using query parameter
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteConfiguration(@RequestParam Long id) {
        if (!configurationRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuration with ID " + id + " not found.");
        }
        configurationRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Configuration with ID " + id + " has been deleted.");
    }

    // Modified getConfiguration method using query parameter
    @GetMapping("/get")
    public ResponseEntity<ConfigurationEntity> getConfiguration(@RequestParam Long id) {
        Optional<ConfigurationEntity> requestedConfig = configurationRepository.findById(id);
        if (requestedConfig.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(requestedConfig.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
