package de.jinba.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {
    @GetMapping
    public ResponseEntity<String> viewDashboard() {
        return ResponseEntity.ok("Dashboard");
    }
}
