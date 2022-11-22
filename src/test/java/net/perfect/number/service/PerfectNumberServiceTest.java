package net.perfect.number.service;

import net.perfect.number.exception.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class PerfectNumberServiceTest {

    @InjectMocks
    PerfectNumberService perfectNumberService;

    @Test
    void checkForPerfectNumber_withValidNumber_ok() {
        assertEquals(true, perfectNumberService.checkForPerfectNumber(6));
    }

    @Test
    void checkForPerfectNumber_withValidNumber_false() {
        assertEquals(false, perfectNumberService.checkForPerfectNumber(10));
    }


    @Test
    void listAllPerfectNumber_withValidRange_ok() {
        assertEquals(Arrays.asList(1, 6, 28), perfectNumberService.listAllPerfectNumber(1, 100));
    }

    @Test
    void listAllPerfectNumber_withInValidRange_ok() {
        assertThrows(InvalidRequestException.class, () -> perfectNumberService.listAllPerfectNumber(100, 1));
    }

    @Test
    void listAllPerfectNumber_withValidRange_expectEmptyList() {
        assertEquals(Arrays.asList(), perfectNumberService.listAllPerfectNumber(10, 20));
    }
}
