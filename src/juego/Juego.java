package juego;


import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// Variables y métodos propios de cada grupo
	Mikasa mikasa;
	Proyectil[] proyectiles;
	Kyojin[] kyojines;
	Obstaculo[] obstaculos;
	colision c;
	Random r = new Random();
	Fondo fondo;
	int vidas;

	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan, Final Season - Grupo ... - v1", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		//c = new colision();
		obstaculos = new Obstaculo[4];
		obstaculos[0] = new Obstaculo(600,150); //árbol
		obstaculos[1] = new Obstaculo(200,180); //casa
		obstaculos[2] = new Obstaculo(500,320); //casa
		obstaculos[3] = new Obstaculo(150,460); //árbol
		
		fondo = new Fondo();
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);
		vidas=100;		
		proyectiles = new Proyectil[4];
		kyojines = new Kyojin[4];
		
		for(int i =0; i<kyojines.length;i++) {
			int x=0;
			int y=0;
			boolean posOk = false;
			
			while(posOk==false){
				x = r.nextInt(entorno.ancho());
				y = r.nextInt(entorno.alto());
				for(int j=0;j<obstaculos.length;j++) {
					if(obstaculos[j].coincidePos(x, y)==false && x!=mikasa.getPosX() && y!=mikasa.getPosY()){
						posOk = true; 
					}
					else {
						posOk = false;
						break; // rompe todo el for o esa pasada del for?
					}			
				}
				// está bien kyojines que puedan salir solapados?
			}
			kyojines[i] = new Kyojin(x,y,20,60);
		}
				
		
		

		
	
		
			
		
		// ...

		// Inicia el juego!
		this.entorno.iniciar();
		
	}

	/**
	 * Durante el juego, el método tick() será ejecutado en cada instante y 
	 * por lo tanto es el método más importante de esta clase. Aquí se debe 
	 * actualizar el estado interno del juego para simular el paso del tiempo 
	 * (ver el enunciado del TP para mayor detalle).
	 */
	public void tick()
	{
		// Procesamiento de un instante de tiempo
		fondo.dibujarse(this.entorno);
		
		obstaculos[0].dibArbol(entorno);
		obstaculos[1].dibCasa(entorno);
		obstaculos[2].dibCasa2(entorno);
		obstaculos[3].dibArbol(entorno);
		
		//Mikassa 
		mikasa.mover(entorno);
		mikasa.dibujarse(entorno);
		mikasa.limiteDeCiudad(entorno);
		mikasa.disparar(entorno,this.proyectiles);
		
		//Proyectiles
		for(int i=0;i<proyectiles.length;i++){
			if(proyectiles[i]!=null){
				proyectiles[i].dibujarse(entorno);
				proyectiles[i].mover();
				if(proyectiles[i].limiteDeCiudad(entorno)){
					proyectiles[i] = null;
				}
			}
		}
		
		
		//Kyojines
		for(Kyojin k : kyojines) {
			if(k!=null){
				k.dibujarse(entorno);
				k.moverse();
				k.limiteDeCiudad(entorno);
			}
		}
		//c.colisionKyojines(kyo);
		//c.colisionKyojin(kyo);
		
		/*if(c.MikasaKyojin(kyo, mikasa)) {
			vidas--;
			if(vidas<=0) {
				mikasa = null;
			}
		}*/
		entorno.escribirTexto("Vidas: " + vidas, 700, 100);
		
		
		// ...
		

	}
	

	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
