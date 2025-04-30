package karting.services;

import jakarta.mail.internet.MimeMessage;
import karting.entities.comprobanteEntity;
import karting.repositories.comprobanteRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ComprobanteServiceTest {

    @InjectMocks
    private comprobanteService comprobanteService;

    @Mock
    private comprobanteRepository comprobanteRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper helper;

    @Test
    void testGetComprobantes() {
        List<comprobanteEntity> lista = Arrays.asList(new comprobanteEntity(), new comprobanteEntity());
        when(comprobanteRepository.findAll()).thenReturn(lista);

        List<comprobanteEntity> result = comprobanteService.getComprobantes();

        assertEquals(2, result.size());
    }

    @Test
    void testSaveComprobante() throws Exception {
        comprobanteEntity comprobante = new comprobanteEntity();
        comprobante.setIdReserva(1L);
        comprobante.setNombreCliente("Juan");
        comprobante.setCorreoCliente("juan@mail.com");
        comprobante.setFechaEmision(new Date());
        comprobante.setCantVueltas(5);
        comprobante.setTiempoMax(30);
        comprobante.setDescuento(2000);

        when(comprobanteRepository.save(comprobante)).thenReturn(comprobante);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        comprobanteEntity result = comprobanteService.saveComprobante(comprobante);

        assertEquals(comprobante, result);
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

    @Test
    void testDeleteComprobante() {
        Long id = 1L;
        boolean result = comprobanteService.deleteComprobante(id);

        assertTrue(result);
        verify(comprobanteRepository, times(1)).deleteById(id);
    }

    @Test
    void testGenerarComprobantePdf() throws Exception {
        comprobanteEntity comprobante = new comprobanteEntity();
        comprobante.setIdReserva(1L);
        comprobante.setNombreCliente("Pedro");
        comprobante.setFechaEmision(new Date());
        comprobante.setCantVueltas(5);
        comprobante.setTiempoMax(30);
        comprobante.setDescuento(2000);

        byte[] pdf = comprobanteService.generarComprobantePdf(comprobante);

        assertNotNull(pdf);
        assertTrue(pdf.length > 0);
    }


    @Test
    void testEnviarComprobantePorCorreo() throws Exception {
        // Arrange: Crear un comprobante con datos necesarios
        comprobanteEntity comprobante = new comprobanteEntity();
        comprobante.setIdReserva(123L);
        comprobante.setNombreCliente("Pedro");
        comprobante.setCorreoCliente("pedro@mail.com");
        comprobante.setFechaEmision(new Date());
        comprobante.setCantVueltas(3);
        comprobante.setTiempoMax(25);
        comprobante.setDescuento(1500);

        // Mocks necesarios
        MimeMessage mimeMessageMock = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessageMock);

        // Usamos un Spy para interceptar MimeMessageHelper real sin lanzar errores
        MimeMessageHelper helperSpy = spy(new MimeMessageHelper(mimeMessageMock, true));
        // Necesario si quieres testear internamente el helper (opcional)
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act & Assert: simplemente aseguramos que no se lanza excepción
        assertDoesNotThrow(() -> comprobanteService.enviarComprobantePorCorreo(comprobante));

        // Verificamos que el mail fue "enviado"
        verify(mailSender, times(1)).send(any(MimeMessage.class));
    }

}
