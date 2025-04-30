package karting.services;

import karting.Dtos.ReportePersonasDTO;
import karting.entities.clienteEntity;
import karting.entities.comprobanteEntity;
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

    @Mock
    private comprobanteService comprobanteService;



    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetReservas() {
        reservaEntity r1 = new reservaEntity();
        reservaEntity r2 = new reservaEntity();

        when(reservaRepository.findAll()).thenReturn(Arrays.asList(r1, r2));

        List<reservaEntity> result = reservaService.getReservas();
        assertEquals(2, result.size());
        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    void testGetReservaByIdReserva() {
        reservaEntity reserva = new reservaEntity();
        reserva.setIdReserva(1L);

        when(reservaRepository.findByIdReserva(1L)).thenReturn(reserva);

        reservaEntity result = reservaService.getReservaByIdReserva(1L);
        assertNotNull(result);
        assertEquals(1L, result.getIdReserva());
        verify(reservaRepository).findByIdReserva(1L);
    }

    @Test
    void testSaveReserva() {
        reservaEntity reserva = new reservaEntity();
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        reservaEntity result = reservaService.saveReserva(reserva);
        assertNotNull(result);
        verify(reservaRepository).save(reserva);
    }

    @Test
    void testUpdateReserva() {
        reservaEntity reserva = new reservaEntity();
        when(reservaRepository.save(reserva)).thenReturn(reserva);

        reservaEntity result = reservaService.updateReserva(reserva);
        assertNotNull(result);
        verify(reservaRepository).save(reserva);
    }

    @Test
    void testDeleteReserva() {
        Long id = 1L;

        doNothing().when(reservaRepository).deleteById(id);

        boolean result = reservaService.deleteReserva(id);
        assertTrue(result);
        verify(reservaRepository).deleteById(id);
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
        // Setear el cumpleaños igual a hoy
        Date fechaCumple = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        cliente.setFechaNacimiento(fechaCumple);
        when(clienteRepository.findByRut(rut)).thenReturn(Optional.of(cliente));
        when(clienteService.getFechaNacimiento(rut)).thenReturn(fechaCumple);

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
        // Setear el cumpleaños igual a hoy
        Date fechaCumple = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());
        cliente.setFechaNacimiento(fechaCumple);
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
        when(clienteService.getFechaNacimiento(rut)).thenReturn(fechaCumple);
        when(comprobanteService.saveComprobante(any(comprobanteEntity.class)))
                .thenReturn(new comprobanteEntity()); // ← ESTA ES LA CORRECTA



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

    private Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, 0, 0, 0); // Mes -1 porque en Calendar enero=0
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    @Test
    void testObtenerIngresosPorVueltasYMese() {
        // Arrange
        Date inicio = createDate(2024, 1, 1);
        Date fin = createDate(2024, 3, 31);

        reservaEntity reserva1 = new reservaEntity();
        reserva1.setCantVueltas(10);
        reserva1.setMontoTotal(100.0);
        reserva1.setFechaReserva(createDate(2024, 1, 15));

        reservaEntity reserva2 = new reservaEntity();
        reserva2.setCantVueltas(15);
        reserva2.setMontoTotal(150.0);
        reserva2.setFechaReserva(createDate(2024, 2, 10));

        reservaEntity reserva3 = new reservaEntity();
        reserva3.setCantVueltas(20);
        reserva3.setMontoTotal(200.0);
        reserva3.setFechaReserva(createDate(2024, 3, 5));

        when(reservaRepository.findByFechaReservaBetween(inicio, fin))
                .thenReturn(Arrays.asList(reserva1, reserva2, reserva3));

        // Act
        Map<String, Map<String, Double>> resultado = reservaService.obtenerIngresosPorVueltasYMese(inicio, fin);

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.containsKey("10 vueltas o máx 10 min"));
        assertTrue(resultado.containsKey("15 vueltas o máx 15 min"));
        assertTrue(resultado.containsKey("20 vueltas o máx 20 min"));
        assertTrue(resultado.containsKey("TOTAL"));

        // Validar montos por categoría
        assertEquals(100.0, resultado.get("10 vueltas o máx 10 min").get("enero 2024"), 0.01);
        assertEquals(150.0, resultado.get("15 vueltas o máx 15 min").get("febrero 2024"), 0.01);
        assertEquals(200.0, resultado.get("20 vueltas o máx 20 min").get("marzo 2024"), 0.01);

        // Validar montos totales
        assertEquals(100.0, resultado.get("TOTAL").get("enero 2024"), 0.01);
        assertEquals(150.0, resultado.get("TOTAL").get("febrero 2024"), 0.01);
        assertEquals(200.0, resultado.get("TOTAL").get("marzo 2024"), 0.01);

        verify(reservaRepository, times(1)).findByFechaReservaBetween(inicio, fin);
    }

    @Test
    void testObtenerReporteAgrupado() {
        // Arrange
        Date inicio = createDate(2024, 1, 1);
        Date fin = createDate(2024, 1, 31);

        reservaEntity reserva1 = new reservaEntity();
        reserva1.setCantidadPersonas(2);
        reserva1.setMontoTotal(100.0);
        reserva1.setFechaReserva(createDate(2024, 1, 5));

        reservaEntity reserva2 = new reservaEntity();
        reserva2.setCantidadPersonas(5);
        reserva2.setMontoTotal(200.0);
        reserva2.setFechaReserva(createDate(2024, 1, 20));

        when(reservaRepository.findByFechaReservaBetween(inicio, fin))
                .thenReturn(Arrays.asList(reserva1, reserva2));

        // Act
        List<ReportePersonasDTO> resultado = reservaService.obtenerReporteAgrupado(inicio, fin);

        // Assert
        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());

        Set<String> rangos = new HashSet<>();
        for (ReportePersonasDTO dto : resultado) {
            rangos.add(dto.getRango());
        }

        assertTrue(rangos.contains("1-2 personas"));
        assertTrue(rangos.contains("3-5 personas"));


        verify(reservaRepository, times(1)).findByFechaReservaBetween(inicio, fin);
    }

    @Test
    void testGetRangosPosibles() {
        List<String> rangos = reservaService.getRangosPosibles();
        assertEquals(4, rangos.size());
        assertTrue(rangos.contains("1-2 personas"));
        assertTrue(rangos.contains("11-15 personas"));
    }

    @Test
    void testObtenerRango() {
        assertEquals("1-2 personas", reservaService.obtenerRango(1));
        assertEquals("1-2 personas", reservaService.obtenerRango(2));
        assertEquals("3-5 personas", reservaService.obtenerRango(3));
        assertEquals("6-10 personas", reservaService.obtenerRango(10));
        assertEquals("11-15 personas", reservaService.obtenerRango(11));
    }

    @Test
    void testCapitalize() {
        assertEquals("Hola", reservaService.capitalize("hola"));
        assertEquals("Hola", reservaService.capitalize("HOLA"));
        assertEquals("", reservaService.capitalize(""));
        assertNull(reservaService.capitalize(null));
    }
}

