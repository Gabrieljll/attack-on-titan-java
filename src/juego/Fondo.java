package juego;

import java.awt.Image;

import entorno.Entorno;

public class Fondo {
	
	//Methods
	
	public void dibujarse(Entorno entorno, Image imagenFondo, double escala){
		entorno.dibujarImagen(imagenFondo, entorno.ancho()/2, entorno.alto()/2, 0, escala);
	}
}
