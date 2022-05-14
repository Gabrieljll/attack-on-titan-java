package juego;

import java.awt.Color;

import entorno.Entorno;

public class Proyectil {
	//Variables de instancia
	double posx;
	double posy;
	double diametro;
	double angulo;
	double radio;
	double velocidad;
	Proyectil(double x, double y, double angulo, double radio){
		this.posx = x;
		this.posy = y;
		this.angulo = angulo;
		this.radio = radio;
		this.velocidad = 10;
	}
	public void dibujarse(Entorno entorno) {
		entorno.dibujarCirculo(this.posx,this.posy, 30, Color.orange);
	}
	public void mover(){
		 this.posx = this.posx + Math.cos(this.angulo)*velocidad;
		 this.posy = this.posy + Math.sin(this.angulo)*velocidad;
//			this.posx = this.posx + ( Math.cos(this.angulo)*velocidad);
//			this.posy = this.posy + ( Math.sin(this.angulo)*velocidad);
		
	}
	public boolean limiteDeCiudad(Entorno entorno) {
		if (this.posx >= entorno.ancho() || this.posy >= entorno.alto()) {
			return true;
		}
		if(this.posx <= 1 || this.posy <= 1) {
			return true;
		}
		return false;
	}
	
	

}
