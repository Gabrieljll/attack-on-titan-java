package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondo {
	private int[] posicion;	
	//Methods
	
	public void dibujarse(Entorno entorno, Image imagenFondo, double escala){
		entorno.dibujarImagen(imagenFondo, entorno.ancho()/2, entorno.alto()/2, 0, escala);
	}
}
