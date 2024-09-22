package az.edu.muradsproject.gelolocation.repositories;

import az.edu.muradsproject.gelolocation.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
    List<Clinic> findByDoctorsNameContainingIgnoreCaseAndDoctorsSpecialtyContainingIgnoreCaseAndCityContainingIgnoreCase(
            String doctorName, String specialty, String city);

    @Query("SELECT c FROM Clinic c JOIN c.doctors d WHERE "
            + "(LOWER(d.name) LIKE LOWER(CONCAT('%', :doctorName, '%')) OR :doctorName IS NULL) "
            + "AND (LOWER(d.specialty) LIKE LOWER(CONCAT('%', :specialty, '%')) OR :specialty IS NULL) "
            + "AND (LOWER(c.city) LIKE LOWER(CONCAT('%', :city, '%')) OR :city IS NULL)")
    List<Clinic> findClinicsByDoctorAndCity(
            @Param("doctorName") String doctorName,
            @Param("specialty") String specialty,
            @Param("city") String city
    );}
