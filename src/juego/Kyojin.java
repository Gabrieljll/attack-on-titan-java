package juego;
import java.awt.*;
import java.util.Random;

import entorno.Entorno;
import entorno.Herramientas;
public class Kyojin {
	//Variables de instancia
	private double posX;
	private double posY;
	private double ancho;
	private double alto;
	private double angulo;
	private Image img;
	private Image img2;
	private Random r =  new Random(20);
	private double velocidad;

	
	//Metodo constructor
	Kyojin(double x, double y, double ancho, double alto){
		this.posX = x;
		this.posY = y;
		this.ancho = ancho;
		this.alto = alto;
		this.velocidad = 0.4;
		img2 = Herramientas.cargarImagen("resources/kyojinIzq.png");
	}
	
	public void dibujarse(Entorno entorno) {
		//rectangulo(x,y,ancho, alto,angulo, color)
		//entorno.dibujarRectangulo(this.posX, this.posY, 20, 60, this.angulo, Color.GREEN);
		entorno.dibujarImagen(img2, posX, posY, angulo,0.2);
	}
	
	public void moverse() {
		this.posX += Math.cos(this.angulo)*velocidad;
		this.posY += Math.sin(this.angulo)*velocidad;
	}
	
	public void limiteDeCiudad(Entorno entorno) {
		if (this.getPosX() >= entorno.ancho() || this.getPosY() >= entorno.alto()) {
			this.angulo = this.angulo -90;
		}
		if(this.getPosX() <= 0 || this.getPosY() <= 0) {
			this.angulo = this.angulo +90;
		}
		
	}
	public Obstaculo verificarColisionObstaculos(Obstaculo[] obstaculos, double dist) {
		for(int i =0; i < obstaculos.length; i++) {
			if(((this.getPosX() - obstaculos[i].getX()) * (this.getPosX() - obstaculos[i].getX()) + 
			    (this.getPosY() - obstaculos[i].getY()) * (this.getPosY() - obstaculos[i].getY()) < dist*dist)) {
				 return obstaculos[i];
			}
		}
		return null;	
	}
	
	public void esquivarObstaculo(Entorno entorno, Obstaculo obstaculoChocado) {
			if(obstaculoChocado.getX() < this.getPosX() && obstaculoChocado.getY() < this.getPosY()) {
				this.setPosX(this.getPosX()+2);
				this.setPosY(this.getPosY()+2);
			}else {
				this.setPosX(this.getPosX()-2);
				this.setPosY(this.getPosY()-2);
			}		
	}
		
		public double getPosX() {
			return this.posX;
		}
		public void setPosX(double posX) {
			this.posX = posX;
		}
		public double getPosY() {
			return this.posY;
		}
		public void setPosY(double posY) {
			this.posY = posY;
		}
		public double getAngulo() {
			return angulo;
		}
		public void setAngulo(double angulo) {
			this.angulo = angulo;
		}

}
