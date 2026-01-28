package analyseurs;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import libIO.*;

import main.InfosClient;
import main.BaseDeLoc;

/**
 * La classe ActVelo met en oeuvre les actions de l'automate d'analyse
 * syntaxique des locations de vélos
 * 
 * @author
 * Amin Choubai
 * Mathieu Del Angel
 * Alexis Guibert
 * janvier 2026
 */

public class ActVelo extends AutoVelo {
	// Raccourci d'accès à l'analyseur lexical
	private final LexVelo analyseurLexical = (LexVelo) this.getAnalyseurLexical();

	/** Table des actions */
	// TODO compléter la table ACTION
	private final int[][] ACTION = {
			// Etat ADULTE DEBUT ENFANT FIN HEURES IDENT NBENTIER VIRG PTVIRG BARRE AUTRES
			/* 0 */ { -1,    -1,    -1,   -1,   -1,     1,     -1,   -1,   16,    17,    -1 },
			/* 1 */ { -1,     5,    -1,    4,   -1,    -1,      2,   -1,   -1,    -1,    -1 },
			/* 2 */ { -1,    -1,    -1,   -1,    3,    -1,     -1,   -1,   -1,    -1,    -1 },
			/* 3 */ { -1,     5,    -1,    4,   -1,    -1,     -1,   -1,   -1,    -1,    -1 },
			/* 4 */ { -1,    -1,    -1,   -1,   -1,    -1,     -1,    15,   16,    -1,    -1 },
			/* 5 */ { -1,    -1,    -1,   -1,   -1,    -1,      6,   -1,   -1,    -1,    -1 },
			/* 6 */ {  8,    -1,     7,   -1,   -1,    -1,     -1,   -1,   -1,    -1,    -1 },
			/* 7 */ { -1,    -1,    -1,   -1,   -1,    -1,     -1,    11,   12,    -1,    -1 },
			/* 8 */ { -1,    -1,    -1,   -1,   -1,    -1,      9,   13,   14,    -1,    -1 },
			/* 9 */ { -1,    -1,    10,   -1,   -1,    -1,     -1,   -1,   -1,    -1,    -1 },
			/* e */ { -1,    -1,    -1,   -1,   -1,    -1,     -1,   -1,   16,    17,    -1 }, /* 10 */
			/* f */ { -1,    -1,    -1,   -1,   -1,    -1,     -1,   -1,   -1,    -1,    -1 }, /* 11 */
	};

	/** Nombre de vélos initialement disponibles */
	private static final int MAX_VELOS_ADULTES = 50;
	private static final int MAX_VELOS_ENFANT = 20;

	/** Ensemble des locations en cours (non terminées) */
	private final BaseDeLoc maBaseDeLoc;

	/** Ensemble des clients différents vus, pour chaque jour */
	private final ArrayList<Set<Integer>> clientsParJour;

	// Rappel: chaque validation correspond à un jour différent
	// jourCourant correspond à la validation en cours d'analyse
	private int jourCourant;

	// Rappel: chaque validation est composée de plusieurs opérations
	// nbOperationTotales comptabilise toutes les opérations contenues dans la
	// donnée à analyser, erronées ou non
	private int nbOperationTotales;

	// nbOperationCorrectes comptabilise toutes les opérations sans erreur
	// contenues dans la donnée à analyser
	private int nbOperationCorrectes;

	// variables opérations en cours
	private String nomClientCourant;
	private int numIdentClientCourant;
	private int horaireCourant;
	private boolean horairePresent;
	private int qteAdulteCourant;
	private int qteEnfantCourant;
	// velos dispos
	private int velosAdultesDisponibles;
	private int velosEnfantDisponibles;
	// jour max clients
	private int jourMaxClients;
	private int nbMaxClients;
	// gestion d'erreur
	private boolean erreurEnCours;

	/**
	 * Constructeur classe ActVelo
	 * 
	 * @param flot donnée à analyser
	 */
	public ActVelo(InputStream flot) {
		super(flot);
		clientsParJour = new ArrayList<>();
		maBaseDeLoc = new BaseDeLoc();
	}

	/**
	 * Définition de la méthode abstraite getAction de Automate
	 * 
	 * @param etat  code de l'état courant
	 * @param unite code de l'unité lexicale courante
	 * @return code de l'action à exécuter
	 **/
	@Override
	public final int getAction(int etat, int unite) {
		return ACTION[etat][unite];
	}

	/**
	 * Définition de la méthode abstraite initAction de Automate
	 */
	@Override
	public final void initAction() {

		// initialisations à effectuer avant les actions
		nbOperationCorrectes = 0;
		nbOperationTotales = 0;
		jourCourant = 1;

		// initialisation des clients du premier jour
		// NB: le jour 0 n'est pas utilisé
		clientsParJour.add(0, new HashSet<>());
		clientsParJour.add(1, new HashSet<>());

		// init velos dispo
		velosAdultesDisponibles = MAX_VELOS_ADULTES;
		velosEnfantDisponibles = MAX_VELOS_ENFANT;

		// init suivi jour
		jourMaxClients = 1;
		nbMaxClients = 0;

		// init var temp
		nomClientCourant = "";
		numIdentClientCourant = -1;
		horaireCourant = 8;
		horairePresent = false;
		qteAdulteCourant = 0;
		qteEnfantCourant = 0;
		erreurEnCours = false;
	}

	/**
	 * Définition de la méthode abstraite faireAction de Automate
	 * 
	 * @param etat  code de l'état courant
	 * @param unite code de l'unité lexicale courante
	 **/
	@Override
	public final void faireAction(int etat, int unite) {
		executer(ACTION[etat][unite]);
	}

	/**
	 * Exécution d'une action
	 * 
	 * @param numAction numéro de l'action à exécuter
	 */
	private final void executer(int numAction) {

		switch (numAction) {
			case -1: // action vide
				break;

			case 1 : // IDENT reconnu - début d'opération
				numIdentClientCourant = analyseurLexical.getNumIdCourant();
				nomClientCourant = analyseurLexical.chaineIdent(numIdentClientCourant);
				horairePresent = false;
				horaireCourant = 8; // par défaut
				qteAdulteCourant = 0;
				qteEnfantCourant = 0;
				erreurEnCours = false;
				nbOperationTotales++;
				break;
			case 2: // NBENTIER reconnu après IDENT (horaire)
				horaireCourant = analyseurLexical.getValEnt();
				horairePresent = true;
				break;
			case 3: // HEURES reconnu
				// rien à faire (horaire déjà stocké dans act => 2)
				break;
			case 4: // FIN reconnu - opération de fin de location
				if (!horairePresent) {
					horaireCourant = 19; // par défaut
				}
				traiterFinLocation();
				break;
			case 5: // DEBUT reconnu - début d'opération de début de location
				if (!horairePresent) {
					horaireCourant = 8; // par défaut
				}
				break;
			case 6: // NBENTIER après DEBUT - première quantité
				qteAdulteCourant = 0;
				qteEnfantCourant = 0;
				// tmp (on sait pas encore si adulte OU enfant)
				qteAdulteCourant = analyseurLexical.getValEnt();
				break;
			case 7: // ENFANT après première quantité
				qteEnfantCourant = qteAdulteCourant;
				qteAdulteCourant = 0;
				traiterDebutLocation();
				break;
			case 8: // ADULTE après première quantité
				// déjà rempli
				break;
			case 9: // NBENTIER après ADULTE (deuxième quantité)
				qteEnfantCourant = analyseurLexical.getValEnt();
				break;
			case 10: // ENFANT après deuxième quantité
				traiterDebutLocation();
				break;
			case 11: // VIRG après opération complète (enfant seul)
				// rien à faire
				break;
			case 12: // PTVIRG après opération complète (enfant seul) - fin de journée
				bilanJournalier();
				jourCourant++;
				clientsParJour.add(jourCourant, new HashSet<>());
				break;
			case 13: // VIRG après adulte seul
				traiterDebutLocation();
				break;
			case 14: // PTVIRG après adulte seul - fin de journée
				traiterDebutLocation();
				bilanJournalier();
				jourCourant++;
				clientsParJour.add(jourCourant, new HashSet<>());
				break;
			case 15: // VIRG après FIN
				// rien à faire
				break;
			case 16: // PTVIRG après FIN - fin de journée
				bilanJournalier();
				jourCourant++;
				clientsParJour.add(jourCourant, new HashSet<>());
				break;
			case 17: // BARRE - fin de l'analyse
				bilanFinal();
            	System.out.println("\nFin d'analyse. La fenêtre graphique est encore active, pour continuer tapez entrée");
            	try {
                	System.in.read();
            	} catch (Exception e) {

				}
            	System.exit(0);
			default:
				Lecture.attenteSurLecture("action " + numAction + " non prévue");
		}
	}

	private void traiterDebutLocation(){
		if (erreurEnCours) {
			return;
		}
		// Verif horaire
		if (horaireCourant < 8 || horaireCourant > 19) {
			Ecriture.ecrireStringln("ERREUR: Horaire invalide (" + horaireCourant + "h) pour le client " + nomClientCourant);
			return;
		}
		// Verif quantite
		if (qteAdulteCourant == 0 && qteEnfantCourant == 0) {
			Ecriture.ecrireStringln("ERREUR: Aucun vélo demandé pour le client " + nomClientCourant);
			return;
		}
		// Verif dispo
		if (qteAdulteCourant > velosAdultesDisponibles || qteEnfantCourant > velosEnfantDisponibles) {
			Ecriture.ecrireStringln("ERREUR: Pas assez de vélos disponibles pour le client " + nomClientCourant);
			return;
		}
		// Verif client pas de loc en cours
		if (maBaseDeLoc.getInfosClient(nomClientCourant) != null) {
			Ecriture.ecrireStringln("ERREUR: Le client " + nomClientCourant + " a déjà une location en cours");
			return;
		}
		// Enregistrement location
		maBaseDeLoc.enregistrerLoc(nomClientCourant, jourCourant, horaireCourant, qteAdulteCourant, qteEnfantCourant);
		// Maj vélos dispos
		velosAdultesDisponibles -= qteAdulteCourant;
		velosEnfantDisponibles -= qteEnfantCourant;
		// Ajout client ens clients jour
		clientsParJour.get(jourCourant).add(numIdentClientCourant);
		// Operat correcte = ++
		nbOperationCorrectes++;
	}

	private void traiterFinLocation() {
		if (erreurEnCours) {
			return;
		}
		// Verif horaire
		if (horaireCourant < 8 || horaireCourant > 19) {
			Ecriture.ecrireStringln("ERREUR: Horaire invalide (" + horaireCourant + "h) pour le client " + nomClientCourant);
			return;
		}
		InfosClient infosClient = maBaseDeLoc.getInfosClient(nomClientCourant);
		if (infosClient == null) {
			Ecriture.ecrireStringln("ERREUR: Le client " + nomClientCourant + " n'a pas de location en cours");
			return;
		}
		// Verif cohérence temps
		if (jourCourant == infosClient.jourEmprunt && horaireCourant < infosClient.heureDebut) {
			Ecriture.ecrireStringln("ERREUR FATALE: Heure de fin (" + horaireCourant + "h) inférieure à l'heure de début (" + infosClient.heureDebut + "h) pour le client " + nomClientCourant);
			System.exit(1);
		}
		int duree = calculDureeLoc(infosClient.jourEmprunt, infosClient.heureDebut, jourCourant, horaireCourant);
		int montant = duree * (infosClient.qteAdulte * 4 + infosClient.qteEnfant * 2);
		// affiche facture
		Ecriture.ecrireStringln("Le client: " + nomClientCourant.toUpperCase() + " doit payer : " + montant + " euros pour " + infosClient.qteAdulte + " vélo(s) adulte et " + infosClient.qteEnfant + " vélo(s) enfant");
		// Maj velos dispos
		velosAdultesDisponibles += infosClient.qteAdulte;
		velosEnfantDisponibles += infosClient.qteEnfant;
		// Suppression location
		maBaseDeLoc.supprimerClient(nomClientCourant);
		// Ajout client ens clients jour
		clientsParJour.get(jourCourant).add(numIdentClientCourant);
		// Opérat correcte = ++
		nbOperationCorrectes++;
	}

	private void bilanJournalier() {
		Ecriture.ecrireStringln("******************************************************************************");
		Ecriture.ecrireStringln("BILAN DU JOUR " + jourCourant);
		maBaseDeLoc.afficherLocationsEnCours();
		// velos manquants
		int velosAdultesManquants = MAX_VELOS_ADULTES - velosAdultesDisponibles;
		int velosEnfantManquants = MAX_VELOS_ENFANT - velosEnfantDisponibles;
		// affichage
		Ecriture.ecrireStringln("Nombre de vélos adulte manquants : " + velosAdultesManquants);
		Ecriture.ecrireStringln("Nombre de vélos enfant manquants : " + velosEnfantManquants);
		// Maj jour max clients
		int nbClientsJour = clientsParJour.get(jourCourant).size();
		if (nbClientsJour > nbMaxClients) {
			nbMaxClients = nbClientsJour;
			jourMaxClients = jourCourant;
		}
	}

	private void bilanFinal() {
		Ecriture.ecrireStringln("Opérations correctes : " + nbOperationCorrectes + " - Nombre total d'opérations : " + nbOperationTotales);
		Ecriture.ecrireStringln("Voici les clients qui doivent encore rendre des vélos");
		maBaseDeLoc.afficherLocationsEnCours();
		Ecriture.ecrireStringln("**************************** BILAN AFFLUENCE **********************************");
		Ecriture.ecrireStringln("Le jour de plus grande affluence est : " + jourMaxClients + " avec " + nbMaxClients + " clients servis");
	}

	/**
	 * Calcul de la durée d'une location
	 *
	 * @param jourDebutLoc  numéro du jour de début de la location à partir de 1
	 * @param heureDebutLoc heure du début de la location, entre 8 et 19
	 * @param jourFinLoc    numéro du jour de la fin de la location à partir de 1
	 * @param heureFinLoc   heure de fin de la location, entre 8 et 19
	 * @return nombre d'heures comptabilisées pour la location
	 *         (les heures de nuit entre 19h et 8h ne sont pas comptabilisées)
	 */
	private final int calculDureeLoc(int jourDebutLoc, int heureDebutLoc, int jourFinLoc, int heureFinLoc) {
		int duree = 0;
		if (jourDebutLoc == jourFinLoc) { // vélos rendus le jour de l'emprunt
			if (heureFinLoc != heureDebutLoc) {
				duree = heureFinLoc - heureDebutLoc;
			} else {
				duree = 1;
			}
		} else { // vélos rendus après le jour d'emprunt (duree négative interdite)
			duree = 19 - heureDebutLoc; // duree du premier jour
			duree = duree + (heureFinLoc - 8); // ajout de la duree du dernier jour
			if (jourFinLoc > jourDebutLoc + 1) { // plus 11h ouvrées par jour intermediaire
				duree = duree + 11 * (jourFinLoc - jourDebutLoc - 1);
			}
		}
		return duree;
	}
}
