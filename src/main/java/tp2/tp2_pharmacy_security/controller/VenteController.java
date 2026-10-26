package tp2.tp2_pharmacy_security.controller;



import lombok.RequiredArgsConstructor;
import tp2.tp2_pharmacy_security.repository.MedicamentRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final MedicamentRepository repo;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("medicaments", repo.findAll());
        return "ventes";
    }

    @PostMapping("/vendre")
    public String vendre(@RequestParam Long id, @RequestParam int quantite, RedirectAttributes ra) {
        var opt = repo.findById(id);
        if (opt.isEmpty()) {
            ra.addFlashAttribute("erreur", "Médicament introuvable");
        } else if (quantite <= 0 || quantite > opt.get().getQuantite()) {
            ra.addFlashAttribute("erreur", "Quantité invalide ou stock insuffisant");
        } else {
            var m = opt.get();
            m.setQuantite(m.getQuantite() - quantite);
            repo.save(m);
            ra.addFlashAttribute("message", "Vente enregistrée : " + quantite + " x " + m.getNom());
        }
        return "redirect:/ventes";
    }
}
