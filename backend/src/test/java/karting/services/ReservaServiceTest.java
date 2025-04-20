package karting.services;

import karting.entities.clienteEntity;
import karting.entities.reservaEntity;
import karting.repositories.clienteRepository;
import karting.repositories.reservaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReservaServiceTest {

    @Spy
    @InjectMocks
    private reservaService reservaService;

    @Mock
    private reservaRepository reservaRepository;

    @Mock
    private clienteRepository clienteRepository;

    @Mock
    private clienteService clienteService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void crearReserva_DeberiaGuardarReservaCorrectamente() {
        // Arrange
        String rut = "12345678-9";
        Date fecha = new Date();
        int personas = 5;
        double monto = 15000;

        clienteEntity cliente = new clienteEntity();
        cliente.setRut(rut);
        cliente.setCantidadReservas(1);
        when(clienteRepository.findByRut(rut)).thenReturn(Optional.of(cliente));

        // Act
        reservaEntity reserva = reservaService.crearReserva(rut, fecha, personas, monto);

        // Assert
        assertEquals("PENDIENTE", reserva.getEstado());
        assertEquals(10, reserva.getCantVueltas()); // porque monto=15000
        assertEquals(10, reserva.getTiempoMax());
        assertEquals(rut, reserva.getRutCliente());

        verify(reservaRepository, atLeastOnce()).save(any(reservaEntity.class));
        verify(clienteRepository, atLeastOnce()).save(any(clienteEntity.class));
    }

    @Test
    void crearReserva_FechaNula_DeberiaLanzarExcepcion() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            reservaService.crearReserva("12345678-9", null, 3, 20000);
        });
    }

    @Test
    void confirmarReserva_DeberiaConfirmarReserva() throws Exception {
        // Arrange
        Long reservaId = 1L;
        String rut = "12345678-9";
        Date fecha = new Date();

        // Cliente mock
        clienteEntity cliente = new clienteEntity();
        cliente.setRut(rut);
        cliente.setCantidadReservas(2);
        cliente.setFechaNacimiento(new Date()); // Hoy es su cumpleaños

        // Reserva mock
        reservaEntity reserva = new reservaEntity();
        reserva.setIdReserva(reservaId);
        reserva.setEstado("PENDIENTE");
        reserva.setRutCliente(rut);
        reserva.setFechaReserva(fecha);
        reserva.setCantidadPersonas(6); // >5, por lo tanto debería aplicar descuento por cantidad
        reserva.setMontoTotal(30000);


        // Mockeos
        when(reservaRepository.findById(reservaId)).thenReturn(Optional.of(reserva));
        when(clienteRepository.findByRut(rut)).thenReturn(Optional.of(cliente));

        // Act
        reservaService.confirmarReserva(reservaId);

        // Assert
        assertEquals("CONFIRMADA", reserva.getEstado());
        assertTrue(reserva.getMontoTotal() < 30000); // Aplicó al menos un descuento
        verify(reservaRepository).save(reserva);
    }

    // 👥 obtenerDescuentoXcant
    @Test
    void testObtenerDescuentoXcant_entre3y5() {
        reservaEntity reserva = new reservaEntity();
        reserva.setCantidadPersonas(4);

        Double descuento = reservaService.obtenerDescuentoXcant(reserva);
        assertEquals(0.1, descuento);
    }

    @Test
    void testObtenerDescuentoXcant_entre6y10() {
        reservaEntity reserva = new reservaEntity();
        reserva.setCantidadPersonas(7);

        Double descuento = reservaService.obtenerDescuentoXcant(reserva);
        assertEquals(0.2, descuento);
    }

    @Test
    void testObtenerDescuentoXcant_mayor10() {
        reservaEntity reserva = new reservaEntity();
        reserva.setCantidadPersonas(12);

        Double descuento = reservaService.obtenerDescuentoXcant(reserva);
        assertEquals(0.3, descuento);
    }

    @Test
    void testObtenerDescuentoXcant_menor3() {
        reservaEntity reserva = new reservaEntity();
        reserva.setCantidadPersonas(2);

        Double descuento = reservaService.obtenerDescuentoXcant(reserva);
        assertEquals(0.0, descuento);
    }

    // 📅 obtenerDescuentoXcumple
    @Test
    void testObtenerDescuentoXcumple_enCumple() {
        reservaEntity reserva = new reservaEntity();
        reserva.setRutCliente("12345678-9");

        // Setear el cumpleaños igual a hoy
        Date fechaCumple = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clienteService.getFechaNacimiento("12345678-9")).thenReturn(fechaCumple);

        Double descuento = reservaService.obtenerDescuentoXcumple(reserva);
        assertEquals(0.5, descuento);
    }

    @Test
    void testObtenerDescuentoXcumple_noCumple() {
        reservaEntity reserva = new reservaEntity();
        reserva.setRutCliente("12345678-9");

        // Setear el cumpleaños en una fecha distinta
        LocalDate otraFecha = LocalDate.of(1990, 1, 1); // Suponiendo hoy NO es 1 de enero
        Date fechaCumple = Date.from(otraFecha.atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clienteService.getFechaNacimiento("12345678-9")).thenReturn(fechaCumple);

        Double descuento = reservaService.obtenerDescuentoXcumple(reserva);
        assertEquals(0.0, descuento);
    }

    // 🔁 obtenerDescuentoXcl
    @Test
    void testObtenerDescuentoXcl_frecuente_entre2y4() {
        reservaEntity reserva = new reservaEntity();
        reserva.setRutCliente("11111111-1");

        when(clienteService.getCantidadReservas("11111111-1")).thenReturn(3);

        Double descuento = reservaService.obtenerDescuentoXcl(reserva);
        assertEquals(0.1, descuento);
    }

    @Test
    void testObtenerDescuentoXcl_frecuente_entre5y6() {
        reservaEntity reserva = new reservaEntity();
        reserva.setRutCliente("11111111-1");

        when(clienteService.getCantidadReservas("11111111-1")).thenReturn(6);

        Double descuento = reservaService.obtenerDescuentoXcl(reserva);
        assertEquals(0.2, descuento);
    }

    @Test
    void testObtenerDescuentoXcl_frecuente_mayor7() {
        reservaEntity reserva = new reservaEntity();
        reserva.setRutCliente("11111111-1");

        when(clienteService.getCantidadReservas("11111111-1")).thenReturn(8);

        Double descuento = reservaService.obtenerDescuentoXcl(reserva);
        assertEquals(0.3, descuento);
    }

    @Test
    void testObtenerDescuentoXcl_noFrecuente() {
        reservaEntity reserva = new reservaEntity();
        reserva.setRutCliente("11111111-1");

        when(clienteService.getCantidadReservas("11111111-1")).thenReturn(1);

        Double descuento = reservaService.obtenerDescuentoXcl(reserva);
        assertEquals(0.0, descuento);
    }

    @Test
    void testObtenerTarifaXdia_sabado() {
        reservaEntity reserva = new reservaEntity();
        // Suponemos un sábado, por ejemplo 19 de abril de 2025
        reserva.setFechaReserva(Date.from(LocalDate.of(2025, 4, 19).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        double tarifa = reservaService.obtenerTarifaXdia(reserva);
        assertEquals(1.2, tarifa);
    }

    @Test
    void testObtenerTarifaXdia_domingo() {
        reservaEntity reserva = new reservaEntity();
        // Suponemos un domingo, por ejemplo 20 de abril de 2025
        reserva.setFechaReserva(Date.from(LocalDate.of(2025, 4, 20).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        double tarifa = reservaService.obtenerTarifaXdia(reserva);
        assertEquals(1.2, tarifa);
    }

    @Test
    void testObtenerTarifaXdia_diaSemana() {
        reservaEntity reserva = new reservaEntity();
        // Suponemos un martes, por ejemplo 22 de abril de 2025
        reserva.setFechaReserva(Date.from(LocalDate.of(2025, 4, 22).atStartOfDay(ZoneId.systemDefault()).toInstant()));

        double tarifa = reservaService.obtenerTarifaXdia(reserva);
        assertEquals(1.0, tarifa);
    }

    @Test
    void testAplicarDescuento_descuentoXCantidadEsMayor() {
        reservaEntity reserva = new reservaEntity();
        clienteEntity cliente = new clienteEntity();
        cliente.setRut("12345678-9");

        // Setear el cumpleaños igual a hoy
        Date fechaCumple = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clienteService.getFechaNacimiento("12345678-9")).thenReturn(fechaCumple);

        reserva.setMontoTotal(100000);
        reserva.setCantidadPersonas(6); // activa descuento por cantidad
        reserva.setRutCliente("12345678-9"); // ASOCIAMOS EL CLIENTE A LA RESERVA ✅

        when(reservaService.obtenerDescuentoXcant(reserva)).thenReturn(0.3); // 30%
        when(reservaService.obtenerDescuentoXcl(reserva)).thenReturn(0.1);
        when(reservaService.obtenerDescuentoXcumple(reserva)).thenReturn(0.0);

        reservaService.aplicarDescuento(reserva);

        assertEquals(70000, reserva.getMontoTotal());
        assertEquals(0.3, reserva.getDescuento());
    }


    @Test
    void testAplicarDescuento_descuentoXClienteFrecuenteEsMayor() {
        reservaEntity reserva = new reservaEntity();
        clienteEntity cliente = new clienteEntity();
        cliente.setRut("12345678-9");

        // Setear el cumpleaños igual a hoy
        Date fechaCumple = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clienteService.getFechaNacimiento("12345678-9")).thenReturn(fechaCumple);

        reserva.setMontoTotal(100000);
        reserva.setRutCliente("12345678-9");

        when(reservaService.obtenerDescuentoXcant(reserva)).thenReturn(0.0);
        when(reservaService.obtenerDescuentoXcl(reserva)).thenReturn(0.5); // 50%
        when(reservaService.obtenerDescuentoXcumple(reserva)).thenReturn(0.2);

        reservaService.aplicarDescuento(reserva);

        assertEquals(50000, reserva.getMontoTotal());
        assertEquals(0.5, reserva.getDescuento());
    }

    @Test
    void testAplicarDescuento_descuentoXCumpleEsMayor() {
        reservaEntity reserva = new reservaEntity();
        clienteEntity cliente = new clienteEntity();
        cliente.setRut("12345678-9");

        // Setear el cumpleaños igual a hoy
        Date fechaCumple = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clienteService.getFechaNacimiento("12345678-9")).thenReturn(fechaCumple);

        reserva.setMontoTotal(100000);
        reserva.setRutCliente("12345678-9");

        when(reservaService.obtenerDescuentoXcant(reserva)).thenReturn(0.2);
        when(reservaService.obtenerDescuentoXcl(reserva)).thenReturn(0.3);
        when(reservaService.obtenerDescuentoXcumple(reserva)).thenReturn(0.4); // mayor

        reservaService.aplicarDescuento(reserva);

        assertEquals(60000, reserva.getMontoTotal());
        assertEquals(0.4, reserva.getDescuento());
    }

    @Test
    void testAplicarDescuento_sinDescuento() {
        reservaEntity reserva = new reservaEntity();
        reserva.setMontoTotal(100000);
        clienteEntity cliente = new clienteEntity();
        cliente.setRut("12345678-9");

        // Setear el cumpleaños igual a hoy
        Date fechaCumple = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        when(clienteService.getFechaNacimiento("12345678-9")).thenReturn(fechaCumple);

        reserva.setRutCliente("12345678-9");
        when(reservaService.obtenerDescuentoXcant(reserva)).thenReturn(0.0);
        when(reservaService.obtenerDescuentoXcl(reserva)).thenReturn(0.0);
        when(reservaService.obtenerDescuentoXcumple(reserva)).thenReturn(0.0);

        reservaService.aplicarDescuento(reserva);

        assertEquals(100000, reserva.getMontoTotal());
        assertEquals(0.0, reserva.getDescuento());
    }

}

