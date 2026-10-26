package tp2.tp2_pharmacy_security.controller;



import lombok.RequiredArgsConstructor;
import tp2.tp2_pharmacy_security.model.Medicament;
import tp2.tp2_pharmacy_security.repository.MedicamentRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/medicaments")
@RequiredArgsConstructor
public class MedicamentController {

    private final MedicamentRepository repo;

    @GetMapping
    public String liste(Authentication auth, Model model) {
        model.addAttribute("medicaments", repo.findAll());
        model.addAttribute("canEdit", has(auth, "ADMIN") || has(auth, "PHARMACIEN"));
        model.addAttribute("canDelete", has(auth, "ADMIN"));
        return "medicaments";
    }

    @PostMapping("/save")
    public String ajouter(@ModelAttribute Medicament m) {
        m.setId(null);
        repo.save(m);
        return "redirect:/medicaments";
    }

    @PostMapping("/stock/{id}")
    public String modifierStock(@PathVariable Long id, @RequestParam int quantite) {
        repo.findById(id).ifPresent(m -> {
            m.setQuantite(Math.max(quantite, 0));
            repo.save(m);
        });
        return "redirect:/medicaments";
    }

    @PostMapping("/delete/{id}")
    public String supprimer(@PathVariable Long id) {
        repo.deleteById(id);
        return "redirect:/medicaments";
    }

    private boolean has(Authentication a, String role) {
        return a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_" + role));
    }
}
