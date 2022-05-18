package juego;
import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Mikasa {
	//Variables de instancia
	private double x;
	private double y;
    double angulo;
	private Image img1;
	private Image img2;
	private Image img3;
	private int vida;
	double dist;
	//private boolean chocaObstaculoDelante;
	//private boolean chocaObstaculoAtras;
	public boolean mikasaKyiojin;
	
	Mikasa(int x, int y){
		this.x = x;
		this.y = y;
		this.angulo = angulo;
//		this.chocaObstaculoDelante = false;
//		this.chocaObstaculoAtras = false;
		this.mikasaKyiojin = false;
		this.dist = 40;
		this.vida = 3;
		img1 = Herramientas.cargarImagen("resources/mikaDer.png");		
		img2 = Herramientas.cargarImagen("resources/mikaIzq.png");
		img3 = Herramientas.cargarImagen("resources/mikaTitanDer.png");
		
	}
	public void dibujarse(Entorno entorno, Image img) {

		//entorno.dibujarTriangulo(this.x, this.y, 50, 30, this.angulo, Color.blue);
		entorno.dibujarImagen(img, x, y, angulo,0.1);
	}
	public void mover(Entorno entorno) 
	{
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA) ){
			this.x += Math.cos(this.angulo)*2;
			this.y += Math.sin(this.angulo)*2;
		}
		
		if (entorno.estaPresionada(entorno.TECLA_DERECHA)){			
			this.angulo = this.angulo + Herramientas.radianes(1);
			//this.chocaObstaculoDelante=false;
		}
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA)){			
			this.angulo = this.angulo + Herramientas.radianes(-1);
			//this.chocaObstaculoDelante=false;
		}
		
		if (entorno.estaPresionada(entorno.TECLA_ABAJO)){
			this.x -= Math.cos(this.angulo)*1.1;
			this.y -= Math.sin(this.angulo)*1.1;	
		}
	}
	
	public void disparar(Entorno e, Proyectil[] proyectiles ) {
		
		int index = lugarProyectil(proyectiles);
		
		if(e.sePresiono(e.TECLA_ESPACIO) && index!=-1){
				proyectiles[index] =  new Proyectil(this.getPosX(), this.getPosY(),this.angulo, 30);
		}

	}
	
	public void mataKyojin(Proyectil[] proyectiles, Kyojin[] kyojines) {
		for(int i = 0; i <= proyectiles.length && i <= kyojines.length ; i++) {
			if(proyectiles[i] != null && kyojines[i] != null && proyectiles[i].chocaKyojin(kyojines, dist)) {
				kyojines[i] = null;
				proyectiles[i] = null;
			}
		}
	}

	
	//m�todo para evitar que la minita huya de la ciudad
	public void limiteDeCiudad(Entorno entorno) {
		if (this.getPosX() >= entorno.ancho() || this.getPosY() >= entorno.alto()) {
			this.angulo = this.angulo - 90;
		}
		if (this.getPosX() <= 0 || this.getPosY() <= 0) {
			this.angulo = this.angulo + 90;
		}
		
	}
	
	public Obstaculo chocaObtaculos(Obstaculo[] obstaculos, double dist) {
		for(int i =0; i < obstaculos.length; i++) {
			if(((this.getPosX() - obstaculos[i].getX()) * (this.getPosX() - obstaculos[i].getX()) + 
			    (this.getPosY() - obstaculos[i].getY()) * (this.getPosY() - obstaculos[i].getY()) < dist*dist)) {
				 return obstaculos[i];
			}
		}
		return null;	
	}
	
	public boolean chocaSuero(Suero suero, double dist) {
		if(((this.getPosX() - suero.getX()) * (this.getPosX() - suero.getX()) + 
		    (this.getPosY() - suero.getY()) * (this.getPosY() - suero.getY()) < dist*dist)) {
			return true;
		}
	return false;
	}
	
	public void seVuelveTitan(){
		//entorno.dibujarImagen(img3, getPosX(), getPosX(), angulo, 0.1);
		this.mikasaKyiojin = true;	
	}
	public boolean chocaKyojin(Kyojin[] kyojines, double dist) {
		for(int i =0; i < kyojines.length; i++) {
			if(((this.getPosX() - kyojines[i].getPosX()) * (this.getPosX() - kyojines[i].getPosX()) + 
	            (this.getPosY() - kyojines[i].getPosY()) * (this.getPosY() - kyojines[i].getPosY()) < dist*dist  )) {
				return true;
			}
		}
		return false;	
	}
	
	
	
//	public void setChocaObstaculoDelante(boolean chocaObstaculo) {
//		this.chocaObstaculoDelante = chocaObstaculo;
//	}
//	public void setChocaObstaculoAtras(boolean chocaObstaculo) {
//		this.chocaObstaculoAtras = chocaObstaculo;
//	}
	public double getPosX() {
		return this.x;
	}
	public void setPosX(double posX) {
		this.x = posX;
	}
	public double getPosY() {
		return this.y;
	}
	public void setPosY(double posY) {
		this.y = posY;
	}
	
	public int lugarProyectil(Proyectil[] proyectiles){
		for (int i=0; i<proyectiles.length;i++){
			if(proyectiles[i]==null){
				return i;
			}
		}
		return -1;
	}

}