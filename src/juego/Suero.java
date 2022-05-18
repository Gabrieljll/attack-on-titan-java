package juego;

import java.awt.Color;

import entorno.Entorno;

public class Suero {
	private double x;
	private double y;
	private double dist;
	
	public Suero(double x, double y) {
		this.x = x;
		this.y = y;
		this.dist = 20;
	}
	
	public void dibujarse(Entorno entorno) {
		
		entorno.dibujarCirculo(x, y, dist, Color.RED);
		
		//entorno.dibujarImagen(img, x, y, 0,0.3);
		
	}

	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	
}
