package com.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * class for the food
 * 
 * @author Zayed
 *
 */
public class Food {

	private int x, y; // coordinates on the canvas
	private Color color; // color of the food

	/**
	 * Constructor: create and initialize the food
	 * 
	 * @param body       - The body ArrayList extracted from the snake object, used
	 *                   to avoid putting the piece of food under the snake
	 * @param gridWidth  - width of the canvas in grids
	 * @param gridHeight - height of the canvas in grids
	 * @param cellSize   - size of a grid cell in pixels
	 */
	public Food(ArrayList<SnakeBody> body, int gridWidth, int gridHeight, int cellSize) {

		boolean same = true;
		x = y = -1;

		while (same) {
			x = ((int) (gridWidth * Math.random())) * cellSize;
			y = ((int) (gridHeight * Math.random())) * cellSize;

			int i = 0;
			same = false;
			while (!same && i < body.size()) {
				same = (body.get(i).getX() == x && body.get(i).getY() == y);
				i++;
			}
		}

		color = randomColor();

	}

	/**
	 * get a random color
	 * 
	 * @return a random color
	 */
	private Color randomColor() {
		double random = Math.random();

		if (random < 0.30)
			return Color.GREEN;
		else if (random < 0.60)
			return Color.RED;
		else if (random < 0.95)
			return Color.BLUE;
		else
			return Color.ORANGE;
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
	 * Draw the piece of food
	 * 
	 * @param g        - Graphics used to draw on the Canvas
	 * @param cellSize - size of the gird cell in the canvas in pixels
	 */
	public void draw(Graphics g, int cellSize) {
		g.setColor(color);
		g.fillRect(x, y, cellSize, cellSize);
	}
}
