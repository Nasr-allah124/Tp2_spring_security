package tp2.tp2_pharmacy_security.service;



import lombok.RequiredArgsConstructor;
import tp2.tp2_pharmacy_security.model.*;
import tp2.tp2_pharmacy_security.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class InitData implements CommandLineRunner {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final MedicamentRepository medicamentRepo;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        for (String nom : new String[]{"ADMIN", "PHARMACIEN", "CAISSIER", "CLIENT"}) {
            if (roleRepo.findByName(nom).isEmpty()) {
                roleRepo.save(new Role(null, nom));
            }
        }

        creerUser("admin", "admin123", "ADMIN");
        creerUser("pharma", "pharma123", "PHARMACIEN");
        creerUser("caisse", "caisse123", "CAISSIER");
        creerUser("client", "client123", "CLIENT");

        if (medicamentRepo.count() == 0) {
            medicamentRepo.save(new Medicament(null, "Amoxicilline 500mg", new BigDecimal("45.00"), 50));
            medicamentRepo.save(new Medicament(null, "Doliprane 500mg", new BigDecimal("25.50"), 120));
            medicamentRepo.save(new Medicament(null, "Vitamine C 1000mg", new BigDecimal("30.00"), 80));
            medicamentRepo.save(new Medicament(null, "Bétadine 10%", new BigDecimal("18.00"), 40));
            medicamentRepo.save(new Medicament(null, "Nurofen 400mg", new BigDecimal("42.00"), 60));
        }
    }

    private void creerUser(String username, String motDePasse, String role) {
        if (userRepo.findByUsername(username).isPresent()) return;
        User u = new User();
        u.setUsername(username);
        u.setPassword(encoder.encode(motDePasse));
        u.setRoles(Set.of(roleRepo.findByName(role).orElseThrow()));
        userRepo.save(u);
    }
}
