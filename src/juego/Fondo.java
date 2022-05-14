package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondo {
	private int[] posicion;
	Image imagen = Herramientas.cargarImagen("resources/pasto.jpg");
	//Methods
	
	public void dibujarse(Entorno entorno){
		entorno.dibujarImagen(imagen, 0, 0, 0,2);
	}
}
