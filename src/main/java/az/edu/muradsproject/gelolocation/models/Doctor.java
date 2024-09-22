package az.edu.muradsproject.gelolocation.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;
    private String name;
    private String specialty;
    private String photoUrl;
    private String qualifications;
    private String experience;
    private boolean isActive;

    @ManyToMany(mappedBy = "doctors")
    private List<Clinic> clinics;


    public void addClinic(Clinic clinic) {
        this.clinics.add(clinic);
        clinic.getDoctors().add(this);
    }

    public void removeClinic(Clinic clinic) {
        this.clinics.remove(clinic);
        clinic.getDoctors().remove(this);
    }

}