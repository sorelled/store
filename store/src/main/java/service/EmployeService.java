package service;

import dto.EmployeDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import model.Departement;
import model.Employe;
import model.Poste;
import org.springframework.stereotype.Service;
import repository.EmployeRepository;
import repository.PosteRepository;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PosteRepository posteRepository;

    // Ajouter un nouvel employé dans la BD
    public Employe addEmploye(Employe employe) {
        return employeRepository.save(employe);
    }

    // Récupérer la liste de tous les employés
    public List<EmployeDTO> getAllEmployes() {
        return employeRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un employé par son ID
    public EmployeDTO getEmployeById(UUID id) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employé non trouvé !"));
        return mapToDTO(employe);
    }

    // Récupérer la liste des employés par poste
    public List<EmployeDTO> getAllEmployeesByPoste(UUID id) {
        return employeRepository.findEmployeByPoste_Id(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Récupérer la liste des employés par département
    public List<EmployeDTO> getAllEmployeesByDepartement(UUID id) {
        return employeRepository.findEmployeByDepartement_Id(id)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Modifier les informations d’un employé existant
    public Employe updateEmploye(UUID id, Employe updatedEmploye) {
        return employeRepository.findById(id).map(existingEmploye -> {
            existingEmploye.setNom(updatedEmploye.getNom());
            existingEmploye.setPoste(updatedEmploye.getPoste());
            existingEmploye.setDepartement(updatedEmploye.getDepartement());
            existingEmploye.setDateEmbauche(updatedEmploye.getDateEmbauche());
            existingEmploye.setEmail(updatedEmploye.getEmail());
            existingEmploye.setSalaire(updatedEmploye.getSalaire());
            return employeRepository.save(existingEmploye);
        }).orElseThrow(() -> new RuntimeException("Employé non trouvé !"));
    }

    // Supprimer un employé par son ID
    public void deleteEmploye(UUID id) {
        if (!employeRepository.existsById(id)) {
            throw new EntityNotFoundException("L'employé avec l'ID " + id + " n'existe pas !");
        }
        employeRepository.deleteById(id);
    }

    // Méthode de conversion d'une entité Employe en DTO
    public EmployeDTO mapToDTO(Employe employe) {
        return new EmployeDTO(
                employe.getId(),
                employe.getNom(),
                employe.getEmail(),
                Period.between(employe.getDateEmbauche(), LocalDate.now()).getYears(),
                employe.getSalaire(),
                employe.getPoste().getLibellePoste(),
                employe.getDepartement().getLibelleDepartement()
        );
    }

    /*
    // Autre méthode possible de conversion (non utilisée ici)
    private EmployeDTO convertToDTO(Employe employe) {
        EmployeDTO employeDTO = new EmployeDTO();
        employeDTO.setId(employe.getId());
        employeDTO.setNom(employe.getNom());
        employeDTO.setEmail(employe.getEmail());
        employeDTO.setAnciennete(Period.between(employe.getDateEmbauche(), LocalDate.now()).getYears());
        employeDTO.setSalaire(employe.getSalaire());
        employeDTO.setPoste(employe.getPoste().getLibellePoste());
        employeDTO.setDepartement(employe.getDepartement().getLibelleDepartement());
        return employeDTO;
    }
    */
}
