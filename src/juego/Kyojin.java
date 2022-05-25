package juego;
import java.awt.*;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
public class Kyojin {
	//Variables de instancia
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double angulo;
	private Image imgKyojinJefe;
	private Image img2;
	private Random r =  new Random(20);
	private double velocidad;

	
	//Metodo constructor
	Kyojin(double x, double y, double ancho, double alto){
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = 0.8;
		img2 = Herramientas.cargarImagen("resources/kyojinIzq.png");
	}
	
	public void dibujarse(Entorno entorno, Image imagenKyojin, double escala) {
		//rectangulo(x,y,ancho, alto,angulo, color)
		//entorno.dibujarRectangulo(this.posX, this.posY, 20, 60, this.angulo, Color.GREEN);
		entorno.dibujarImagen(imagenKyojin, x, y, angulo,escala);
	}
	
	public void moverse() {
		this.x += Math.cos(this.angulo)*velocidad;
		this.y += Math.sin(this.angulo)*velocidad;
	}
	
	public void limiteDeCiudad(Entorno entorno) {
		if (this.getX() >= entorno.ancho() || this.getY() >= entorno.alto()) {
			this.angulo = this.angulo -90;
		}
		if(this.getX() <= 0 || this.getY() <= 0) {
			this.angulo = this.angulo +90;
		}
		
	}
	public Obstaculo colisionObstaculos(Obstaculo[] obstaculos, double dist) {
		for(int i =0; i < obstaculos.length; i++) {
			if(((this.getX() - obstaculos[i].getX()) * (this.getX() - obstaculos[i].getX()) + 
			    (this.getY() - obstaculos[i].getY()) * (this.getY() - obstaculos[i].getY()) < dist*dist)) {
				 return obstaculos[i];
			}
		}
		return null;	
	}
	
	public boolean colisionKyojin(Kyojin kyojin, int dist) {
		if(((this.getX() - kyojin.getX()) * (this.getX() - kyojin.getX()) + 
			    (this.getY() - kyojin.getY()) * (this.getY() - kyojin.getY()) < dist*dist)) {
				 return true;
			}
		return false;
	} 
	
	public void radar(Mikasa mikasa, double dist){
	    if((this.getX() - mikasa.getX()) < dist && (this.getY() - mikasa.getY()) < dist){
	    	this.angulo = Math.atan2( mikasa.getY() - this.getY() , mikasa.getX() - this.getX() );
	    }else {
	    	this.moverse();
	    }
	}
	
	public void esquivarObstaculo(Entorno entorno, Obstaculo obstaculoChocado) {
			if(obstaculoChocado.getX() < this.getX() && obstaculoChocado.getY() < this.getY()) {
				this.setX(this.getX()+2);
				this.setY(this.getY()+2);
			}else {
				this.setX(this.getX()-2);
				this.setY(this.getY()-2);
			}		
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
		public double getAngulo() {
			return angulo;
		}
		public void setAngulo(double angulo) {
			this.angulo = angulo;
		}


}
