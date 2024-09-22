package az.edu.muradsproject.gelolocation.controllers;

import az.edu.muradsproject.gelolocation.dto.DoctorDto;
import az.edu.muradsproject.gelolocation.models.Doctor;
import az.edu.muradsproject.gelolocation.payloads.ApiResponse;
import az.edu.muradsproject.gelolocation.services.ClinicService;
import az.edu.muradsproject.gelolocation.services.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ClinicService clinicService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createDoctor(@RequestBody DoctorDto doctorDto) {
        ApiResponse response= doctorService.createDoctor(doctorDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    // Endpoint to assign a clinic to an existing doctor
    @PostMapping("/{doctorId}/assign-clinic/{clinicId}")
    public ApiResponse assignClinicToDoctor(@PathVariable Long doctorId, @PathVariable Long clinicId) {
        return doctorService.assignClinicToDoctor(doctorId, clinicId);
    }



}
