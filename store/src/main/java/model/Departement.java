package model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.UUID;
@Entity
@Table(name = "departements")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Departement {
    @Id
 @GeneratedValue
  private UUID id;

    @Column(nullable = false)
 @NotBlank(message = "Le libellé du département est obligatoire")
 private String libelleDepartement;

         @OneToMany(mappedBy = "departement", cascade = CascadeType.ALL)
    private List<Employe> employes;
}