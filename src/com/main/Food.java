package com.main;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Food {

	private int x, y;
	private Color color;

	public Food(Snake snake) {
		init(snake.getBody());
	}

	private Color randomColor() {
		Color c;
		double random = Math.random();

		if (random < 0.30)
			c = Color.GREEN;
		else if (random < 0.60)
			c = Color.RED;
		else if (random < 0.96)
			c = Color.BLUE;
		else
			c = Color.ORANGE;

		return c;
	}

	private void randomCoordinates(ArrayList<SnakeBody> body) {
		boolean same = true;
		int x = -1, y = -1;

		while (same) {
			x = ((int) (Game.GRID_WIDTH * Math.random())) * Game.CELL_SIZE;
			y = ((int) (Game.GRID_HEIGHT * Math.random())) * Game.CELL_SIZE;

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

	public void draw(Graphics g) {
		g.setColor(color);
		g.fillRect(x, y, Game.CELL_SIZE, Game.CELL_SIZE);
	}

	public void init(ArrayList<SnakeBody> body) {
		randomCoordinates(body);

		color = randomColor();
	}

}
