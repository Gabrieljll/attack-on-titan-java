package juego;
import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Mikasa {
	//Variables de instancia
	private double x;
	private double y;
    double angulo;
	double dist;
	public boolean mikasaTitan; // Para saber si se convierte en kyojin o no
	
	Mikasa(int x, int y){
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.mikasaTitan = false;
		this.dist = 100;
	}
	
	public void dibujarse(Entorno entorno, Image img) {

		//entorno.dibujarTriangulo(this.x, this.y, 50, 30, this.angulo, Color.blue);
		entorno.dibujarImagen(img, x, y, angulo,0.1);
	}
	
	public void mover(Entorno entorno) 
	{
		
		//Generamos flags para mantenernos dentro de los limites de la ciudad
		boolean limiteX=false;
		boolean limiteY=false;
		int limitPixel = 10; // Evitamos que la imagen desaparezca en el borde
		if(this.x >= entorno.ancho()-limitPixel || this.x <= limitPixel){
			limiteX = true;
		}
		
		if(this.y >= entorno.alto()-limitPixel || this.y <= limitPixel){ 
			limiteY = true;
		}
		
		
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA)){
			
			//Else = se alcanzo limite
			if(limiteY==false){
				this.y += Math.sin(this.angulo)*2;
							}
			else { 
				//Cuando y=0 necesitamos sumar pixel a y
				if(y<=limitPixel)
					this.y = limitPixel+1;
				//Cuando y!=0 reasignamos el límite máximo en y restando el margen de pixel +1
				//El +1 evita que se quede "pegada al límite"
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
		

		//Caminar para atrás es más lento
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
	
	public void disparar(Entorno e, Proyectil[] proyectiles, int index ) {	
		// Espacio + índice con un valor entre 0 y 3 crea proyectil
		if(e.sePresiono(e.TECLA_ESPACIO) && index!=-1){
				proyectiles[index] =  new Proyectil(this.getX(), this.getY(),this.angulo, 30);
				Herramientas.play("resources/proyectil.wav");
		}
		

	}
		

	public boolean colisionKyojin(Kyojin kyojin, double dist) {
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
	


}
