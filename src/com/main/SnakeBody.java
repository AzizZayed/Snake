package com.main;

import java.awt.Color;
import java.awt.Graphics;

public class SnakeBody {

	private int x, y; // coordinates on the canvas

	/**
	 * Constructor: create and initialize the SnakeBody object
	 * 
	 * @param x: x position
	 * @param y: y position
	 */
	public SnakeBody(int x, int y) {
		this.x = x;
		this.y = y;
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

	/**
	 * Draw the part of the snakes body
	 * 
	 * @param g Graphics used to draw on the Canvas
	 */
	public void draw(Graphics g, Color color) {
		g.setColor(color);
		g.fillRect(x, y, GameManager.CELL_SIZE, GameManager.CELL_SIZE);

		g.setColor(Color.WHITE);
		g.drawRect(x, y, GameManager.CELL_SIZE, GameManager.CELL_SIZE);
	}

}
