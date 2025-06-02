package service;

import dto.EmployeDTO;
 import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import model.Poste;
import model.TypePoste;
import org.springframework.stereotype.Service;
import repository.PosteRepository;

import java.util.List;
import java.util.Optional;
 import java.util.UUID;

 @Service
 @RequiredArgsConstructor
 public class PosteService {

         private final PosteRepository posteRepository;

         // Ajouter un nouveau poste en BD
         public Poste addPoste(Poste poste) {
         // Vérifie si le libellé existe déjà
         if (posteRepository.existsDistinctByLibellePoste(poste.getLibellePoste())) {
             throw new IllegalArgumentException("Ce libellé existe déjà.");
            }
         return posteRepository.save(poste);
         }

         // Recupérer la liste de tous les postes qui existent dans la BD
         public List<Poste> getAllPostes() {
         return posteRepository.findAll();

}

         // Recupérer un poste qui existe dans la BD par son ID
         public Optional<Poste> getPosteById(UUID id) {
         return posteRepository.findById(id);
         }

         // Modifier les informations sur un poste (existingPoste) dejà existant en BD
         public Poste updatePoste(UUID id, Poste updatedPoste) {

         return posteRepository.findById(id).map(
                 existingPoste -> {
                     existingPoste.setLibellePoste(updatedPoste.getLibellePoste());
                     existingPoste.setSalaireMax(updatedPoste.getSalaireMax());
                     existingPoste.setSalaireMin(updatedPoste.getSalaireMin());
                     return posteRepository.save(existingPoste);
                     }
                 ).orElseThrow(() -> new RuntimeException("Poste non trouvé !"));
        }

         // Supprimer de la BD un poste par son ID
         public void deletePoste(UUID id) {
         // Si le poste a supprimer n'existe pas il faut afficher un message
         if (!posteRepository.existsById(id)) {
             throw new EntityNotFoundException("Le poste avec id " + id + " n'existe pas !"); }
         posteRepository.deleteById(id);
        }
}

