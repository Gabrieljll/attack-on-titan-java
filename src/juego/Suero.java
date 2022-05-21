package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Suero {
	private double x;
	private double y;
	private double dist;
	private Image imagenSuero;
	
	
	public Suero(double x, double y) {
		this.x = x;
		this.y = y;
		this.dist = 20;
		this.imagenSuero = Herramientas.cargarImagen("resources/suero.png");
	}
	
	public void dibujarse(Entorno entorno) {
		//entorno.dibujarCirculo(x, y, dist, Color.RED);
		entorno.dibujarImagen(imagenSuero, this.x, this.y, 0, 0.06);
	}

	public double getX() {
		return this.x;
	}
	public double getY() {
		return this.y;
	}
	
}
