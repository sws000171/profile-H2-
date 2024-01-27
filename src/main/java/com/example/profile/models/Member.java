package com.example.profile.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
@Table(name="member")
public class Member {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "face_path")
    private String facePath;

    @Column(name = "back_path")
    private String backPath;

    @NotBlank
    @Size(max = 120)
    @Column(name = "name")
    private String name;

    @NotBlank
    @Size(max = 10)
    @Column(name = "occupation")
    private String occupation;
    
    @NotBlank
    @Column(name = "memo")
    private String memo;

    @NotBlank
    @Email
    @Size(max = 254)
    @Column(name = "email")
    private String email;

    @Column(name = "other")
    private String other;

}
