package com.jubasbackend.service.appointment;

import com.jubasbackend.domain.entity.Appointment;
import com.jubasbackend.domain.entity.enums.AppointmentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CancelAppointmentTest extends AbstractAppointmentServiceTest {

    UUID appointmentId = UUID.randomUUID();

    @Test
    @DisplayName("Deve lançar uma exceção quando o agendamento não exite.")
    void shouldThrowExceptionWhenAppointmentDoesNotExist() {
        //ARRANGE
        doReturn(Optional.empty()).when(appointmentRepository).findById(uuidCaptor.capture());

        //ACT & ASSERT
        var exception = assertThrows(NoSuchElementException.class,
                () -> service.cancelAppointment(appointmentId, null));
        var capturedId = uuidCaptor.getValue();

        assertEquals("Appointment not found.", exception.getMessage());
        assertEquals(appointmentId, capturedId);

        verify(appointmentRepository, times(1)).findById(capturedId);
        verifyNoMoreInteractions(appointmentRepository);
    }

    @Test
    @DisplayName("Deve marcar como cancelado se o agendamento já estiver passado.")
    void shouldMarkAsCancelledIfAppointmentHasAlreadyPassed() {
        //ARRANGE
        Appointment appointmentToCancel = Appointment.builder()
                .id(appointmentId)
                .date(LocalDateTime.parse("2024-02-12T14:00")).build();
        doReturn(Optional.of(appointmentToCancel)).when(appointmentRepository).findById(any());
        doReturn(new Appointment()).when(appointmentRepository).save(appointmentEntityCaptor.capture());

        //ACT & ASSERT
        assertDoesNotThrow(() -> service.cancelAppointment(appointmentId, null));

        var capturedAppointment = appointmentEntityCaptor.getValue();

        assertEquals(AppointmentStatus.CANCELADO, capturedAppointment.getAppointmentStatus());

        verify(appointmentRepository, times(1)).findById(any());
        verify(appointmentRepository).save(capturedAppointment);
        verifyNoMoreInteractions(appointmentRepository);
    }

}
