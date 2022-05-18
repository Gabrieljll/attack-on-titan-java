package juego;


import java.awt.Image;
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
	Suero suero;
	//colision c;
	Random r = new Random();
	Fondo fondo;
	int vidas;
	double dist;
	double dist2;
	double dist3;
	Image img1 = Herramientas.cargarImagen("resources/mikaDer.png");		
	Image img2 = Herramientas.cargarImagen("resources/mikaTitanDer.png");
	// ...
	
	Juego()
	{
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
		dist = 80;
		dist2 = 20;
		dist3 = 40;
		
		fondo = new Fondo();
		mikasa = new Mikasa(entorno.ancho()/2,entorno.alto()/2);
		vidas=100;		
		proyectiles = new Proyectil[4];
		kyojines = new Kyojin[4];
		
		
		for(int i =0; i<kyojines.length;i++) {
			double[] nuevaPos = this.generarPos();
			kyojines[i] = new Kyojin(nuevaPos[0],nuevaPos[1],20,60);
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
		obstaculos[4].dibCasa2(entorno);
		
		if(r.nextInt(100) < 100 && suero==null){
			double[] nuevaPos = this.generarPos();
			suero = new Suero(nuevaPos[0],nuevaPos[1]);
		}
		
		if(suero!=null) {
			suero.dibujarse(entorno);
		}
		
		
		
		//Mikassa 
		
		mikasa.mover(entorno);
		
		if( !mikasa.mikasaKyojin){
			mikasa.dibujarse(entorno, this.img1);	
		}		
		else{
			mikasa.dibujarse(entorno, this.img2);
		}
		
		if(mikasa.chocaSuero(suero, dist2)){
			suero = null;
			mikasa.seVuelveTitan();			
		}
		
		
		mikasa.limiteDeCiudad(entorno);
		mikasa.disparar(entorno,this.proyectiles);
		
		
//		if(mikasa.chocaObtaculos(obstaculos, dist)) {
//			mikasa.setPosX(mikasa.getPosX()-2);
//			mikasa.setPosY(mikasa.getPosY()-2);
//		}

		
		//Mikasa Colision obstaculos
		Obstaculo obstaculoChocado = mikasa.verificarColisionObstaculos(obstaculos, dist);
		if(obstaculoChocado != null) {
			mikasa.esquivarObstaculo(entorno, obstaculoChocado);
		}

		//Mikasa Colision Kyojin
		Kyojin kyojinChocado= mikasa.verificarColisionKyojines(kyojines, dist);
		if(kyojinChocado != null && mikasa.mikasaKyojin) {
			this.kyojines=this.eliminarKyojin(kyojinChocado);
			mikasa.esquivarObstaculo(entorno, obstaculoChocado);
		}
		
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
		for(Kyojin kyojin : kyojines) {
			if(kyojin!=null){
				kyojin.dibujarse(entorno);
				kyojin.moverse();
				kyojin.limiteDeCiudad(entorno);
				Obstaculo obstaculoChocadoKyo = kyojin.verificarColisionObstaculos(obstaculos, dist); 
				if(obstaculoChocadoKyo != null) {
					kyojin.esquivarObstaculo(entorno, obstaculoChocadoKyo);
				}
//				if(!mikasa.mikasaKyiojin && mikasa.chocaKyojin(kyojines, dist3)) {
//					this.vidas --;
//				}
//				else if(mikasa.mikasaKyiojin == true && mikasa.chocaKyojin(kyojines, dist3)) {
//					kyojin = null;
//				}
			}
		}
		//mikasa.mataKyojin(proyectiles, kyojines);
		
		entorno.escribirTexto("Vidas: " + vidas, 700, 100);
		
		
		// ...
		

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
				if(obstaculos[j].coincidePos(x, y)==false && x!=mikasa.getPosX() && y!=mikasa.getPosY()){
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
	
	public Kyojin[] eliminarKyojin(Kyojin kyojinChocado) {
		Kyojin [] kyojinesRestantes = new Kyojin[kyojines.length-1];
		for( int i = 0; i< kyojines.length; i++) {
			if ( kyojines[i].equals(kyojinChocado)) {
				kyojines[i] = null;				
			}else {
				kyojinesRestantes[i]=kyojines[i];	
			}
		}
		return kyojinesRestantes;
	}
	
	
	@SuppressWarnings("unused")
	public static void main(String[] args)
	{
		Juego juego = new Juego();
	}
}
