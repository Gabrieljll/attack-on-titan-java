package juego;
import java.awt.*;

import entorno.Entorno;
import entorno.Herramientas;

public class Mikasa {
	//Variables de instancia
	double posX;
	double posY;
	double angulo;
	Image img1;
	Image img2;
	int vida;
	Mikasa(int x, int y){
		this.posX = x;
		this.posY = y;
		this.angulo = angulo;
	
		this.vida = 3;
		img1 = Herramientas.cargarImagen("resources/mikassa.png");
		
	}
	public void dibujarse(Entorno entorno) {

		entorno.dibujarTriangulo(this.posX, this.posY, 50, 30, this.angulo, Color.blue);
		entorno.dibujarImagen(img1, posX, posY, angulo,0.3);
	}
	public void mover(Entorno entorno) 
	{
		if (entorno.estaPresionada(entorno.TECLA_ARRIBA)){
			this.posX += Math.cos(this.angulo)*2;
			this.posY += Math.sin(this.angulo)*2;
		}
		if (entorno.estaPresionada(entorno.TECLA_DERECHA))
			this.angulo = this.angulo + Herramientas.radianes(1);
			
		if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA))
			this.angulo = this.angulo + Herramientas.radianes(-1);
		
		if (entorno.estaPresionada(entorno.TECLA_ABAJO)){
			this.posX -= Math.cos(this.angulo)*1.1;
			this.posY -= Math.sin(this.angulo)*1.1;
		}
	}
	
	
	
	public void disparar(Entorno e, Proyectil[] proyectiles ) {
		
		int index = lugarProyectil(proyectiles);
		
		if(e.sePresiono(e.TECLA_ESPACIO) && index!=-1){
				proyectiles[index] =  new Proyectil(this.getPosX(), this.getPosY(),this.angulo, 30);
		}

	}

	
	//método para evitar que la minita huya de la ciudad
	public void limiteDeCiudad(Entorno entorno) {
		if (this.getPosX() >= entorno.ancho() -50 ) {
			this.angulo = this.angulo -90;
		}
		if(this.getPosX() <= 50) {
			this.angulo = this.angulo + 90;
		}
		if(this.getPosY() >= entorno.alto() -50) {
			this.angulo = this.angulo -90;
		}
		if(this.getPosY() <= 10) {
			this.angulo= this.angulo +90;
		}
		
	}
	
	
	
	
	
	

	
	
	public double getPosX() {
		return this.posX;
	}
	public void setPosX(double posX) {
		this.posX = posX;
	}
	public double getPosY() {
		return this.posY;
	}
	public void setPosY(double posY) {
		this.posY = posY;
	}
	
	public int lugarProyectil(Proyectil[] proyectiles){
		for (int i=0; i<proyectiles.length;i++){
			if(proyectiles[i]==null){
				return i;
			}
		}
		return -1;
	}

}
