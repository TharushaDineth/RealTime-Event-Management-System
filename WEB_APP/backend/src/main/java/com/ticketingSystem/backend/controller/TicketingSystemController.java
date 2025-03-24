package com.ticketingSystem.backend.controller;

import com.ticketingSystem.backend.logic.TicketingSystem;
import com.ticketingSystem.backend.model.ConfigurationEntity;
import com.ticketingSystem.backend.repository.ConfigurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/system")  // Changed the base URL to /api/system
public class TicketingSystemController {

    private final TicketingSystem ticketingSystem;
    private final ConfigurationRepository configurationRepository;

    @Autowired
    public TicketingSystemController(TicketingSystem ticketingSystem, ConfigurationRepository configurationRepository) {
        this.ticketingSystem = ticketingSystem;
        this.configurationRepository = configurationRepository;
    }

    // Modified startSystem method to use query parameter for id
    @PostMapping("/start")
    public ResponseEntity<String> startSystem(@RequestParam Long id) {  // Use @RequestParam to fetch query parameter
        if (TicketingSystem.isSimulationRunning()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("System is already running.");
        }
        Optional<ConfigurationEntity> configuration = configurationRepository.findById(id);

        if (configuration.isPresent()) {
            ConfigurationEntity config = configuration.get();
            ticketingSystem.startSystem(config);  // Starts the system with the given configuration
            return ResponseEntity.status(HttpStatus.OK).body("Ticketing system started with configuration ID: " + id);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Configuration not found");
        }
    }

    @PostMapping("/stop")
    public ResponseEntity<String> stopSystem() {
        if (!TicketingSystem.isSimulationRunning()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("System is already stopped.");
        }
        TicketingSystem.stopSimulation();
        return ResponseEntity.status(HttpStatus.OK).body("Ticketing system stopped successfully");
    }
}