package com.main;

import java.awt.Color;
import java.awt.Graphics;

/**
 * class contains a body part of the snake
 * 
 * @author Zayed
 *
 */
public class SnakeBody {

	private int x, y; // coordinates on the canvas

	/**
	 * Constructor: create and initialize the SnakeBody object
	 * 
	 * @param x - x position
	 * @param y - y position
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
	 * @param g        - Graphics used to draw on the Canvas
	 * @param color    - color of the snake body part
	 * @param cellSize - size of a grid cell in pixels
	 */
	public void draw(Graphics g, int cellSize) {
		g.setColor(Color.BLACK);
		g.fillRect(x, y, cellSize, cellSize);

		g.setColor(Color.WHITE);
		g.drawRect(x, y, cellSize, cellSize);
	}

}
