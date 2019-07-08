package com.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Food {

	private int x, y; // coordinates on the canvas
	private Color color; // color of the food

	/**
	 * Constructor: create and initialize the food
	 * 
	 * @param snake: used to avoid initializing the food under the snake.
	 */
	public Food(Snake snake) {
		init(snake.getBody());
	}

	/**
	 * initialize the food
	 * 
	 * @param body: The body ArrayList extracted from the snake object
	 */
	public void init(ArrayList<SnakeBody> body) {
		randomCoordinates(body);

		color = randomColor();
	}

	/**
	 * @return a random color between green, red, blue and orange (very rare)
	 */
	private Color randomColor() {
		Color c;
		double random = Math.random();

		if (random < 0.30)
			c = Color.GREEN;
		else if (random < 0.60)
			c = Color.RED;
		else if (random < 0.95)
			c = Color.BLUE;
		else
			c = Color.ORANGE;

		return c;
	}

	/**
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * generate the random coordinates
	 * 
	 * @param body: The body ArrayList extracted from the snake object. It is used
	 *              to avoid putting the piece of food under the snake
	 */
	private void randomCoordinates(ArrayList<SnakeBody> body) {
		boolean same = true;
		int x = -1, y = -1;

		while (same) {
			x = ((int) (GameManager.GRID_WIDTH * Math.random())) * GameManager.CELL_SIZE;
			y = ((int) (GameManager.GRID_HEIGHT * Math.random())) * GameManager.CELL_SIZE;

			int i = 0;
			same = false;
			while (!same && i < body.size()) {
				same = (body.get(i).getX() == x && body.get(i).getY() == y);
				i++;
			}
		}

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
	 * Draw the piece of food
	 * 
	 * @param g Graphics used to draw on the Canvas
	 */
	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, GameManager.CELL_SIZE, GameManager.CELL_SIZE);
	}
}
