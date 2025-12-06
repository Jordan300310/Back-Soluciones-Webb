package com.example.web.controller.admin;
import java.util.List;
import org.springframework.graphql.data.method.annotation.Argument; 
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import com.example.web.config.AuthorizationInterceptor;
import com.example.web.dto.admin.DashboardResponse;
import com.example.web.dto.admin.TopProductoDTO; 
import com.example.web.service.admin.AdminDashboardService;
import com.example.web.service.auth.GuardService;
@Controller
public class AdminDashboardResolver {

    private final AdminDashboardService service;
    private final GuardService guard;

    public AdminDashboardResolver(AdminDashboardService service, GuardService guard) {
        this.service = service;
        this.guard = guard;
    }

    @QueryMapping
    public DashboardResponse adminDashboard(
            @Argument String fechaInicio,  
            @Argument String fechaFin,    
            @ContextValue(name = AuthorizationInterceptor.AUTH_CONTEXT_KEY) String authHeader
    ) {
        guard.requireAdmin(authHeader);
        return service.getDashboardData(fechaInicio, fechaFin);
    }

    @QueryMapping
    public List<TopProductoDTO> detalleCategoria(
            @Argument String categoria,
            @Argument String fechaInicio, 
            @Argument String fechaFin,
            @ContextValue(name = AuthorizationInterceptor.AUTH_CONTEXT_KEY) String authHeader
    ) {
        guard.requireAdmin(authHeader);
        return service.getDetalleCategoria(categoria, fechaInicio, fechaFin);
    }
}