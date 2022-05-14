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
	Random r =  new Random(20);
	
	//Metodo constructor
	Kyojin(int x, int y, int ancho, int alto){
		this.posX = x;
		this.posY = y;
		this.ancho = ancho;
		this.alto = alto;
		img = Herramientas.cargarImagen("resources/kyojin3.jpg");
	}
	
	public void dibujarse(Entorno entorno) {
		//rectangulo(x,y,ancho, alto,angulo, color)
		entorno.dibujarRectangulo(this.posX, this.posY, 20, 60, this.angulo, Color.GREEN);
		entorno.dibujarImagen(img, posX, posY, angulo,0.3);
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
