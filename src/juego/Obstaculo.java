package juego;
import java.awt.*;
import entorno.Entorno;
import entorno.Herramientas;

public class Obstaculo {
  //variables de instancia
  private double x;
  private double y;
  private double ancho;
  private double alto;
  private double angulo;
  private Image casa;
  private Image casa2;
  private Image arbol;
  
  public Obstaculo(int x, int y){
    this.x  = x;
    this.y = y;
    this.alto = 30;
    this.ancho= 20;
    this.casa = Herramientas.cargarImagen("resources/casa.png");
    this.casa2 = Herramientas.cargarImagen("resources/casa2.png");
    this.arbol = Herramientas.cargarImagen("resources/arbol.png");
 
  }
  public void dibCasa(Entorno e) {
    //e.dibujarImagen(image, posicionX, posicionY, angulo, escala);
    e.dibujarImagen(casa, x, y, angulo, 0.3);
    
  }
  
  public void dibCasa2(Entorno e) {
    e.dibujarImagen(casa2, x, y, angulo, 0.3);
  }
  
  public void dibArbol(Entorno e) {
    e.dibujarImagen(arbol, x, y, angulo, 0.1);
  }
  
  public boolean coincidePos(double x, double y, double dist){
	  if(((this.x - x) * (this.x - x) + (this.y - y) * (this.y - y) < dist*dist)) {
		  return true;
	  }
	  return false;
  }
  
  public double getX() {
	return x;
  }
  public double getY() {
	return y;
  }
  
}
