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
	double dist;
	public boolean mikasaTitan; // Para saber si se convierte en kyojin o no
	
	Mikasa(int x, int y){
		this.x = x;
		this.y = y;
		this.angulo = angulo;
//		this.chocaObstaculoDelante = false;
//		this.chocaObstaculoAtras = false;
		this.mikasaTitan = false;
		this.dist = 100;
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
		
		//Generamos flags para mantenernos dentro de los l�mites de la ciudad
		boolean limiteX=false;
		boolean limiteY=false;
		int limitPixel = 10;
		
		if(this.x >= entorno.ancho()-limitPixel || this.x <= limitPixel){
			limiteX = true;
		}
		
		if(this.y >= entorno.alto()-limitPixel || this.y <= limitPixel){ 
			limiteY = true;
		}
		
		
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA)){
			
			if(limiteY==false){
				this.y += Math.sin(this.angulo)*2;
							}
			else { //Identificamos cu�l es el l�mite en el que estamos y reasignamos la pos anterior para que el siguiente checkeo no estemos en el l�mite
				if(y<=limitPixel)
					this.y = limitPixel+1;
				else
					this.y = entorno.alto()-(limitPixel+1);
			}
			if(limiteX==false){
				this.x += Math.cos(this.angulo)*2;
			}else {

				if(x<=limitPixel)
					this.x = limitPixel+1;
				else
					this.x = entorno.ancho()-(limitPixel+1);
			}
			
			
			
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
			if(limiteX==false){
				this.x -= Math.cos(this.angulo)*1.1;
			}else {
				this.x += Math.cos(this.angulo)*1.1;
			}
			
			if(limiteY==false){
				this.y -= Math.sin(this.angulo)*1.1;
			}else {
				this.y += Math.sin(this.angulo)*1.1;
			}
			
			
				
		}
	}
	
	public void disparar(Entorno e, Proyectil[] proyectiles ) {
		
		int index = lugarProyectil(proyectiles);
		
		if(e.sePresiono(e.TECLA_ESPACIO) && index!=-1){
				proyectiles[index] =  new Proyectil(this.getX(), this.getY(),this.angulo, 30);
		}

	}
		
	//Respetar l�mites de la ciudad
	public void limiteDeCiudad(Entorno entorno) {
		if (this.getX() >= entorno.ancho() || this.getY() >= entorno.alto()) {
			this.angulo = this.angulo - 90;
		}
		if (this.getX() <= 0 || this.getY() <= 0) {
			this.angulo = this.angulo + 90;
		}
		
	}

	public boolean colisionKyogin(Kyojin kyojin, double dist) {
		
			if(((this.getX() - kyojin.getX()) * (this.getX() - kyojin.getX()) + 
			    (this.getY() - kyojin.getY()) * (this.getY() - kyojin.getY()) < dist*dist)) {
				 return true;
			
			}
		return false;	
	}
	
	
	public Obstaculo colisionObstaculo(Obstaculo[] obstaculos, double dist) {
		for(int i =0; i < obstaculos.length; i++) {
			if(((this.getX() - obstaculos[i].getX()) * (this.getX() - obstaculos[i].getX()) + 
			    (this.getY() - obstaculos[i].getY()) * (this.getY() - obstaculos[i].getY()) < dist*dist)) {
				 return obstaculos[i];
			}
		}
		return null;
	}
	
	public void esquivarObstaculo(Entorno entorno, Obstaculo obstaculoChocado) {
		if(entorno.estaPresionada(entorno.TECLA_ARRIBA)) {
			if(obstaculoChocado.getX() < this.getX() && obstaculoChocado.getY() < this.getY()) {
				this.setX(this.getX()+2);
				this.setY(this.getY()+2);
			}else {
				this.setX(this.getX()-2);
				this.setY(this.getY()-2);
			}
		}
		if(entorno.estaPresionada(entorno.TECLA_ABAJO)) {
			if(obstaculoChocado.getX() > this.getX() && obstaculoChocado.getY() > this.getY()) {
				this.setX(this.getX()-2);
				this.setY(this.getY()-2);
			}else {
				this.setX(this.getX()+2);
				this.setY(this.getY()+2);
			}
		}		
	}
	
	public boolean chocaSuero(Suero suero, double dist) {
		if(((this.getX() - suero.getX()) * (this.getX() - suero.getX()) + 
		    (this.getY() - suero.getY()) * (this.getY() - suero.getY()) < dist*dist)) {
			return true;
		}
	return false;
	}
	
	public void seVuelveTitan(){
		this.mikasaTitan = true;	
	}
	public void seVuelveNormal() {
		this.mikasaTitan = false;
	}
	public boolean colisionKyojin(Kyojin[] kyojines, double dist) {
		for(int i =0; i < kyojines.length; i++) {
			if(((this.getX() - kyojines[i].getX()) * (this.getX() - kyojines[i].getX()) + 
	            (this.getY() - kyojines[i].getY()) * (this.getY() - kyojines[i].getY()) < dist*dist  )) {
				return true;
			}
		}
		return false;	
	}
	
	  public boolean coincidePos(double x, double y, double dist){
		  if(((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) < dist*dist)) {
			  return true;
		  }
		  return false;
	  }
	  
	  public boolean getMikasaTitan(){
		  return this.mikasaTitan;
	  }
	  
	  public double getDistancia() {
		  return this.dist;
	  }
	  public double getX() {
		  return this.x;
	  }
	  
	public void setX(double posX) {
		this.x = posX;
	}
	public double getY() {
		return this.y;
	}
	public void setY(double posY) {
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
