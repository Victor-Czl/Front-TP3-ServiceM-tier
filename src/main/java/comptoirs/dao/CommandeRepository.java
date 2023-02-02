package comptoirs.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import comptoirs.entity.Commande;

// This will be AUTO IMPLEMENTED by Spring into a Bean called CommandeRepository

public interface CommandeRepository extends JpaRepository<Commande, Integer> {

}
