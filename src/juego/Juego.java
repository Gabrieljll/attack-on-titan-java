package juego;


import java.awt.Color;
import java.awt.Image;
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
	private Municion municion;
	
	private Random r = new Random();	
	
	// Iteradores
	private int itSuero;
	private int itKyogin;
	
	// Contadores
	private int vidasMikasa;
	private int municionRestante;
	private int kyojinesEliminados;
	private int vidasJefe;
	
	// Distancias para colisiones
	private double distObstaculos;
	private double distSuero;
	private double distRadar;
	
	// Booleanos para pantallas de juego
	private boolean juegoFinalizado;
	private boolean jefeFinal;
	private boolean mikasaGana;
	
	// Atributos de velocidad
	private double velocidadJefe;
	private double velocidadJefeAumentada;
	private double velocidadKyojines;
	private double velocidadKyojinesAumentada;
	
	//Carga de imágenes
	private Image imagenMika = Herramientas.cargarImagen("resources/mika.png");		
	private Image imagenMikaTitan = Herramientas.cargarImagen("resources/mikaTitan.png");
	private Image imagenFondo = Herramientas.cargarImagen("resources/fondo.jpg");
	private Image imagenFondoGameOver = Herramientas.cargarImagen("resources/fondo-gameover.jpg");
	private Image imagenFondoWin = Herramientas.cargarImagen("resources/win.jpg");
	private Image imagenKyojinNormal = Herramientas.cargarImagen("resources/kyojin.png");
	private Image imagenKyojinJefe = Herramientas.cargarImagen("resources/jefeFinal.png");
	
	//Carga de audios
	private Clip sonVictoria = Herramientas.cargarSonido("resources/victoria.wav");
	private Clip sonPerder = Herramientas.cargarSonido("resources/perder.wav");
	private Clip sonAmbiente = Herramientas.cargarSonido("resources/ambiente.wav");
		
	
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
		this.velocidadJefeAumentada=1.4;
		
		// Instanciamos obstáculos en posiciones fijas
		obstaculos = new Obstaculo[5];
		obstaculos[0] = new Obstaculo(600,150); //árbol
		obstaculos[1] = new Obstaculo(200,180); //casa
		obstaculos[2] = new Obstaculo(500,320); //casa
		obstaculos[3] = new Obstaculo(150,460); //árbol
		obstaculos[4] = new Obstaculo(700,500); //casa
	
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);	
		kyojines = new Kyojin[4];
		kyojinJefe = new Kyojin(0,0,this.velocidadJefe);
		proyectiles = new Proyectil[4];
		
		// Distancias
		distObstaculos = 80;
		distSuero = 20;
		distRadar = 150;
		
		// Iteradores
		itSuero = 0;
		itKyogin = 0;
				
		//Vidas&Municion
		vidasMikasa = 3;
		vidasJefe = 5;
		municionRestante = 4;
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
		//Pantalla de juego general
		if(this.juegoFinalizado == false && this.jefeFinal == false) {
			this.sonAmbiente.loop(10);
			this.juegoGeneral();
		}
		//Pantalla de jefe Final
		else if(this.juegoFinalizado == false && this.jefeFinal == true) {
			this.sonAmbiente.loop(10);
			this.juegoJefeFinal();
		}
		//Pantalla de juego finalizado 
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
		
		// Respawn de suero
		if(r.nextInt(650) < 1 && suero==null && !mikasa.getMikasaTitan()){
			double[] nuevaPos = this.generarPos();
			suero = new Suero(nuevaPos[0],nuevaPos[1],distSuero);
		}
		
		if(suero!=null) {
			suero.dibujarse(entorno);
		}
		
		//Colision Mika Suero
		if(suero!=null && mikasa.colisionSuero(suero, distSuero)){
			suero = null;
			mikasa.seVuelveTitan();			
		}
		
		//Mikassa 
		
		mikasa.mover(entorno);
		mikasaDispara();
		
		
		if( !mikasa.mikasaTitan){
			mikasa.dibujarse(entorno, this.imagenMika);	
		}		
		else{
			mikasa.dibujarse(entorno, this.imagenMikaTitan);
		}

		municion();
		
				
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
						Herramientas.play("resources/gritoMika.wav");
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
								Herramientas.play("resources/gritoKyo.wav");
								kyojinesEliminados++;

								break; // Ya muerto, no recorremos más proyectiles
							}	
						}
					}
				}
					
				if(kyojinesEliminados >= 6) {
					this.jefeFinal = true;
					this.municion = null;
					this.resetearProyectiles();
				}
				if(kyojinChocado == true && mikasa.mikasaTitan == false) {
					mikasa.mikasaTitan=false;					
				}

			}
			else{ // timer para respawn de kyogin
				itKyogin++;
				if(itKyogin==400) {
					double[] nuevaPos = this.generarPos();
					kyojines[i] = new Kyojin(nuevaPos[0],nuevaPos[1], this.velocidadKyojines);
					itKyogin = 0; 
				}
			}
		}
		
		entorno.cambiarFont("Arial", 32, Color.yellow);
	    entorno.escribirTexto("Vidas: " + this.vidasMikasa, 650, 60);
	    entorno.escribirTexto("Municion: " + this.municionRestante, 610, 85);
	    entorno.escribirTexto("Kyojines eliminados: " + this.kyojinesEliminados, 60,590);		    
		
		// ...
	}

	public void juegoJefeFinal(){
		
		//Mikasa
		fondo.dibujarse(this.entorno, imagenFondo, 2);
		mikasa.mover(entorno);
		mikasa.dibujarse(entorno, this.imagenMika);		
		mikasaDispara();
		
		
		municion();
		
		//KyojinJefe

		if(kyojinJefe != null) {			
			kyojinJefe.dibujarse(entorno, imagenKyojinJefe, 1);
			kyojinJefe.moverse();
			kyojinJefe.limiteDeCiudad(entorno);
			
			//Check Radar
			if(kyojinJefe.radar(mikasa, distRadar)==true){
				kyojinJefe.setVelocidad(this.velocidadJefeAumentada);
			}
			else {
				kyojinJefe.setVelocidad(this.velocidadJefe);
			}
			
			// Check Colisión con Mikasa
			boolean kyojinChocado = mikasa.colisionKyojin(kyojinJefe, 40);
			if(kyojinChocado == true ) {
				vidasMikasa--;
				if(this.vidasMikasa > 0){
					// Seteamos nueva posición para evitar perder las vidas de un tirón
					mikasa.setX(this.entorno.ancho()-kyojinJefe.getX());;
					mikasa.setY(this.entorno.alto()-kyojinJefe.getY());;
					Herramientas.play("resources/gritoMika.wav");
					this.generarPosJefe(kyojinJefe);
				}
				else {
					this.jefeFinal=false;
					this.juegoFinalizado=true;
					this.mikasaGana=false;
					this.sonPerder.start();
				}
			}
			
			// Proyectiles
			for(Proyectil proyectil : proyectiles) {
				if(proyectil !=null) {
					
					//check colisión con Kyojin
					boolean kyojinBaleado = proyectil.colisionKyojin(kyojinJefe, 40);
					if(kyojinBaleado) {
						vidasJefe--;
						this.resetearProyectiles();
						if(vidasJefe==1) {
							Herramientas.play("resources/gritoJefePerdio.wav");
							this.generarPosJefe(kyojinJefe);
						}
						else if(vidasJefe==0) {
							this.eliminarKyojin(kyojinJefe);
							this.jefeFinal=false;
							this.juegoFinalizado=true;
							this.mikasaGana=true;
							
							this.sonVictoria.start();
	
						}else {
							Herramientas.play("resources/gritoKyo.wav");
							this.generarPosJefe(kyojinJefe);
						}
						
						break; // Ya muerto, no recorremos más proyectiles
					}
				}
			}
		}
		
		//Dibujamos proyectiles
		for(int i=0;i<proyectiles.length;i++){
			if(proyectiles[i]!=null){
				proyectiles[i].dibujarse(entorno);
				proyectiles[i].mover();
				if(proyectiles[i].limiteDeCiudad(entorno)){
					this.eliminarProyectil(proyectiles[i]);
				}
			}
		}
		
		// Carteles de vidas
		entorno.cambiarFont("Arial", 32, Color.yellow);
	    entorno.escribirTexto("Vidas: " + this.vidasMikasa, 650, 60);
	    entorno.escribirTexto("Municion: " + this.municionRestante, 610, 85);
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
				this.municionRestante = 4;
				this.vidasJefe=5;
				this.juegoFinalizado=false;
				this.sonPerder.stop();
				this.sonPerder.setMicrosecondPosition(0); // Reset para que salga desde seg 0 el sonido
				
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
				this.municionRestante = 4;
				this.juegoFinalizado=false;
				this.mikasaGana=false;
				kyojinJefe = new Kyojin(0,0, this.velocidadJefe);
				
				this.generarPosJefe(kyojinJefe);
				this.sonVictoria.stop();
				this.sonVictoria.setMicrosecondPosition(0);
			}	
		}		
	}
	
	public void resetearSpawns() {		
		mikasa = null;
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);
		mikasa.dibujarse(entorno, imagenMika);
		this.resetearKyojines();
		this.resetearProyectiles();
	}
	
	public void eliminarProyectil(Proyectil proyectilLanzado) {
		for( int i = 0; i< proyectiles.length; i++) {
			if ( proyectiles[i]!=null && proyectiles[i].equals(proyectilLanzado)) {		
				proyectiles[i]=null;	
			}
		}
	}
	
	public void resetearProyectiles() {
		//Eliminar proyectiles existentes
		for( int i = 0; i< proyectiles.length; i++) {
			if(proyectiles[i]!=null){
				eliminarProyectil(proyectiles[i]);
			}
		}
	}
	
	public void resetearKyojines() {
		//Eliminar Kyojines existentes
		for( int i = 0; i< kyojines.length; i++) {
			if(kyojines[i]!=null){
				eliminarKyojin(kyojines[i]);
			}	
		}
		//Creo cuatro nuevos
		for( int i = 0; i< kyojines.length; i++) {
			double[] nuevaPos = this.generarPos();
			kyojines[i] = new Kyojin(nuevaPos[0],nuevaPos[1], this.velocidadKyojines);
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
		// Generamos posición para kyojin o suero
		double x=0;
		double y=0;
		boolean posOk = false;
		
		//Evitamos solapamiento en el respawn
		while(posOk==false){
			// Se genera valor aleatorio de x,y dentro de los márgenes del entorno
			x = r.nextInt(entorno.ancho());
			y = r.nextInt(entorno.alto());
			// Se comprobará que no coincida con pos de obstáculos o mikasa
			for(int j=0;j<obstaculos.length;j++) {
				if(obstaculos[j].coincidePos(x, y, distObstaculos)==false && mikasa.coincidePos(x, y, mikasa.getDistancia())== false){
					posOk = true;
				}
				else {
					posOk = false;
					break; // 
				}			
			}
			// Si kyogines salen juntos se separan por colisión.
		}
		return new double[]{x,y};	
	}
	
	public void generarPosJefe(Kyojin jefe) {
		// Al usarse solamente para Jefe podemos hacerlo void
		double x=0;
		double y=0;
		boolean posOk = false;
		
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
	
	public int lugarProyectil(Proyectil[] proyectiles){
		for (int i=0; i<proyectiles.length;i++){
			if(proyectiles[i]==null){
				return i;
			}
		}
		return -1;
	}
	
	public void mikasaDispara() {
		if(municionRestante>=1){
			if(mikasa.disparar(entorno,this.proyectiles, lugarProyectil(proyectiles))) {
				municionRestante--;
			}
		}
	}
	
	public void municion() {
		//Respawn de munición
		if(r.nextInt(250) < 1 && municion==null){
			double[] nuevaPos = this.generarPos();
			municion = new Municion(nuevaPos[0],nuevaPos[1],distSuero);
		}
		
		if(municion!=null) {
			municion.dibujarse(entorno);
		}

		//Colision Mika Municion		
		if(municion!=null && mikasa.colisionMunicion(municion, distSuero)){
			municion = null;
			municionRestante=municionRestante+2;			
		}
	}

	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
