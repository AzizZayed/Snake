package com.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;

public class Snake {

	private int tailAdd; // length we add every time food is eaten
	private int tailStart; // length at the start
	private byte xDirection = 0;
	private byte yDirection = 0;
	private int score = 0;

	private ArrayList<SnakeBody> body;

	public Snake() {
		tailAdd = 4;
		tailStart = 1;
		init(null);
	}

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

	private void restart() {
		score = 0;
		for (int i = body.size() - 1; i >= tailStart; i--)
			body.remove(i);
	}

	/**
	 * @param xDirection the xDirection to set
	 */
	public void setxDirection(byte xDir) {
		if (xDirection != -xDir)
			this.xDirection = xDir;
		yDirection = 0;
	}

	/**
	 * @param yDirection the yDirection to set
	 */
	public void setyDirection(byte yDir) {
		if (yDirection != -yDir)
			this.yDirection = yDir;
		xDirection = 0;
	}

	private void addToTail() {
		SnakeBody last = body.get(body.size() - 1);
		int x = last.getX();
		int y = last.getY();

		for (int i = 0; i < tailAdd; i++) {
			body.add(new SnakeBody(x, y));
		}
	}

	private boolean ateFood(Food food) {
		SnakeBody head = body.get(0);

		return (head.getX() == food.getX() && head.getY() == food.getY());
	}

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

	private boolean hitWall() {
		SnakeBody head = body.get(0);
		int x = head.getX();
		int y = head.getY();

		return (x < 0 || y < 0 || x >= GameManager.WIDTH || y >= GameManager.HEIGHT);
	}
	
	private void move() {

		SnakeBody head = body.get(0);

		if (body.size() == 1) {
			head.move(xDirection, yDirection);
		} else {

			body.remove(body.size() - 1);
			body.add(0, new SnakeBody(head.getX() + GameManager.CELL_SIZE * xDirection,
					head.getY() + GameManager.CELL_SIZE * yDirection));
		}

	}

	/**
	 * @return the body
	 */
	public ArrayList<SnakeBody> getBody() {
		return body;
	}

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