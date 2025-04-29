package karting.services;

import karting.entities.kartEntity;
import karting.repositories.kartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class KartServiceTest {

    @Mock
    private kartRepository kartRepository;

    @InjectMocks
    private kartService kartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetKarts() {
        // Arrange
        kartEntity kart1 = new kartEntity();
        kartEntity kart2 = new kartEntity();
        when(kartRepository.findAll()).thenReturn(Arrays.asList(kart1, kart2));
            // Act
            List<kartEntity> result = kartService.getKarts();

            // Assert
            assertEquals(2, result.size());
            verify(kartRepository, times(1)).findAll();
    }
    @Test
    void testGetKartById() {
        // Arrange
        kartEntity kart = new kartEntity();
        kart.setIdKart(1);
        when(kartRepository.findByIdKart(1)).thenReturn(kart);

        // Act
        kartEntity result = kartService.getKartById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getIdKart());
        verify(kartRepository, times(1)).findByIdKart(1);
    }

    @Test
    void testSaveKart() {
        // Arrange
        kartEntity kart = new kartEntity();
        when(kartRepository.save(kart)).thenReturn(kart);
        // Act
        kartEntity result = kartService.saveKart(kart);

        // Assert
        assertNotNull(result);
        verify(kartRepository, times(1)).save(kart);
    }

    @Test
    void testUpdateKart() {
        // Arrange
            kartEntity kart = new kartEntity();
            kart.setIdKart(2);
            when(kartRepository.save(kart)).thenReturn(kart);

            // Act
            kartEntity result = kartService.updateKart(kart);

            // Assert
            assertNotNull(result);
            assertEquals(2, result.getIdKart());
            verify(kartRepository, times(1)).save(kart);
    }

    @Test
    void testDeleteKart() {
            // Arrange
            int idKart = 3;
            doNothing().when(kartRepository).deleteById(idKart);

            // Act
            boolean result = kartService.deleteKart(idKart);

            // Assert
            assertTrue(result);
            verify(kartRepository, times(1)).deleteById(idKart);
    }
}


