package com.example.web.controller.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.web.dto.admin.DashboardResponse;
import com.example.web.service.admin.AdminDashboardService;
import com.example.web.service.auth.GuardService;

@RestController
@RequestMapping("/admin/dashboard")
public class AdminDashboardController {

    private final AdminDashboardService service;
    private final GuardService guard;

    public AdminDashboardController(AdminDashboardService service, GuardService guard) {
        this.service = service;
        this.guard = guard;
    }

    @GetMapping
    public DashboardResponse getDashboard(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        guard.requireAdmin(authHeader);
        return service.getDashboardForCurrentMonth();
    }
}
