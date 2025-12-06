package com.example.web.repository.venta;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.web.models.venta.CheckoutPendiente;

@Repository
public interface CheckoutPendienteRepository extends JpaRepository<CheckoutPendiente, Long> {

    Optional<CheckoutPendiente> findByPreferenceId(String preferenceId);

}
