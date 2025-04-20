package karting.Dtos;

import java.util.LinkedHashMap;
import java.util.Map;

public class ReportePersonasDTO {
    private String rango;
    private Map<String, Double> montosPorMes;
    private double total;

    public ReportePersonasDTO() {
        this.montosPorMes = new LinkedHashMap<>();
    }

    public ReportePersonasDTO(String rango) {
        this.rango = rango;
        this.montosPorMes = new LinkedHashMap<>();
        this.total = 0;
    }

    public void agregarMonto(String mes, double monto) {
        this.montosPorMes.merge(mes, monto, Double::sum);
        this.total += monto;
    }

    // ✅ Getters y Setters requeridos por Jackson

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public Map<String, Double> getMontosPorMes() {
        return montosPorMes;
    }

    public void setMontosPorMes(Map<String, Double> montosPorMes) {
        this.montosPorMes = montosPorMes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}
