package com.palaspapas.back.domain.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "employee")
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_type", nullable = false)
    private String idType;

    @Column(name = "id_number", nullable = false, unique = true)
    private String idNumber;

    @Column(name = "user", nullable = false)
    private String user;

    @Column(name = "status", length = 1, nullable = false)
    private String status;

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @Column(name = "last_name", length = 60, nullable = false)
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthday;

    @Column(name = "gender", length = 1, nullable = false)
    private String gender;

    @Column(name = "nationality", length = 15, nullable = false)
    private String nationality;

    @Column(name = "marital_status", nullable = false)
    private String maritalStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "entry_date")
    private LocalDate entryDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "exit_date")
    private LocalDate exitDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "reentry_date")
    private LocalDate reentryDate;

    @Column(name = "academic_degree")
    private String academicDegree;

    @Column(name = "title")
    private String title;

    @Column(name = "salary")
    private Double salary;

    @Column(name = "phone", length = 10)
    private String phone;

    @Column(name = "mobile", length = 10)
    private String mobile;

    @Column(name = "main_street", nullable = false)
    private String mainStreet;

    @Column(name = "secondary_street")
    private String secondaryStreet;

    @Column(name = "personal_email")
    private String personalEmail;

    @Column(name = "institutional_email")
    private String institutionalEmail;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "employee_photo", columnDefinition = "longblob")
    private byte[] employeePhoto;

    @ManyToOne
    @JoinColumn(name = "position_id")
    private Position position;
}
