package juego;
import java.awt.*;
import entorno.Entorno;
import entorno.Herramientas;

public class Obstaculo {
  //variables de instancia
  private double posicionX;
  private double posicionY;
  private double ancho;
  private double alto;
  private double angulo;
  private Image casa;
  private Image casa2;
  private Image arbol;
  
  public Obstaculo(int x, int y){
    this.posicionX  = x;
    this.posicionY = y;
    this.alto = 30;
    this.alto= 20;
    this.casa = Herramientas.cargarImagen("resources/casa.png");
    this.casa2 = Herramientas.cargarImagen("resources/casa2.png");
    this.arbol = Herramientas.cargarImagen("resources/arbol.png");
 
  }
  public void dibCasa(Entorno e) {
    //e.dibujarImagen(image, posicionX, posicionY, angulo, escala);
    e.dibujarImagen(casa, posicionX, posicionY, angulo, 0.3);
    
  }
  public void dibCasa2(Entorno e) {
    e.dibujarImagen(casa2, posicionX, posicionY, angulo, 0.3);
  }
  public void dibArbol(Entorno e) {
    e.dibujarImagen(arbol, posicionX, posicionY, angulo, 0.1);
  }

}
