package tp2.tp2_pharmacy_security.controller;



import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping({"/", "/home"})
    public String home(Authentication auth, Model model) {
        model.addAttribute("username", auth.getName());
        model.addAttribute("roles", auth.getAuthorities());
        model.addAttribute("isAdmin", has(auth, "ADMIN"));
        model.addAttribute("canMedicaments", has(auth, "ADMIN") || has(auth, "PHARMACIEN") || has(auth, "CLIENT"));
        model.addAttribute("canVentes", has(auth, "ADMIN") || has(auth, "PHARMACIEN") || has(auth, "CAISSIER"));
        return "home";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "access-denied";
    }

    private boolean has(Authentication a, String role) {
        return a.getAuthorities().stream().anyMatch(x -> x.getAuthority().equals("ROLE_" + role));
    }
}
