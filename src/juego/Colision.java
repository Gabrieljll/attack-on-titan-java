package juego;

import java.util.Random;

import entorno.Entorno;

public class colision {
	Kyojin[] kyo;
	//Mikasa mik;
	colision(){
		//this.kyo = new Kyojin[4];
		//mik = new Mikasa(100,100);
		
	}

	//true si hay colison Mikasa con Kyojin
	public boolean MikasaKyojin(Kyojin[] k, Mikasa m) {
		boolean colision= false;
		for(Kyojin kyo : k) {
			if((m.getPosX() > kyo.getPosX() - kyo.ancho/2) && 
			   (m.getPosX() < kyo.getPosX() + kyo.ancho/2) &&
			   (m.getPosY() > kyo.getPosY() - kyo.alto/2) &&
			    m.getPosY() < kyo.getPosY() + kyo.alto/2) {
				colision = true;
			}
			 
		}
		return colision;
	          
	}
	public void colisionKyojines(Kyojin[] k) {
		for(int i=0; i<k.length;i++) {
			for(int j =1; j<k.length;j++) {
				if(	   (k[i].getPosX() >= k[j].getPosX()-k[j].ancho/2) &&
					   (k[i].getPosX() <= k[j].getPosX()+k[j].ancho/2) &&
					   (k[i].getPosY() >= k[j].getPosY()-k[j].alto/2) &&
					   (k[i].getPosY() <= k[j].getPosY()+k[j].alto/2)    ){
					
					k[i].angulo-=90;
					k[j].angulo+=90;
				}
			}
		
		}
	}
	
	public boolean kxk(Kyojin[] k) {
		boolean colision= false;
		for(int i=0; i<k.length;i++) {
			for(int j =1; j<k.length;j++) {
				if((k[i].getPosX() >= k[j].getPosX() - k[j].ancho/2) && 
				   (k[i].getPosX() <= k[j].getPosX() + k[j].ancho/2) &&
				   (k[i].getPosY() >= k[j].getPosY() - k[j].alto/2) &&
				   (k[i].getPosY() <= k[j].getPosY() + k[j].alto/2)) {
							colision = true;
				}
			}	 
		}
		return colision;
	          
	}
	
	public void colisionKyojin(Kyojin[] k) {
		for(int i = 0; i <k.length;i++) {
				if(kxk(k)) {
					 k[i].angulo += 90;
				}
		}
	}
	
	
	

}
