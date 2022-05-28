package juego;


import java.awt.Color;
import java.awt.Image;
import java.util.Calendar;
import java.util.Random;

import javax.sound.sampled.Clip;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego
{
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	
	//Instanciamos Variables y métodos propios de cada grupo
	private Fondo fondo;
	private Mikasa mikasa;
	private Proyectil[] proyectiles;
	private Kyojin[] kyojines;
	private Kyojin kyojinJefe;
	private Obstaculo[] obstaculos;
	private Suero suero;
	
	private Random r = new Random();	
	
	private int vidasMikasa;
	private double distObstaculos;
	private double distSuero;
	private double distRadar;
	private boolean juegoFinalizado;
	private boolean jefeFinal;
	private boolean mikasaGana;
	private int itSuero;
	private int itKyogin;
	private int kyojinesEliminados;
	private int vidasJefe;
	private double velocidadJefe;
	private double velocidadJefeAumentada;
	private double velocidadKyojines;
	private double velocidadKyojinesAumentada;
	//carga de imágenes
	private Image img1 = Herramientas.cargarImagen("resources/mikaDer.png");		
	private Image img2 = Herramientas.cargarImagen("resources/mikaTitanDer.png");
	private Image imagenFondo = Herramientas.cargarImagen("resources/pasto.jpg");
	private Image imagenFondoGameOver = Herramientas.cargarImagen("resources/fondo-gameover.jpg");
	private Image imagenFondoWin = Herramientas.cargarImagen("resources/win.jpg");
	private Image imagenKyojinNormal = Herramientas.cargarImagen("resources/kyojinIzq.png");
	private Image imagenKyojinJefe = Herramientas.cargarImagen("resources/TITAN2.png");
	//carga de audios
	private Clip sonVictoria = Herramientas.cargarSonido("resources/victoria.wav");
	private Clip sonProyectil = Herramientas.cargarSonido("resources/proyectil.wav");
	private Clip sonPerder = Herramientas.cargarSonido("resources/perder.wav");
	private Clip sonMuereKyojin = Herramientas.cargarSonido("resources/muereKyojin.wav");
	private Clip sonGritoJefeGano = Herramientas.cargarSonido("resources/gritoJefeGano.wav");
	private Clip sonGritoJefePerdio = Herramientas.cargarSonido("resources/gritoJefePerdio.wav");
	private Clip sonGritoMika = Herramientas.cargarSonido("resources/gritoMika2.wav");
	private Clip sonAmbiente = Herramientas.cargarSonido("resources/ambiente2.wav");
		
	
	// ...
	
	Juego()
	{
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Attack on Titan, Final Season - Grupo 5 - v1", 800, 600);

		// Inicializar lo que haga falta para el juego
		fondo = new Fondo();		
				
		this.juegoFinalizado = false;
		this.jefeFinal = false;
		this.mikasaGana = false;
		this.velocidadKyojines= 0.8;
		this.velocidadKyojinesAumentada= 0.9;
		this.velocidadJefe= 0.9;
		this.velocidadJefeAumentada=1;
		
		obstaculos = new Obstaculo[5];
		obstaculos[0] = new Obstaculo(600,150); //árbol
		obstaculos[1] = new Obstaculo(200,180); //casa
		obstaculos[2] = new Obstaculo(500,320); //casa
		obstaculos[3] = new Obstaculo(150,460); //árbol
		obstaculos[4] = new Obstaculo(700,500); //casa
	
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);	
		kyojines = new Kyojin[4];
		kyojinJefe = new Kyojin(0,0,10,20, this.velocidadJefe);
		proyectiles = new Proyectil[4];
		
		// Distancias
		distObstaculos = 80;
		distSuero = 20;
		distRadar = 150;
		
		// Iteradores
		itSuero = 0;
		itKyogin = 0;
		
		//Vidas
		vidasMikasa = 3;
		vidasJefe = 5;
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
		// Procesamiento de un instante de tiempo
		if(this.juegoFinalizado == false && this.jefeFinal == false) {
			this.sonAmbiente.loop(10);
			this.juegoGeneral();
		}
		else if(this.juegoFinalizado == false && this.jefeFinal == true) {
			this.sonAmbiente.loop(10);
			this.juegoJefeFinal();
		}
		else if (this.juegoFinalizado == true && this.jefeFinal == false){
			this.finDelJuego(mikasaGana);
		}
	}
	
		public void juegoGeneral(){
			
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
			mikasa.limiteDeCiudad(entorno);
			mikasa.disparar(entorno,this.proyectiles);
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
			
			//Timer Transformación
			if(mikasa.getMikasaTitan()&& itSuero<1000) { // unos 15 segundos aprox
				itSuero++;
			}else{
				mikasa.seVuelveNormal();
				itSuero = 0;
			}
				
				
				////Colisiones kyojines
				for(int i=0;i<kyojines.length-1;i++) {
					for(int j=i+1;j<kyojines.length;j++) {
						if(kyojines[i]!=null && kyojines[j]!=null) {
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
					kyojines[i].dibujarse(entorno, imagenKyojinNormal, 0.2);
					kyojines[i].moverse();
					kyojines[i].limiteDeCiudad(entorno);					
					Obstaculo obstaculoChocadoKyo = kyojines[i].colisionObstaculos(obstaculos, distObstaculos); 
					if(obstaculoChocadoKyo != null) {
						kyojines[i].esquivarObstaculo(entorno, obstaculoChocadoKyo);
					}else {
						if(kyojines[i].radar(mikasa, distRadar)==true){
							kyojines[i].setVelocidad(this.velocidadKyojinesAumentada);
						}
						else {
							kyojines[i].setVelocidad(this.velocidadKyojines);
						}
					}
					
					//Mikasa Colision Kyojin
					boolean kyojinChocado = mikasa.colisionKyojin(kyojines[i], 40);
					if(kyojinChocado == true && mikasa.mikasaTitan == true) {
						this.eliminarKyojin(kyojines[i]);
						Herramientas.play("resources/muereKyojin.wav");
						mikasa.mikasaTitan=false;
						kyojinesEliminados++;
					}
					else if (kyojinChocado == true && mikasa.mikasaTitan == false){
						vidasMikasa--;
						if(this.vidasMikasa > 0){
							this.resetearSpawns();
							this.sonGritoMika.start();
							this.sonGritoMika.setMicrosecondPosition(0);
						}
						else {
							this.juegoFinalizado=true;
							this.sonPerder.start();
							
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
									Herramientas.play("resources/gritoKyo2.wav");
									kyojinesEliminados++;
	
									break; // Ya muerto, no recorremos más proyectiles
								}	
							}
						}
					}
						
					if(kyojinesEliminados >= 6) {
						this.jefeFinal = true;
					}
					if(kyojinChocado == true && mikasa.mikasaTitan == false) {
						mikasa.mikasaTitan=false;					
					}
	
				}
				else{ // timer para respawn de kyogin
					itKyogin++;
					if(itKyogin==400) {
						double[] nuevaPos = this.generarPos();
						kyojines[i] = new Kyojin(nuevaPos[0],nuevaPos[1],10,20, this.velocidadKyojines);
						itKyogin = 0; 
					}
				}
			}
			
			entorno.cambiarFont("Arial", 32, Color.yellow);
		    entorno.escribirTexto("Vidas: " + this.vidasMikasa, 650, 60);
		    entorno.escribirTexto("Kyojines eliminados: " + this.kyojinesEliminados, 60,590);		    
			
			// ...
		}

		public void juegoJefeFinal(){
			
			//Mikasa
			fondo.dibujarse(this.entorno, imagenFondo, 2);
			mikasa.mover(entorno);
			mikasa.dibujarse(entorno, this.img1);		
			mikasa.disparar(entorno,this.proyectiles);
			mikasa.limiteDeCiudad(entorno);
			
			
			//KyojinJefe
	
			if(kyojinJefe != null) {			
				kyojinJefe.dibujarse(entorno, imagenKyojinJefe, 1);
				kyojinJefe.moverse();
				kyojinJefe.limiteDeCiudad(entorno);
				if(kyojinJefe.radar(mikasa, distRadar)==true){
					kyojinJefe.setVelocidad(this.velocidadJefeAumentada);
				}
				else {
					kyojinJefe.setVelocidad(this.velocidadJefe);
				}
				boolean kyojinChocado = mikasa.colisionKyojin(kyojinJefe, 40);
				if(kyojinChocado == true ) {
					vidasMikasa--;
					if(this.vidasMikasa > 0){
						mikasa.setX(this.entorno.ancho()-kyojinJefe.getX());;
						mikasa.setY(this.entorno.alto()-kyojinJefe.getY());;
						this.sonGritoMika.start();
						this.sonGritoMika.setMicrosecondPosition(0);
						this.generarPosJefe(kyojinJefe);
					}
					else {
						this.jefeFinal=false;
						this.juegoFinalizado=true;
						this.mikasaGana=false;
						this.sonPerder.start();
					}
				}
				
				for(Proyectil proyectil : proyectiles) {
					if(proyectil !=null) {
						boolean kyojinBaleado = proyectil.colisionKyojin(kyojinJefe, 40);
						if(kyojinBaleado) {
							vidasJefe--;
							this.eliminarProyectil(proyectil);
							if(vidasJefe==1) {
								Herramientas.play("resources/gritoJefePerdio.wav");
							    this.sonGritoJefePerdio.start();
								this.sonGritoJefePerdio.setMicrosecondPosition(0);
							}
							else if(vidasJefe==0) {
								this.eliminarKyojin(kyojinJefe);
								this.jefeFinal=false;
								this.juegoFinalizado=true;
								this.mikasaGana=true;
								
								this.sonVictoria.start();
		
							}else {
								Herramientas.play("resources/gritoKyo2.wav");
								this.generarPosJefe(kyojinJefe);
							}
							
							break; // Ya muerto, no recorremos más proyectiles
						}
					}
				}
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
			
			entorno.cambiarFont("Arial", 32, Color.yellow);
		    entorno.escribirTexto("Vidas: " + this.vidasMikasa, 650, 60);
		    entorno.escribirTexto("Vidas Jefe Final: " + this.vidasJefe, 60,590);	
		}
	
	public void finDelJuego(boolean mikasaGana) {
		this.sonAmbiente.stop();
		if ( mikasaGana == false ) {
			this.resetearKyojines();
			fondo.dibujarse(this.entorno, imagenFondoGameOver, 1);
			entorno.cambiarFont("Arial", 25, Color.green);
			entorno.escribirTexto("Presiona ENTER para reintentar", 220, 430);
			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				this.kyojinesEliminados=0;
				this.resetearSpawns();
				this.vidasMikasa=3;
				this.vidasJefe=5;
				this.juegoFinalizado=false;
				this.sonPerder.stop();
				//this.sonPerder.setMicrosecondPosition(0);
				
			}	
		}
		else { //Mikasa ganó
			fondo.dibujarse(this.entorno, imagenFondoWin, 2.5);
			entorno.cambiarFont("Arial", 25, Color.white);
			entorno.escribirTexto("Presiona ENTER para jugar de nuevo", 190, 400);
			if (entorno.sePresiono(entorno.TECLA_ENTER)) {
				this.kyojinesEliminados=0;
				this.resetearSpawns();
				this.vidasMikasa=3;
				this.vidasJefe=5;
				this.juegoFinalizado=false;
				this.mikasaGana=false;
				kyojinJefe = new Kyojin(0,0,30,70, this.velocidadJefe);
				this.generarPosJefe(kyojinJefe);
				this.sonVictoria.stop();
				this.sonVictoria.setMicrosecondPosition(0);
			}	
		}		
	}
	
	public void resetearSpawns() {		
		mikasa = null;
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);
		mikasa.dibujarse(entorno, img1);
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
			kyojines[i] = new Kyojin(nuevaPos[0],nuevaPos[1],20,60, this.velocidadKyojines);
		}	
	}
	
	public void eliminarKyojin(Kyojin kyojin) {
		if (!kyojin.equals(kyojinJefe)) {
			for( int i = 0; i< kyojines.length; i++) {
				if ( kyojines[i]!=null && kyojines[i].equals(kyojin)) {		
					kyojines[i]=null;	
				}
			}
		}
		else {
			this.kyojinJefe = null;
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
					break; // 
				}			
			}
			// ¿¿está bien kyojines que puedan salir solapados???
		}
		return new double[]{x,y};	
	}
	
	public void generarPosJefe(Kyojin jefe) {
		double x=0;
		double y=0;
		boolean posOk = false;
		//Evitamos solapamiento en el respawn
		while(posOk==false){
			x = r.nextInt(entorno.ancho());
			y = r.nextInt(entorno.alto());
			if(mikasa.coincidePos(x, y, mikasa.getDistancia())== false){
				posOk = true;
			}
			else {
				posOk = false; 
			}			
		}
		jefe.setX(x);
		jefe.setY(y);
			
	}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
