package analyseurs;

import java.io.*;

/**
 * La classe AutoVelo met en oeuvre l'automate d'analyse syntaxique des
 * locations de vélos, par interpreteur de tables
 * 
 * @author
 * Amin Choubai
 * Mathieu Del Angel
 * Alexis Guibert
 * janvier 2026
 */

public abstract class AutoVelo extends Automate {
	// RAPPEL: reprise après erreur demandée sur les items VIRG, PTVIRG et BARRE

	/** Table des transitions */
	private static final int[][] TRANSIT = {
			// Etat ADULTE DEBUT ENFANT FIN HEURES IDENT NBENTIER VIRG PTVIRG BARRE AUTRES
			/* 0 */ { 10,    10,   10,   10,  10,     1,    10,    10,   10,   11,    10 },
			/* 1 */ { 10,     5,   10,    4,  10,    10,     2,    10,   10,   10,    10 },
			/* 2 */ { 10,    10,   10,   10,   3,    10,    10,    10,   10,   10,    10 },
			/* 3 */ { 10,     5,   10,    4,  10,    10,    10,    10,   10,   10,    10 },
			/* 4 */ { 10,    10,   10,   10,  10,    10,    10,     0,    0,   10,    10 },
			/* 5 */ { 10,    10,   10,   10,  10,    10,     6,    10,   10,   10,    10 },
			/* 6 */ {  8,    10,    7,   10,  10,    10,    10,    10,   10,   10,    10 },
			/* 7 */ { 10,    10,   10,   10,  10,    10,    10,     0,    0,   10,    10 },
			/* 8 */ { 10,    10,   10,   10,  10,    10,     9,     0,    0,   10,    10 },
			/* 9 */ { 10,    10,    7,   10,  10,    10,    10,    10,   10,   10,    10 },
			/* e */ { 10,    10,   10,   10,  10,    10,    10,     0,    0,   11,    10 }, /* 10 */
			/* f */ { -1,    -1,   -1,   -1,  -1,    -1,    -1,    -1,   -1,   -1,    -1 }, /* 11 */
	};

	/**
	 * Constructeur classe AutoVelo
	 * 
	 * @param flot donnée à analyser
	 */
	protected AutoVelo(InputStream flot) {
		// on utilise ici un analyseur lexical de type LexVelo
		// on numérote les états initial, final, et d'erreur selon la convention
		super(new LexVelo(flot), 0, TRANSIT.length, TRANSIT.length - 1);
	}

	/**
	 * Définition de la méthode abstraite getTransition de Automate
	 * 
	 * @param etat  code de l'état courant
	 * @param unite code de l'unité lexicale courante
	 * @return code de l'état suivant
	 **/
	@Override
	public final int getTransition(int etat, int unite) {
		return TRANSIT[etat][unite];
	}
}