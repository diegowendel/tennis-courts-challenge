package com.tenniscourts.reservations;

import com.tenniscourts.schedules.Schedule;
import com.tenniscourts.schedules.ScheduleDTO;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = ReservationService.class)
public class ReservationServiceTest {

    @InjectMocks
    ReservationService reservationService;
    @Mock
    ReservationRepository reservationRepository;
    @Mock
    ReservationMapper reservationMapper;

    @Test
    public void getRefundValueFullRefund() {
        Schedule schedule = new Schedule();

        LocalDateTime startDateTime = LocalDateTime.now().plusDays(2);

        schedule.setStartDateTime(startDateTime);

        Assert.assertEquals(reservationService.getRefundValue(Reservation.builder().schedule(schedule).value(new BigDecimal(10L)).build()), new BigDecimal(10));
    }

    @Test
    public void rescheduleReservationToSameSchedule() {
        // Set up
        Schedule schedule = newSchedule();
        ScheduleDTO scheduleDTO = newScheduleDTO(schedule);
        Reservation reservation = newReservation(schedule);
        ReservationDTO reservationDTO = newReservationDTO(scheduleDTO);

        // Mocks
        Mockito.when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        Mockito.when(reservationMapper.map(reservation)).thenReturn(reservationDTO);

        // Assert
        Exception expectedEx = assertThrows(IllegalArgumentException.class, () ->
                reservationService.rescheduleReservation(reservation.getId(), schedule.getId())
        );
        Assert.assertEquals("Cannot reschedule to the same slot.", expectedEx.getMessage());
    }

    private Reservation newReservation(Schedule schedule) {
        Reservation reservation = Reservation.builder()
                .schedule(schedule)
                .value(BigDecimal.TEN)
                .reservationStatus(ReservationStatus.READY_TO_PLAY)
                .build();
        reservation.setId(2L);
        return reservation;
    }

    private ReservationDTO newReservationDTO(ScheduleDTO scheduleDTO) {
        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setSchedule(scheduleDTO);
        return reservationDTO;
    }

    private Schedule newSchedule() {
        Schedule schedule = Schedule.builder().build();
        schedule.setId(1L);
        return schedule;
    }

    private ScheduleDTO newScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setId(schedule.getId());
        return scheduleDTO;
    }
}