package tp2.tp2_pharmacy_security.repository;



import tp2.tp2_pharmacy_security.model.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicamentRepository extends JpaRepository<Medicament, Long> { }
