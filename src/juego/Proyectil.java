package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Proyectil {
	//Variables de instancia
	double posX;
	double posY;
	double diametro;
	double angulo;
	double radio;
	double velocidad;
	private Image img;


	
	
	Proyectil(double x, double y, double angulo, double radio){
		this.posX = x;
		this.posY = y;
		this.angulo = angulo;
		this.radio = radio;
		this.velocidad = 5;
		img = Herramientas.cargarImagen("resources/proyectil.png");
		
	}
	public void dibujarse(Entorno entorno) {
		entorno.dibujarCirculo(this.posX,this.posY, 10, Color.orange);
		entorno.dibujarImagen(img, posX, posY, angulo,0.15);
	}
	public void mover(){
		 this.posX = this.posX + Math.cos(this.angulo)*velocidad;
		 this.posY = this.posY + Math.sin(this.angulo)*velocidad;
//			this.posX = this.posX + ( Math.cos(this.angulo)*velocidad);
//			this.posY = this.posY + ( Math.sin(this.angulo)*velocidad);
		
	}
	public boolean limiteDeCiudad(Entorno entorno) {
		if (this.posX >= entorno.ancho() || this.posY >= entorno.alto()) {
			return true;
		}
		if(this.posX <= 1 || this.posY <= 1) {
			return true;
		}
		return false;
	}
	
	

}
