package gg.sim.mzevallos.devoir4;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author X_Pet
 */
public class JeuDuPendu {
    
    static int nbrMots = 0;
    static String[] mot = new String [nbrMots];
    static int NEM = 10;
    
    public static String[] Pendu(){
        
        // création de variables
        int min = 5;
        int max = 40;
        String nPropre;
        String cAccent;
        boolean nomsPropres = false;
        boolean nomsAccents = true;
        
        
        System.out.println("Changez les options ou faites <Entrée> pour accepter la valeur par défaut: ");
        
        String reponse = Utilitaires.lireChaine("Nombre d'erreur maximum (10): ");
        if(reponse != "") NEM = Utilitaires.deStringVersInt(reponse);
        
        reponse = Utilitaires.lireChaine("Longeur minimum des mots, au moins 3 (5): ");
        if(reponse != "") min = Utilitaires.deStringVersInt(reponse);
        if(min < 3) min = 3;
        
        reponse = Utilitaires.lireChaine("Longeur maximum des mots, au moins 3 (40): ");
        if(reponse != "") max = Utilitaires.deStringVersInt(reponse);
        if(max < 3) max = 3;
        
        nPropre = Utilitaires.lireChaine("Noms Propres (N): ");
        cAccent = Utilitaires.lireChaine("Caractère accentuées (N): ");
        
        System.out.println("\nles options: \n"
                         + "Nombre d'erreurs maximum: " + NEM
                         + "\nLongueur minimum des mots: " + min
                         + "\nLongueur maximum des mots: " + max);
        if(nPropre.equals("o")){
            System.out.println("avec noms propres");
            nomsPropres = true;
        } else if (! nPropre.equals("o")){
            System.out.println("sans noms propres");
            nomsPropres = false;
        } 
        if (cAccent.equals("o")){
            System.out.println("avec lettres accentuées\n");
            nomsAccents = true;
        } else if (! cAccent.equals("o")){
            System.out.println("sans lettres accentuées\n");
            nomsAccents = false;
        }
        
        String fichierLecture = "listeMots.txt";
         
        String fichierEcriture = "listeMotsRetenus.txt";
        String nomCommuns = "^[a-záÁâÂàÀåÅãÃäÄæÆçÇéÉêÊèÈëËíÍîÎìÌïÏñÑóÓôÔòÒøØõÕöÖœŒšŠßðÐþÞúÚûÛùÙüÜýÝÿŸ].*$";
        String nomSansAccents = "^[a-zA-Z]*$";
        
       
        //lecture/ecriture du fichier...
        try {
            
            FileWriter ecrireMots = new FileWriter(fichierEcriture);     
            File mots = new File(fichierLecture);
            Scanner lireMots = new Scanner(mots);
            int nb =0;
            
            while (lireMots.hasNext()) {
              String ligne = lireMots.nextLine();
              if (((ligne.length() >= min ) && (ligne.length() <= max )) && (nomsPropres || ligne.matches(nomCommuns)) 
                      && (nomsAccents || ligne.matches(nomSansAccents))){
                //System.out.println(ligne);
                ecrireMots.write(ligne + "\n");
                nb++;
                nbrMots++;
              }
            }
            
            lireMots.close();
            ecrireMots.close();
            System.out.println("Il y a " + nb + " mots retenus\n");
            return mot;
            } 
        catch (FileNotFoundException e) {
            System.out.println("Une erreur est survenue lors de la lecture du fichier " +  fichierLecture );
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
                System.out.println("Erreur lors de l'écriture dans le fichier "  + fichierEcriture);
                e.printStackTrace();
                return null;
        } 
    }// Pendu
    
    public void Jeu(String motChoisi){
        
        
        // créations des variables
        boolean ok = true;
        char[]lettreJeu = new char[motChoisi.length()];
        char[]corriger = new char[motChoisi.length()];
        char[] lettreEssayer = new char[NEM];
        int verificateur = 0;
        int verifierGagnant = 0;
        int compteur = 0;
        int faute = 0;
        char lettreC = 0;
        
        // création du tableau de fautes
        for(int y = 0; y<NEM;y++){
            lettreEssayer[y] = '-';
        }
        
        // création du tableau pour jouer et son corriger
        for(int i = 0; i < motChoisi.length(); i++){
           lettreJeu[i] = motChoisi.charAt(i);
           corriger[i] = motChoisi.charAt(i);

            if(lettreJeu[0] == lettreJeu[i]){
                
               lettreJeu[i] =lettreJeu[0];
               verifierGagnant++;
               
            } else if(lettreJeu[0] !=lettreJeu[i] ||lettreJeu[motChoisi.length()-1] !=lettreJeu[i]){
               lettreJeu[i] = '-';
            }
        }
        
        for(int j = corriger.length-1; j >= 0; j--){
            //System.out.println(lettreJeu[j]);
            if(corriger[j] == corriger[corriger.length-1]) {
                
               lettreJeu[j] = corriger[corriger.length-1];
               verifierGagnant++; 
            }
        }
        
        System.out.println("Mot à trouver:");
        
        // le jeu est joué a l'infini
        while(ok = true){
            // montre le mot caché
            for (char c :lettreJeu) {
                System.out.print(c + " ");
            }
            
            String lettre = Utilitaires.lireChaine("\n    Deviner une lettre: ");
            if(lettre != "") lettreC = Utilitaires.deStringVersChar(lettre);
            else System.out.println("Chaîne vide");
            
            
            // vérifie si le char est dans le corrigé
            for(int i = 0; i < corriger.length; i++){
                
                // si c'est le cas, alors montrer la lettre dans le tableau du jeu
                if(corriger[i] == lettreC && lettreJeu[i] != lettreC){
                    lettreJeu[i] = corriger[i];
                    verifierGagnant++;
                    verificateur++;
                } else if(corriger[i] == lettreJeu[i] && lettreJeu[i] == lettreC){
                    System.out.print(corriger[i] + " a déja été utilisée. Lettres essayées: [");
                    for(int j = 0; j < lettreEssayer.length; j++){
                            System.out.print(lettreEssayer[j] + "");
                        }
                        System.out.print("]");
                        System.out.println();
                        verificateur++;
                }
                
            }
            
            // si le mot n'est pas encore vérifié
            if(verificateur == 0){
                
                for(int x = 0; x < corriger.length; x++){
                    
                    // si la lettre n'est pas dans le mot caché
                    if (corriger[x] != lettreC && !Character.isDigit(lettreC)){
                        System.out.print("      " + lettre + " n'est pas dans le mot. Reste: " + (NEM-(faute + 1)) + " Chances. "
                                + "Lettres essayées: [");
                        lettreEssayer[faute] = lettreC;
                        for(int j = 0; j < lettreEssayer.length; j++){
                            System.out.print(lettreEssayer[j] + "");
                        }
                        System.out.print("]");
                        System.out.println();
                        verificateur++;
                        faute++;
                        break;
                    
                    // si le char n'est pas une lettre    
                    }else if(!Character.isLetter(lettreC)){
                        System.out.println(lettre + " n'est pas une lettre");
                        verificateur++;
                        faute++;
                        break;
                    }
                }
            }
            
            verificateur = 0;
            compteur++;
            
            // si il n'y a plus de chances
            if(faute == lettreEssayer.length){
                System.out.println("Fin de round, vous avez perdu!\n");
                System.out.println("Le mot était: " + motChoisi);
                System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
                ok = false;
                break;
            
            // si le joueur a trouvé le mot
            } else if(verifierGagnant == lettreJeu.length){
                System.out.println("Vous avez trouvé le mot: " + motChoisi);
                System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
                ok = false;
                break;
            }
 
        }
        
    }// Jeu
    
    public void resoudre()throws IOException {
        
        // création de variables et activation de la méthode pendu
        boolean continuer = true;
        
        new RandomAccessFile("listeMotsRetenus.txt","rw").setLength(0);
        
        Pendu();
        
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        
        // liste que prend le String d'un fichier
        String[] test;
        List<String> listOfStrings = new ArrayList<String>();
        ArrayList<Character> alphabets = new ArrayList<Character>();
        String alpha = "^[a-z áÁâÂàÀåÅãÃäÄæÆçÇéÉêÊèÈëËíÍîÎìÌïÏñÑóÓôÔòÒøØõÕöÖœŒšŠßðÐþÞúÚûÛùÙüÜýÝÿŸ].*$";
        
        for(int i = 0; i < alpha.length();i++){
            alphabets.add(alpha.charAt(i));
        }
       
        // prend l'information du fichier
        BufferedReader bf = new BufferedReader(new FileReader("listeMotsRetenus.txt"));
       
        //lit la ligne entière comme un String
        String ligne = bf.readLine();
       
        // vérifie pour la fin d'un fichier
        while (ligne != null) {
            listOfStrings.add(ligne);
            ligne = bf.readLine();
        }
       
        bf.close();
       
        // garde les données dans un tableau
        String[] mot = listOfStrings.toArray(new String[0]);
        
        while(continuer = true){
            int motChoisi = Utilitaires.genererAleatoire(nbrMots, 1);
            Jeu(mot[motChoisi]);
        }
        
    }// resoudre
    
}// class
