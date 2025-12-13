package com.gcstudios.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gcstudios.main.Game;
import com.gcstudios.world.Camera;
import com.gcstudios.world.World;

public class BulletShoot extends Entity {
	
	private double dx;
	private double dy;
	private double spd = 2.5;
	
	private int life = 60,curLife = 0;

	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, sprite);		
		this.dx = dx;
		this.dy = dy;
	}
	
	
	public void tick() {
		double nextX = x + dx * spd;
		double nextY = y + dy * spd;

		// 🔥 COLISÃO COM MAPA
		if(!World.isFree((int)nextX, (int)nextY)) {
			Game.bullets.remove(this);
			return;
		}

		x = nextX;
		y = nextY;

		curLife++;
		if(curLife >= life) {
			Game.bullets.remove(this);
		}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, 3, 3);
	}
	
}
