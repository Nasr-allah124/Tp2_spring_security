package tp2.tp2_pharmacy_security.controller;



import lombok.RequiredArgsConstructor;
import tp2.tp2_pharmacy_security.model.User;
import tp2.tp2_pharmacy_security.repository.RoleRepository;
import tp2.tp2_pharmacy_security.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder encoder;

    @GetMapping
    public String page(Model model) {
        model.addAttribute("users", userRepo.findAll());
        model.addAttribute("roles", roleRepo.findAll());
        return "admin";
    }

    @PostMapping("/users")
    public String creer(@RequestParam String username, @RequestParam String password,
                        @RequestParam String role, RedirectAttributes ra) {
        if (userRepo.findByUsername(username).isPresent()) {
            ra.addFlashAttribute("erreur", "Ce nom d'utilisateur existe déjà");
            return "redirect:/admin";
        }
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(password));
        u.setRoles(Set.of(roleRepo.findByName(role).orElseThrow()));
        userRepo.save(u);
        ra.addFlashAttribute("message", "Utilisateur créé : " + username);
        return "redirect:/admin";
    }

    @PostMapping("/users/delete/{id}")
    public String supprimer(@PathVariable Long id, Authentication auth, RedirectAttributes ra) {
        userRepo.findById(id).ifPresent(u -> {
            if (u.getUsername().equals(auth.getName())) {
                ra.addFlashAttribute("erreur", "Tu ne peux pas supprimer ton propre compte");
            } else {
                userRepo.delete(u);
                ra.addFlashAttribute("message", "Utilisateur supprimé : " + u.getUsername());
            }
        });
        return "redirect:/admin";
    }
}
