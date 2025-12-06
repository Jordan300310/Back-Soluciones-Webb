package com.example.web.dto.venta;

import java.util.List;

public class CheckoutRequest {
    private List<VentaItemRequest> items;

    private String direccion;
    private String ciudad;
    private String pais;
    private String codigoPostal;

    public List<VentaItemRequest> getItems() {
        return items;
    }
    public void setItems(List<VentaItemRequest> items) {
        this.items = items;
    }
    public String getDireccion() {
        return direccion;
    }
    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public String getCiudad() {
        return ciudad;
    }
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }
    public String getPais() {
        return pais;
    }
    public void setPais(String pais) {
        this.pais = pais;
    }
    public String getCodigoPostal() {
        return codigoPostal;
    }
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
}
