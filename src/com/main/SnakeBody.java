package com.main;

import java.awt.Color;
import java.awt.Graphics;

public class SnakeBody{

	private int x, y;
	
	public SnakeBody(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void draw(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(x, y, GameManager.CELL_SIZE, GameManager.CELL_SIZE);
		
		g.setColor(Color.BLACK);
		g.drawRect(x, y, GameManager.CELL_SIZE, GameManager.CELL_SIZE);
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	public void move(byte xDir, byte yDir) {
		x += xDir*GameManager.CELL_SIZE;
		y += yDir*GameManager.CELL_SIZE;
	}
	
}
