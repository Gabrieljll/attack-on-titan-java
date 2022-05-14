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
	Proyectil[] proyectil;
	Kyojin[] kyo;
	colision c;
	Random r = new Random();
	int vidas;

	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan, Final Season - Grupo ... - v1", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		c = new colision();
		mikasa = new Mikasa(100,100);
		vidas=100;		
		proyectil = new Proyectil[4];
		kyo = new Kyojin[4];
		for(int i =0; i<kyo.length;i++) {
			kyo[i] = new Kyojin(r.nextInt(entorno.ancho()),r.nextInt(entorno.alto())-20,20,60);
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
		

		//Mikassa 
		mikasa.mover(entorno);
		mikasa.dibujarse(entorno);
		mikasa.limiteDeCiudad(entorno);
		mikasa.disparar(entorno,this.proyectil);
		
		//Proyectiles
		for(int i=0;i<proyectil.length;i++){
			if(proyectil[i]!=null){
				proyectil[i].dibujarse(entorno);
				proyectil[i].mover();
				if(proyectil[i].limiteDeCiudad(entorno)){
					proyectil[i] = null;
				}
			}
		}
		
		
		//Kyojines
		for(Kyojin k : kyo) {
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
