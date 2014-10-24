import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class Main {
	
	static int[] poids;
	static int[] gris;
	
	static int nbCouleurs = 3;
	
	static int[] pal = { 0, 0, 0, 0, 0,
						20, 20, 20, 20, 20,
						100, 132, 164, 164, 180,
						255, 255, 255, 255, 255,
						255, 255, 255, 255, 255 };
	
	static Integer[][] tmpTab;
	static Integer[] reductionTab;

	public static void main(String[] args) {	
		int[] finale = calculReduction(pal);
		System.out.print("Finale: ");
		afficheTab(finale);
	}
	
	public static int[] calculReduction(int[] base) {
		Map<Integer, Integer> grisPoids = new HashMap<Integer, Integer>();
		int[] finale = new int[base.length];
		
		for(int i : base) {
			if(grisPoids.containsKey(i))
				grisPoids.put(i, grisPoids.get(i) + 1);
			else
				grisPoids.put(i, 1);
		}
		
		poids = new int[grisPoids.keySet().size()];
		gris = new int[poids.length];
		int j = 0;
		for(int i : grisPoids.keySet()) {
			gris[j] = i;
			poids[j] = grisPoids.get(i);
			j++;
		}
		
		System.out.print("Base: ");
		afficheTab(base);
		
		System.out.print("GrisBase: ");
		afficheTab(gris);
		
		System.out.print("PoidsBase: ");
		afficheTab(poids);
		
		tmpTab = new Integer[nbCouleurs][poids.length];
		reductionTab = new Integer[nbCouleurs-1];
		
		System.out.print("Distance min: ");
		System.out.println(DM(0, nbCouleurs));
		
		Arrays.sort(reductionTab);
		System.out.print("Index de réduction: ");
		afficheTab(reductionTab);
		
		int[] poidsFinale = new int[nbCouleurs];
		int[] grisFinale = new int[nbCouleurs];
		
		for(int i = 0; i < nbCouleurs; i++) {
			if(i == 0) {
				grisFinale[i] = meilleurGris(0, reductionTab[i]);
				poidsFinale[i] = poidsTranche(0, reductionTab[i]);
			}
			else if(i < reductionTab.length) {
				grisFinale[i] = meilleurGris(reductionTab[i-1], reductionTab[i]);
				poidsFinale[i] = poidsTranche(reductionTab[i-1], reductionTab[i]);
			}
			else {
				grisFinale[i] = meilleurGris(reductionTab[i-1], poids.length);
				poidsFinale[i] = poidsTranche(reductionTab[i-1], poids.length);
			}
		}
		
		System.out.print("GrisFinale: ");
		afficheTab(grisFinale);
		
		System.out.print("PoidsFinale: ");
		afficheTab(poidsFinale);
		
		int index = 0;
		for(int i = 0; i < nbCouleurs; i++) {
			for(int m = 0; m < poidsFinale[i]; m++) {
				finale[index++] = grisFinale[i];
			}
		}
		
		return finale;
	}
	
	private static int poidsTranche(int debut, int fin) {
		int resultat = 0;
		for(int i = debut; i < fin; i++) {
			resultat += poids[i];
		}
		
		return resultat;
	}
	
	public static int meilleurGris(int debut, int fin) {
		int num = 0;
		int den = 0;
		
		if(debut == fin) {
			return gris[debut];
		}
		
		for(int i = debut; i < fin; i++) {
			num += (poids[i] * gris[i]);
			den += poids[i];
		}
		
		return (num/den);
	}
	
	public static int distanceMin(int debut, int fin) {
		int resultat = 0;
		int grisFusionnes = meilleurGris(debut, fin);
		
		for(int i = debut; i < fin; i++) {
			resultat += poids[i] * Math.pow((gris[i] - grisFusionnes),2);
		}
		
		return resultat;
	}

	public static int DM(int l, int k) {
		if(tmpTab[k-1][l] != null) {
			return tmpTab[k-1][l];
		}
		
		if(k-1 == 0) {
			tmpTab[k-1][l] = distanceMin(l, poids.length);
			return tmpTab[k-1][l];
		}
		else {
			int min = Integer.MAX_VALUE;
			Integer indexMin = null;
			
			for(int i = l; i < poids.length; i++) {
				int tmp = distanceMin(l, i) + DM(i, k-1);
				
				if(tmp < min) {
					min = tmp;
					indexMin = i;
				}
			}
			
			tmpTab[k-1][l] = min;
			reductionTab[k-2] = indexMin;
			
			return min;
		}
	}
	
	private static void afficheTab(int[] tab) {
		for(int i : tab) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
	
	private static void afficheTab(Integer[] tab) {
		for(Integer i : tab) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
}
