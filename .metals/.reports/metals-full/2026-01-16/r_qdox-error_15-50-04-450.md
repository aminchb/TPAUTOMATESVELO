error id: file:///C:/Users/aminc/Desktop/TPAutomatesVelo/src/analyseurs/LexVelo.java
file:///C:/Users/aminc/Desktop/TPAutomatesVelo/src/analyseurs/LexVelo.java
### com.thoughtworks.qdox.parser.ParseException: syntax error @[144,1]

error in qdox parser
file content:
```java
offset: 3553
uri: file:///C:/Users/aminc/Desktop/TPAutomatesVelo/src/analyseurs/LexVelo.java
text:
```scala
package analyseurs;

import libIO.*;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * La classe LexVelo implémente un analyseur lexical pour une application de
 * location de velos
 * 
 * @author // TODO compléter les noms du trinôme
 *         janvier 2026
 */

public class LexVelo extends Lex {
	// TODO à compléter

	/** Nombre de mots réservés dans l'application Velo */
	public static final int NBRES = 5;

	/** Code des items lexicaux */
	protected static final int ADULTE = 0, DEBUT = 1, ENFANT = 2, FIN = 3, HEURES = 4;
	protected static final int IDENT = 5, NBENTIER = 6;
	protected static final int VIRG = 7, PTVIRG = 8, BARRE = 9, AUTRES = 10;

	/** Affichage souhaité des items lexicaux dans une fenêtre graphique */
	public static final String[] images = {
			"ADULTE", "DEBUT", "ENFANT", "FIN",
			"HEURES", "IDENT", "NBENT",
			"VIRG", "PTVIRG", "BARRE", "AUTRE" };

	/** Attributs des items lexicaux */
	private int valEnt;
	private int numIdCourant;

	/** Table des mots réservés et identifiants */
	private final ArrayList<String> tabIdent;

	/**
	 * Constructeur classe LexVelo
	 * 
	 * @param flot donnée d'entrée à analyser
	 */
	public LexVelo(InputStream flot) {
		super(flot);

		// Initialisation de tabIdent avec les mots réservés
		this.tabIdent = new ArrayList<>();
		for (int i = 0; i < NBRES; i++) {
			this.tabIdent.add(i, images[i]);
		}

		// Prélecture du premier caractère de la donnée
		lireCarLu();
	}

	/**
	 * @return la valeur de l'attribut de l'item NBENTIER
	 */
	public final int getvalEnt() {
		return valEnt;
	}

	/**
	 * @return la valeur de l'attribut de l'item IDENT
	 */
	public final int getnumIdCourant() {
		return numIdCourant;
	}

	/**
	 * Lecture du prochain item lexical, et mise à jour des attributs lexicaux
	 * correspondants.
	 * 
	 * @return code de l'item lexical reconnu
	 */
	@Override
	public final int lireSymb() {
		String symbol = "";
		while();

		// TODO
		throw new UnsupportedOperationException("méthode lireSymb à implémenter");
	}

	/**
	 * Accès à la chaîne d'un identificateur non réservé
	 * 
	 * @param numIdent numéro d'un ident dans la table des idents
	 * @return chaîne correspondant à numIdent
	 */
	public final String chaineIdent(int numIdent) {
		switch (numIdent) {
			case 0:
			case 0:
			case 0:
			case _: return "";
			
		// TODO
		throw new UnsupportedOperationException("méthode chaineIdent à imlémenter");
	}

	/**
	 * Main pour tester l'analyseur lexical seul, sans analyse syntaxique
	 */
	public static void main(String[] args) {

		InputStream flot;
		if (args.length == 1) {
			flot = Lecture.ouvrir(args[0]);
		} else {
			String nomfich = Lecture.lireString("nom du fichier d'entrée : ");
			flot = Lecture.ouvrir(nomfich);
		}
		if (flot == null) {
			Lecture.attenteSurLecture("Erreur avec le flux du fichier d'entrée");
			System.exit(0);
		}

		LexVelo testVelo = new LexVelo(flot);

		int token; // unité lexicale courante
		do {
			token = testVelo.lireSymb();
			switch (token) {
				case NBENTIER:
					Lecture.attenteSurLecture("token : " + images[token] +
							" attribut valEnt = " + testVelo.valEnt);
					break;
				case IDENT:
					Lecture.attenteSurLecture("token : " + images[token]
							+ " attribut numIdCourant = " + testVelo.numIdCourant
							+ " chaine associee = " + testVelo.chaineIdent(testVelo.numIdCourant));
					break;
				default:
					Lecture.attenteSurLecture("token : " + images[token]);
			}
		} while (token != BARRE);

		Lecture.fermer(flot);
		Lecture.attenteSurLecture("fin d'analyse");
	}
}
@@
```

```



#### Error stacktrace:

```
com.thoughtworks.qdox.parser.impl.Parser.yyerror(Parser.java:2025)
	com.thoughtworks.qdox.parser.impl.Parser.yyparse(Parser.java:2147)
	com.thoughtworks.qdox.parser.impl.Parser.parse(Parser.java:2006)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:232)
	com.thoughtworks.qdox.library.SourceLibrary.parse(SourceLibrary.java:190)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:94)
	com.thoughtworks.qdox.library.SourceLibrary.addSource(SourceLibrary.java:89)
	com.thoughtworks.qdox.library.SortedClassLibraryBuilder.addSource(SortedClassLibraryBuilder.java:162)
	com.thoughtworks.qdox.JavaProjectBuilder.addSource(JavaProjectBuilder.java:174)
	scala.meta.internal.mtags.JavaMtags.indexRoot(JavaMtags.scala:49)
	scala.meta.internal.metals.SemanticdbDefinition$.foreachWithReturnMtags(SemanticdbDefinition.scala:99)
	scala.meta.internal.metals.Indexer.indexSourceFile(Indexer.scala:546)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3(Indexer.scala:677)
	scala.meta.internal.metals.Indexer.$anonfun$reindexWorkspaceSources$3$adapted(Indexer.scala:674)
	scala.collection.IterableOnceOps.foreach(IterableOnce.scala:630)
	scala.collection.IterableOnceOps.foreach$(IterableOnce.scala:628)
	scala.collection.AbstractIterator.foreach(Iterator.scala:1313)
	scala.meta.internal.metals.Indexer.reindexWorkspaceSources(Indexer.scala:674)
	scala.meta.internal.metals.MetalsLspService.$anonfun$onChange$2(MetalsLspService.scala:912)
	scala.runtime.java8.JFunction0$mcV$sp.apply(JFunction0$mcV$sp.scala:18)
	scala.concurrent.Future$.$anonfun$apply$1(Future.scala:691)
	scala.concurrent.impl.Promise$Transformation.run(Promise.scala:500)
	java.base/java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1136)
	java.base/java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:635)
	java.base/java.lang.Thread.run(Thread.java:840)
```
#### Short summary: 

QDox parse error in file:///C:/Users/aminc/Desktop/TPAutomatesVelo/src/analyseurs/LexVelo.java