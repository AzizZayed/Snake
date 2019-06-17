package com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Snake {

	private int tailAdd; // length we add every time food is eaten
	private int tailStart; // length at the start

	/*
	 * direction in x and y, if one is not zero, the other must be zero since the
	 * snake can not move diagonal
	 */
	private byte xDirection = 0;
	private byte yDirection = 0;

	private int score = 0;

	// All the body parts, the body part at index 0 is the head
	private ArrayList<SnakeBody> body;

	/**
	 * Constructor: create and initialize the snake
	 */
	public Snake() {
		tailAdd = 4;
		tailStart = 1;
		init(null);
	}

	/**
	 * Initialize the snake
	 * 
	 * @param food: to initialize the food object right after sometimes this
	 *              parameter will be null if there is no food object to pass in yet
	 */
	private void init(Food food) {
		score = 0;
		xDirection = 0;
		yDirection = 0;

		body = new ArrayList<SnakeBody>();

		int x = (GameManager.GRID_WIDTH / 2) * GameManager.CELL_SIZE;
		int y = (GameManager.GRID_HEIGHT / 2) * GameManager.CELL_SIZE;
		for (int i = 0; i < tailStart; i++) {
			body.add(new SnakeBody(x, y));
		}

		if (food != null)
			food.init(body);
	}

	/**
	 * restart from the initial tail length. Means the snake hit itself probably
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
	 * @param xDirection: the xDirection to set
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
	 * @param yDirection: the yDirection to set
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
	 * @param food: the food we will test if it was eaten
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
			hit = (head.getX() == body.get(i).getX() && head.getY() == body.get(i).getY());
			i++;
		}

		return hit;
	}

	/**
	 * test if the snake's head hit a wall
	 * 
	 * @return true if the snake hit the wall, false if otherwise
	 */
	private boolean hitWall() {
		SnakeBody head = body.get(0);
		int x = head.getX();
		int y = head.getY();

		return (x < 0 || y < 0 || x >= GameManager.WIDTH || y >= GameManager.HEIGHT);
	}

	/**
	 * Move the snake
	 */
	private void move() {

		SnakeBody head = body.get(0);

		body.remove(body.size() - 1);
		body.add(0, new SnakeBody(head.getX() + GameManager.CELL_SIZE * xDirection,
				head.getY() + GameManager.CELL_SIZE * yDirection));

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
	 * @param g Graphics used to draw on the Canvas
	 */
	public void draw(Graphics g) {
		Color color = Color.WHITE;

		for (SnakeBody bodyPart : body) {
			bodyPart.draw(g, color);
		}

		int txtSize = 25;
		g.setColor(color);
		g.setFont(new Font("Times New Roman", Font.BOLD, txtSize));
		g.drawString("Score: " + Integer.toString(score), 5, txtSize);
	}

	/**
	 * update the snake object. In one update, we move the snake. Then we check for
	 * collisions with the wall, the snake's tail and a piece of food
	 * 
	 * @param food: used for the collision tests
	 */
	public void update(Food food) {
		move();

		if (hitTail())
			restart();
		if (hitWall())
			init(food);

		if (ateFood(food)) {
			addToTail();
			score += tailAdd * 15;
			food.init(body);
		}

	}
}