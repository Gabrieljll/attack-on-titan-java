package juego;
import java.awt.*;
import java.util.Random;

import entorno.Entorno;
public class Kyojin {
	//Variables de instancia
	public double posX;
	public double posY;
	public double ancho;
	public double alto;
	
	public double angulo;
	Image img;
	Random r =  new Random(20);
	
	//Metodo constructor
	Kyojin(int x, int y, int ancho, int alto){
		this.posX = x;
		this.posY = y;
		this.ancho = ancho;
		this.alto = alto;
	}
	
	public void dibujarse(Entorno entorno) {
		//rectangulo(x,y,ancho, alto,angulo, color)
		entorno.dibujarRectangulo(this.posX, this.posY, 20, 60, this.angulo, Color.GREEN);
	}
	
	public void moverse() {
		this.posX += Math.cos(this.angulo);
		this.posY += Math.sin(this.angulo);
	}
	
	public void limiteDeCiudad(Entorno entorno) {
		if (this.getPosX() >= entorno.ancho() || this.getPosY() >= entorno.alto()) {
			this.angulo = this.angulo -90;
		}
		if(this.getPosX() <= 1 || this.getPosY() <= 1) {
			this.angulo = this.angulo + 90;
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
