package juego;

import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Fondo {
	private int[] posicion;
	private Image imagen = Herramientas.cargarImagen("resources/pasto.jpg");
	//Methods
	
	public void dibujarse(Entorno entorno){
		entorno.dibujarImagen(imagen, entorno.ancho()/2, entorno.alto()/2, 0,2);
	}
}
