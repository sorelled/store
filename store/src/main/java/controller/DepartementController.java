package controller;

import dto.EmployeDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import model.Departement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import service.DepartementService;
import service.EmployeService;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departement")
public class DepartementController {

    private final DepartementService departementService;
    private final EmployeService employeService;

    // Liste des departements
    @GetMapping
    public String getAllDepartements(Model model) {
        model.addAttribute("departemnts", departementService.allDepartements());
        return "departement/list";
    }

    /* Créer un nouveau departement: la création et
    la modification d'une entité aura toujours 2 méthodes
    (une avec GetMapping et l'autre avec PostMapping */
    @GetMapping("/new")
    public String add(Model model) {
        model.addAttribute("departement", new Departement());
        return "departement/add";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("departement") Departement departement,
                       BindingResult bindingResult,
                       Model model,
                       RedirectAttributes ra) {
        // On teste si les données saisies dans le formulaire sont valides
        if (bindingResult.hasErrors()) {
            // Retourner à la vue avec les erreurs
            return "departement/add";
        }

        try {
            departementService.addDepartement(departement);
            // Message flash
            String msg = "Le département " + departement.getLibelleDepartement()
                    + " a été créé avec succès !";
            ra.addFlashAttribute("msg", msg);

            /* Après la création d'un nouveau département on se redirige
            vers la page des listes des départements */
            return "redirect:/departement";
        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("libelleDepartement", null, e.getMessage());
            return "departement/add";
        }
    }

    // Pour voir la liste des employés pour le departement indiqué
    @GetMapping("/employes/{id}")
    public String listeEmployesByPoste(@PathVariable UUID id, Model model) {
        Departement departement = departementService.getDepartementById(id)
                .orElseThrow(() -> new EntityNotFoundException("Département non existant"));

        // Liste des employes du departement dont l'id est {id}
        List<EmployeDTO> employesDTO = employeService.getAllEmployeesByDepartement(id);

        // Transmission des variables au template
        model.addAttribute("departement", departement.getLibelleDepartement());
        model.addAttribute("employes", employesDTO);

        // Affichage du template
        return "departement/listemploye";
    }

    /* Modifier les informations d'un departement: la création et
    la modification d'une entité aura toujours 2 méthodes
    (une avec GetMapping et l'autre avec PostMapping */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable UUID id, Model model, RedirectAttributes ra) {
        // On recupère le departement à modifier
        Departement departement = departementService.getDepartementById(id)
                .orElseThrow(() -> new EntityNotFoundException("Poste non trouvé"));

        model.addAttribute("departement", departement);

        // Appel de la page qui affichera les informations sur le departement à modifier
        return "departement/edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable UUID id,
                         @Valid @ModelAttribute("departement") Departement departement,
                         BindingResult bindingResult,
                         Model model,
                         RedirectAttributes ra) {
        // On teste si les données saisies dans le formulaire sont valides
        if (bindingResult.hasErrors()) {
            // Retourner à la vue avec les erreurs
            return "departement/edit";
        }

        try {
            departementService.updateDepartement(id, departement);
            // Message flash
            String msg = "Le département " + departement.getLibelleDepartement()
                    + " a été mis à jour avec succès !";
            ra.addFlashAttribute("msg", msg);

            /* Après la modification d'un poste on se redirige vers
            la page des listes des departements */
            return "redirect:/departement";

        } catch (IllegalArgumentException e) {
            bindingResult.rejectValue("libelleDepartement", null, e.getMessage());
            return "departement/edit";
        }
    }

    // Supprimer un departement de la BD
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable UUID id, Model model, RedirectAttributes ra) {
        // Suppression du departement dont l'id est {id}
        departementService.deleteDepartement(id);

        // Message flash
        String msg = "Le département a été supprimé avec succès !";
        ra.addFlashAttribute("msg", msg);

        // Redirection vers la page affichant les listes des departement
        return "redirect:/departement";
    }
}

