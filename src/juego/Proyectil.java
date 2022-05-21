package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Proyectil {
	//Variables de instancia
	double x;
	double y;
	double diametro;
	double angulo;
	double radio;
	double velocidad;
	private Image img;


	
	
	Proyectil(double x, double y, double angulo, double radio){
		this.x = x;
		this.y = y;
		this.angulo = angulo;
		this.radio = radio;
		this.velocidad = 10;
		img = Herramientas.cargarImagen("resources/proyectil.png");
		
	}
	public void dibujarse(Entorno entorno) {
		entorno.dibujarCirculo(this.x,this.y, 10, Color.orange);
		entorno.dibujarImagen(img, x, y, angulo,0.08);
	}
	public void mover(){
		 this.x = this.x + Math.cos(this.angulo)*velocidad;
		 this.y = this.y + Math.sin(this.angulo)*velocidad;
	}
	public boolean limiteDeCiudad(Entorno entorno) {
		if (this.x >= entorno.ancho() || this.y >= entorno.alto()) {
			return true;
		}
		if(this.x <= 1 || this.y <= 1) {
			return true;
		}
		return false;
	}
	public Kyojin chocaKyojin(Kyojin[] kyojines, double dist) {
		for(int i =0; i < kyojines.length; i++) {
			if(((this.getX() - kyojines[i].getPosX()) * (this.getX() - kyojines[i].getPosX()) + 
			    (this.getY() - kyojines[i].getPosY()) * (this.getY() - kyojines[i].getPosY()) < dist*dist)) {
				 return kyojines[i];
			}
		}
		return null;	
	}
	public double getX() {
		return this.x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return this.y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getAngulo() {
		return angulo;
	}
	public void setAngulo(double angulo) {
		this.angulo = angulo;
	}
	
	

}
