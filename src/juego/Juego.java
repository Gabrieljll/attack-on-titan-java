package juego;


import java.awt.Color;
import java.awt.Image;
import java.util.Calendar;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	// Variables y métodos propios de cada grupo
	private Mikasa mikasa;
	private Proyectil[] proyectiles;
	private Kyojin[] kyojines;
	private Obstaculo[] obstaculos;
	private Suero suero;
	private Random r = new Random();
	private Fondo fondo;
	private int vidasContador;
	private double distObstaculos;
	private double distSuero;
	private double distRadar;
	private int itSuero;
	private int itKyogin;
	private int kyojinesEliminados;
	private boolean juegoFinalizado;
	private Image img1 = Herramientas.cargarImagen("resources/mikaDer.png");		
	private Image img2 = Herramientas.cargarImagen("resources/mikaTitanDer.png");
	private Image imagenFondo = Herramientas.cargarImagen("resources/pasto.jpg");
	private Image imagenFondoGameOver = Herramientas.cargarImagen("resources/fondo-gameover.jpg");
	// ...
	
	Juego()
	{
		this.juegoFinalizado = false;
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan, Final Season - Grupo 5 - v1", 800, 600);
		
		// Inicializar lo que haga falta para el juego
		//c = new colision();
		obstaculos = new Obstaculo[5];
		obstaculos[0] = new Obstaculo(600,150); //árbol
		obstaculos[1] = new Obstaculo(200,180); //casa
		obstaculos[2] = new Obstaculo(500,320); //casa
		obstaculos[3] = new Obstaculo(150,460); //árbol
		obstaculos[4] = new Obstaculo(700,500); //casa
		
		// Distancias
		distObstaculos = 80;
		distSuero = 20;
		distRadar = 100;
		
		// Iteradores
		itSuero = 0;
		itKyogin = 0;
		 
		fondo = new Fondo();
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);		
		proyectiles = new Proyectil[4];
		kyojines = new Kyojin[4];
		
		
		resetearKyojines();

			
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
		if(this.juegoFinalizado == false) {
			// Procesamiento de un instante de tiempo
			
			fondo.dibujarse(this.entorno, imagenFondo, 2);			
			
			obstaculos[0].dibArbol(entorno);
			obstaculos[1].dibCasa(entorno);
			obstaculos[2].dibCasa2(entorno);
			obstaculos[3].dibArbol(entorno);
			obstaculos[4].dibCasa2(entorno);
			
			if(r.nextInt(650) < 1 && suero==null && !mikasa.getMikasaTitan()){
				double[] nuevaPos = this.generarPos();
				suero = new Suero(nuevaPos[0],nuevaPos[1]);
			}
			
			if(suero!=null) {
				suero.dibujarse(entorno);
			}
			
			//Mikassa 
			
			mikasa.mover(entorno);
			
			if( !mikasa.mikasaTitan){
				mikasa.dibujarse(entorno, this.img1);	
			}		
			else{
				mikasa.dibujarse(entorno, this.img2);
			}
			
			if(suero!=null && mikasa.chocaSuero(suero, distSuero)){
				suero = null;
				mikasa.seVuelveTitan();			
			}
			
			
			//mikasa.limiteDeCiudad(entorno);
			mikasa.disparar(entorno,this.proyectiles);
			
			//Mikasa Colision obstaculos
			Obstaculo obstaculoChocado = mikasa.colisionObstaculo(obstaculos, distObstaculos);
			if(obstaculoChocado != null) {
				mikasa.esquivarObstaculo(entorno, obstaculoChocado);
			}
	
			//Proyectiles
			for(int i=0;i<proyectiles.length;i++){
				if(proyectiles[i]!=null){
					proyectiles[i].dibujarse(entorno);
					proyectiles[i].mover();
					if(proyectiles[i].limiteDeCiudad(entorno)){
						this.eliminarProyectil(proyectiles[i]);
					}
				}
			}
			
			//Timmer Transformación
			if(mikasa.getMikasaTitan()&& itSuero<1000) { // unos 15 segundos aprox
				itSuero++;
			}else{
				mikasa.seVuelveNormal();
				itSuero = 0;
			}
			
			////Colisiones kyojines
			for(int i=0;i<kyojines.length-1;i++) {
				for(int j=i+1;j<kyojines.length;j++) {
					if(kyojines[i]!=null&&kyojines[j]!=null) {
						if(kyojines[i].colisionKyojin(kyojines[j], 70)) {
							kyojines[i].setAngulo(Herramientas.radianes(r.nextInt(90)));
							kyojines[i].moverse();
							kyojines[j].setAngulo(Herramientas.radianes(150));
							kyojines[j].moverse();
						}
					}
				}
			}
			////
			
			//Kyojines
			//for(Kyojin kyojin : kyojines) {
			for(int i=0;i<kyojines.length;i++) {
				if(kyojines[i]!=null){
					kyojines[i].dibujarse(entorno);
					kyojines[i].moverse();
					kyojines[i].limiteDeCiudad(entorno);					
					Obstaculo obstaculoChocadoKyo = kyojines[i].colisionObstaculos(obstaculos, distObstaculos); 
					if(obstaculoChocadoKyo != null) {
						kyojines[i].esquivarObstaculo(entorno, obstaculoChocadoKyo);
					}else {
						kyojines[i].radar(mikasa, distRadar);
					}
					
					//Mikasa Colision Kyojin
					boolean kyojinChocado = mikasa.colisionKyogin(kyojines[i], distObstaculos);
					if(kyojinChocado == true && mikasa.mikasaTitan == true) {
						this.eliminarKyojin(kyojines[i]);
						mikasa.mikasaTitan=false;
						kyojinesEliminados++;
					}
					else if (kyojinChocado == true && mikasa.mikasaTitan == false){
						if(mikasa.getVidas()-1 > 0){
							this.vidasContador++;
							this.resetearSpawns();
						}
						else {
							this.juegoFinalizado=true;
						}
					}
					else {
						// En caso que no haya muerto por colisión checkeo si muere por proyectiles
						for(Proyectil proyectil : proyectiles) {
							if(proyectil !=null) {
								boolean kyojinBaleado = proyectil.colisionKyojin(kyojines[i], distObstaculos);
								if(kyojinBaleado) {
									this.eliminarKyojin(kyojines[i]);
									this.eliminarProyectil(proyectil);
									kyojinesEliminados++;
									break; // Ya muerto, no recorremos más proyectiles
								}	
							}
						}
					}
						
					if(kyojinChocado == true && mikasa.mikasaTitan == false) {
						mikasa.mikasaTitan=false;					
					}
	
				}
				else{ // timmer para respawn de kyogin
					itKyogin++;
					if(itKyogin==400) {
						double[] nuevaPos = this.generarPos();
						kyojines[i] = new Kyojin(nuevaPos[0],nuevaPos[1],20,60);
						itKyogin = 0; 
					}
				}
			}
			
			entorno.cambiarFont("Arial", 32, Color.yellow);
		    entorno.escribirTexto("Vidas: " + mikasa.getVidas(), 650, 60);
		    entorno.escribirTexto("Kyojines eliminados: " + this.kyojinesEliminados, 60,590);		    
			
			// ...
		}
		else {
			this.resetearKyojines();
			fondo.dibujarse(this.entorno, imagenFondoGameOver, 1);
			entorno.cambiarFont("Arial", 25, Color.green);
			entorno.escribirTexto("Presiona Barra Espacio para reintentar", 185, 430);
			if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
				mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);
				this.kyojinesEliminados=0;
				this.vidasContador=0;
				this.juegoFinalizado=false;				
			}
		}
	}
	
	
	public void resetearSpawns() {		
		mikasa = null;
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);
		mikasa.dibujarse(entorno, img1);
		mikasa.setVidas(mikasa.getVidas()-this.vidasContador);	
		this.resetearKyojines();
	}
	
	public void eliminarProyectil(Proyectil proyectilLanzado) {
		for( int i = 0; i< proyectiles.length; i++) {
			if ( proyectiles[i]!=null && proyectiles[i].equals(proyectilLanzado)) {		
				proyectiles[i]=null;	
			}
		}
	}
	
	public void resetearKyojines() {
		//Desecho si había
		for( int i = 0; i< kyojines.length; i++) {
			if(kyojines[i]!=null){
				eliminarKyojin(kyojines[i]);
			}	
		}
		//Creo cuatro nuevos
		for( int i = 0; i< kyojines.length; i++) {
			double[] nuevaPos = this.generarPos();
			kyojines[i] = new Kyojin(nuevaPos[0],nuevaPos[1],20,60);
		}
		
		
	}
	public void eliminarKyojin(Kyojin kyojin) {
		for( int i = 0; i< kyojines.length; i++) {
			if ( kyojines[i]!=null && kyojines[i].equals(kyojin)) {		
				kyojines[i]=null;	
			}
		}
	}
	
	public double[] generarPos() {
		double x=0;
		double y=0;
		boolean posOk = false;
		//Evitamos solapamiento en el respawn
		while(posOk==false){
			x = r.nextInt(entorno.ancho());
			y = r.nextInt(entorno.alto());
			for(int j=0;j<obstaculos.length;j++) {
				if(obstaculos[j].coincidePos(x, y, distObstaculos)==false && mikasa.coincidePos(x, y, mikasa.getDistancia())== false){
					posOk = true;
				}
				else {
					posOk = false;
					break; // ¿¿rompe todo el for o esa pasada del for??
				}			
			}
			// ¿¿está bien kyojines que puedan salir solapados???
		}
		return new double[]{x,y};	
	}
	
	

	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
