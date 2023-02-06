package comptoirs.service;

import comptoirs.dao.CommandeRepository;
import comptoirs.dao.LigneRepository;
import comptoirs.dao.ProduitRepository;
import comptoirs.entity.Ligne;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated // Les contraintes de validatipn des méthodes sont vérifiées
public class LigneService {
    // La couche "Service" utilise la couche "Accès aux données" pour effectuer les traitements
    private final CommandeRepository commandeDao;
    private final LigneRepository ligneDao;
    private final ProduitRepository produitDao;

    // @Autowired
    // La couche "Service" utilise la couche "Accès aux données" pour effectuer les traitements
    public LigneService(CommandeRepository commandeDao, LigneRepository ligneDao, ProduitRepository produitDao) {
        this.commandeDao = commandeDao;
        this.ligneDao = ligneDao;
        this.produitDao = produitDao;
    }

    /**
     * <pre>
     * Service métier : 
     *     Enregistre une nouvelle ligne de commande pour une commande connue par sa clé,
     *     Incrémente la quantité totale commandée (Produit.unitesCommandees) avec la quantite à commander
     * Règles métier :
     *     - le produit référencé doit exister
     *     - la commande doit exister
     *     - la commande ne doit pas être déjà envoyée (le champ 'envoyeele' doit être null)
     *     - la quantité doit être positive
     *     - On doit avoir une quantite en stock du produit suffisante
     * <pre>
     * 
     *  @param commandeNum la clé de la commande
     *  @param produitRef la clé du produit
     *  @param quantite la quantité commandée (positive)
     *  @return la ligne de commande créée
     */
    @Transactional
    Ligne ajouterLigne(Integer commandeNum, Integer produitRef, @Positive int quantite) {
        //On vérifie que le produit existe
        var produit = produitDao.findById(produitRef).orElseThrow();
        //On vérifie que la commande existe
        var commande = commandeDao.findById(commandeNum).orElseThrow();
        //On vérifie que la commande n'est pas déjà été expédiée
        if(commande.getEnvoyeele() != null) {
            throw new IllegalStateException(("La commande a déjà été expédiée"));
        }
        //On vérifie que la quantité demandée est positive
        if(quantite <= 0) {
            throw new IllegalStateException("La quantité demandée doit être positive");
        }
        if(produit.getUnitesEnStock() < quantite) {
            throw new IllegalStateException("La quantité demandée est supérieur au stock");
        }
        Ligne ligne = new Ligne(commande, produit, quantite);
        commande.getLignes().add(ligne);
        ligneDao.save(ligne);

        return  ligne;
    }
}
