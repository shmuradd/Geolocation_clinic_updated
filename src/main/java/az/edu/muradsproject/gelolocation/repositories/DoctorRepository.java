package az.edu.muradsproject.gelolocation.repositories;

import az.edu.muradsproject.gelolocation.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByNameAndSpecialty(String doctorName, String specialty);
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyContainingIgnoreCase(String name, String specialty);

    List<Doctor> findByNameContainingIgnoreCase(String doctorName);
}
