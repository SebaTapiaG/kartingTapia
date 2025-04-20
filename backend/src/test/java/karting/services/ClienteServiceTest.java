package karting.services;

import karting.entities.clienteEntity;
import karting.repositories.clienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private clienteService clienteService;
    private clienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        clienteRepository = mock(clienteRepository.class); // Creamos un mock del repo
        clienteService = new clienteService();
        clienteService.clienteRepository = clienteRepository; // Inyectamos el mock
    }

    @Test
    void testGetNombreCliente() {
        clienteEntity mockCliente = new clienteEntity();
        mockCliente.setRut("12345678-9");
        mockCliente.setNombre("Juan Pérez");

        when(clienteRepository.findByRut("12345678-9")).thenReturn(Optional.of(mockCliente));

        String nombre = clienteService.getNombreCliente("12345678-9");

        assertEquals("Juan Pérez", nombre);
    }

    @Test
    void testGetNombreClienteNotFound() {
        when(clienteRepository.findByRut("98765432-1")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            clienteService.getNombreCliente("98765432-1");
        });

        assertEquals("Cliente no encontrado", exception.getMessage());
    }

    @Test
    void testGetCantidadReservas() {
        clienteEntity mockCliente = new clienteEntity();
        mockCliente.setRut("12345678-9");
        mockCliente.setCantidadReservas(5);

        when(clienteRepository.findByRut("12345678-9")).thenReturn(Optional.of(mockCliente));

        int cantidad = clienteService.getCantidadReservas("12345678-9");

        assertEquals(5, cantidad);
    }

    @Test
    void testGetCorreoCliente() {
        clienteEntity mockCliente = new clienteEntity();
        mockCliente.setRut("12345678-9");
        mockCliente.setCorreo("juan@example.com");

        when(clienteRepository.findByRut("12345678-9")).thenReturn(Optional.of(mockCliente));

        String correo = clienteService.getCorreoCliente("12345678-9");

        assertEquals("juan@example.com", correo);
    }

    @Test
    void testSaveCliente() {
        clienteEntity nuevoCliente = new clienteEntity();
        nuevoCliente.setRut("11111111-1");
        nuevoCliente.setNombre("Nuevo Cliente");

        when(clienteRepository.save(nuevoCliente)).thenReturn(nuevoCliente);

        clienteEntity resultado = clienteService.saveCliente(nuevoCliente);

        assertNotNull(resultado);
        assertEquals("Nuevo Cliente", resultado.getNombre());
    }

    @Test
    void testDeleteCliente() {
        doNothing().when(clienteRepository).deleteById("11111111-1");

        boolean eliminado = clienteService.deleteCliente("11111111-1");

        assertTrue(eliminado);
        verify(clienteRepository, times(1)).deleteById("11111111-1");
    }




}
