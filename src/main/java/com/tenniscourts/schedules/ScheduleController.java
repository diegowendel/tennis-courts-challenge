package com.tenniscourts.schedules;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@RequestMapping("/schedules")
@RestController
public class ScheduleController extends BaseRestController {

    private final ScheduleService scheduleService;

    @PostMapping
    public ResponseEntity<Void> addScheduleTennisCourt(@RequestBody CreateScheduleRequestDTO createScheduleRequestDTO) {
        return ResponseEntity.created(locationByEntity(
                scheduleService.addSchedule(createScheduleRequestDTO.getTennisCourtId(),
                        createScheduleRequestDTO).getId())
        ).build();
    }

    @GetMapping("/interval")
    @ResponseBody
    public ResponseEntity<List<ScheduleDTO>> findSchedulesByDates(@RequestParam("start") LocalDate startDate,
                                                                  @RequestParam("end") LocalDate endDate) {
        return ResponseEntity.ok(scheduleService.findSchedulesByDates(
                LocalDateTime.of(startDate, LocalTime.of(0, 0)),
                LocalDateTime.of(endDate, LocalTime.of(23, 59)))
        );
    }

    @GetMapping("/{scheduleId}")
    @ResponseBody
    public ResponseEntity<ScheduleDTO> findByScheduleId(@PathVariable("scheduleId") Long scheduleId) {
        return ResponseEntity.ok(scheduleService.findSchedule(scheduleId));
    }
}
