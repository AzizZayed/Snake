package com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

/**
 * class contains the snake
 * 
 * @author Zayed
 *
 */
public class Snake {

	private int tailAdd = 4; // length we add every time food is eaten
	private int tailStart = 1; // length at the start

	/*
	 * direction in x and y, if one is not zero, the other must be zero since the
	 * snake can not move diagonal
	 */
	private byte xDirection = 0;
	private byte yDirection = 0;

	private int score = 0; // hold the score of the player
	private int scoreMult = 30; // used to calculate new score when the player eats food

	// All the body parts, the body part at index 0 is the head
	private ArrayList<SnakeBody> body;

	/**
	 * Constructor: create and initialize the snake
	 * 
	 * @param gridWidth  - width of the canvas in grids
	 * @param gridHeight - height of the canvas in grids
	 * @param cellSize   - size of one grid cell in pixels
	 */
	public Snake(int gridWidth, int gridHeight, int cellSize) {
		score = 0;
		xDirection = 0;
		yDirection = 0;

		body = new ArrayList<SnakeBody>();

		int x = (gridWidth / 2) * cellSize;
		int y = (gridHeight / 2) * cellSize;

		for (int i = 0; i < tailStart; i++) {
			body.add(new SnakeBody(x, y));
		}
	}

	/**
	 * restart from the initial tail length. This function is usually called when
	 * the snake hits itself
	 */
	private void restart() {
		score = 0;

		for (int i = body.size() - 1; i >= tailStart; i--)
			body.remove(i);
	}

	/**
	 * Set the direction in x. Make sure the snake is not already moving in the
	 * opposite direction of the desired direction. This is illegal in Snake
	 * 
	 * @param xDir - the x direction to set
	 */
	public void setxDirection(byte xDir) {
		if (xDirection != -xDir)
			this.xDirection = xDir;
		yDirection = 0;
	}

	/**
	 * Set the direction in y. Make sure the snake is not already moving in the
	 * opposite direction of the desired direction. This is illegal in Snake
	 * 
	 * @param yDir - the y direction to set
	 */
	public void setyDirection(byte yDir) {
		if (yDirection != -yDir)
			this.yDirection = yDir;
		xDirection = 0;
	}

	/**
	 * add a bunch of new body parts at the end of the tail. They are added at the
	 * same position of the last body part to make it look like it is actually
	 * growing
	 */
	private void addToTail() {
		SnakeBody last = body.get(body.size() - 1);
		int x = last.getX();
		int y = last.getY();

		for (int i = 0; i < tailAdd; i++) {
			body.add(new SnakeBody(x, y));
		}
	}

	/**
	 * test if the snake ate the food passed in parameter. Basically a simple
	 * collision test
	 * 
	 * @param food - the food we will test if it was eaten
	 * @return true if the food was eaten, false if otherwise
	 */
	private boolean ateFood(Food food) {
		SnakeBody head = body.get(0);

		return (head.getX() == food.getX() && head.getY() == food.getY());
	}

	/**
	 * test if the snake's head hit a part of its tail
	 * 
	 * @return true if the head hit the tail, false if otherwise
	 */
	private boolean hitTail() {
		SnakeBody head = body.get(0);
		boolean hit = false;

		int i = 1;
		while (!hit && i < body.size()) {
			SnakeBody sb = body.get(i);
			hit = (head.getX() == sb.getX() && head.getY() == sb.getY());
			i++;
		}

		return hit;
	}

	/**
	 * Move the snake
	 * 
	 * @param width    - width of canvas in pixels
	 * @param height   - height of canvas in pixels
	 * @param cellSize - size of one grid cell in pixels
	 */
	private void move(int width, int height, int cellSize) {

		SnakeBody head = body.get(0);

		int x = head.getX() + cellSize * xDirection;
		int y = head.getY() + cellSize * yDirection;

		// determine the position of the new head
		SnakeBody newHead = new SnakeBody(x < 0 ? width + x : x % width, y < 0 ? height + y : y % height);

		body.remove(body.size() - 1);
		body.add(0, newHead);

	}

	/**
	 * @return the body of the snake, type SnakeBody
	 */
	public ArrayList<SnakeBody> getBody() {
		return body;
	}

	/**
	 * Draw the snake
	 * 
	 * @param g        - Graphics used to draw on the Canvas
	 * @param cellSize - size of one grid cell in pixels
	 */
	public void draw(Graphics g, int cellSize) {
		for (SnakeBody bodyPart : body) {
			bodyPart.draw(g, cellSize);
		}

		int txtSize = 25;
		g.setColor(Color.BLACK);
		g.setFont(new Font("Times New Roman", Font.BOLD, txtSize));
		g.drawString("Score: " + score, 5, txtSize);
	}

	/**
	 * update the snake object. In one update, we move the snake. Then we check for
	 * collisions with the wall, the snake's tail and a piece of food
	 * 
	 * @param food:      used for the collision tests
	 * @param gridWidth  - width of the canvas in grids
	 * @param gridHeight - height of the canvas in grids
	 * @param width      - width of canvas in pixels
	 * @param height     - height of canvas in pixels
	 * @param cellSize   - size of one grid cell in pixels
	 */
	public boolean update(Food food, int gridWidth, int gridHeight, int width, int height, int cellSize) {
		move(width, height, cellSize);

		if (hitTail())
			restart();

		if (ateFood(food)) {
			addToTail();
			score += tailAdd * scoreMult;

			return true;
		}

		return false;
	}
}
