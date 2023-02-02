package comptoirs.dao;


import comptoirs.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// This will be AUTO IMPLEMENTED by Spring into a Bean called ProductCodeRepository
// CRUD refers Create, Read, Update, Delete

public interface ClientRepository extends JpaRepository<Client, String> {
    /**
     * Calcule le nombre d'articles commandés par un client
     * @param clientCode la clé du client
     */
    // Attention : SUM peut renvoyer NULL si on ne trouve pas d'enregistrement
    // On utilise COALESCE pour renvoyer 0 dans ce cas
    // http://www.h2database.com/html/functions.html#coalesce
    @Query("SELECT COALESCE(SUM(l.quantite), 0) FROM Ligne l WHERE l.commande.client.code = :clientCode")
    int nombreArticlesCommandesPar(String clientCode);

}
