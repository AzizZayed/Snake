package com.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int CELL_SIZE = 15; //px
	private static final int GRID_SIZE = 40; //cells

	public final static int WIDTH = CELL_SIZE * GRID_SIZE;
	public final static int HEIGHT = WIDTH; // 1:1 aspect ratio

	public final static int GRID_WIDTH = WIDTH / CELL_SIZE;
	public final static int GRID_HEIGHT = HEIGHT / CELL_SIZE;

	public boolean running = false; // true if the game is running
	private Thread gameThread; // thread where the game is updated AND drawn (single thread game)

	// snake arraylist snake
	private Snake snake;

	// food
	private Food food;

	public Game() {

		canvasSetup();
		initialise();

		new Window("Snake", this);

		//this.addKeyListener(new KeyInput(snake));
		this.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				
				if (key == KeyEvent.VK_UP) {
					snake.setyDirection((byte) -1);
				}
				else if (key == KeyEvent.VK_DOWN) {
					snake.setyDirection((byte) 1);
				}
				else if (key == KeyEvent.VK_RIGHT) {
					snake.setxDirection((byte) 1);
				}
				else if (key == KeyEvent.VK_LEFT) {
					snake.setxDirection((byte) -1);
				}
			}
		});

		this.setFocusable(true);

	}

	/**
	 * initialize all our game objects
	 */
	private void initialise() {
		// Initialize snake object
		snake = new Snake();

		// Initialize food object
		food = new Food(snake);
	}

	/**
	 * just to setup the canvas to our desired settings and sizes
	 */
	private void canvasSetup() {
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setMaximumSize(new Dimension(WIDTH, HEIGHT));
		this.setMinimumSize(new Dimension(WIDTH, HEIGHT));
	}

	/**
	 * Game loop
	 */
	@Override
	public void run() {
		// so you can keep your sanity, I won't explain the game loop... you're welcome

		this.requestFocus();
		// game timer

		final double MAX_FRAMES_PER_SECOND = 30.0;
		final double MAX_UPDATES_PER_SECOND = 15.0;

		long startTime = System.nanoTime();
		final double uOptimalTime = 1000000000 / MAX_UPDATES_PER_SECOND;
		final double fOptimalTime = 1000000000 / MAX_FRAMES_PER_SECOND;
		double uDeltaTime = 0, fDeltaTime = 0;
		int frames = 0, updates = 0;
		long timer = System.currentTimeMillis();

		while (running) {

			long currentTime = System.nanoTime();
			uDeltaTime += (currentTime - startTime) / uOptimalTime;
			fDeltaTime += (currentTime - startTime) / fOptimalTime;
			startTime = currentTime;

			while (uDeltaTime >= 1) {
				update();
				updates++;
				uDeltaTime--;
			}

			while (fDeltaTime >= 1) {
				render();
				frames++;
				fDeltaTime--;
			}

			if (System.currentTimeMillis() - timer >= 1000) {
				System.out.println("UPS: " + updates + ", FPS: " + frames);
				frames = 0;
				updates = 0;
				timer += 1000;
			}
		}

		stop();
	}

	/**
	 * start the thread and the game
	 */
	public synchronized void start() {
		gameThread = new Thread(this);
		/*
		 * since "this" is the "Game" Class you are in right now and it implements the
		 * Runnable Interface we can give it to a thread constructor. That thread with
		 * call it's "run" method which this class inherited (it's directly above)
		 */
		gameThread.start(); // start thread
		running = true;
	}

	/**
	 * Stop the thread and the game
	 */
	public void stop() {
		try {
			gameThread.join();
			running = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * draw the back and all the objects
	 */
	public void render() {
		// Initialize drawing tools first before drawing

		BufferStrategy buffer = this.getBufferStrategy(); // extract buffer so we can use them
		// a buffer is basically like a blank canvas we can draw on

		if (buffer == null) { // if it does not exist, we can't draw! So create it please
			this.createBufferStrategy(3); // Creating a Triple Buffer
			/*
			 * triple buffering basically means we have 3 different canvases this is used to
			 * improve performance but the drawbacks are the more buffers, the more memory
			 * needed so if you get like a memory error or something, put 2 instead of 3 or
			 * even 1...if you run a computer from 2002...
			 * 
			 * BufferStrategy:
			 * https://docs.oracle.com/javase/7/docs/api/java/awt/image/BufferStrategy.html
			 */

			return;
		}

		Graphics g = buffer.getDrawGraphics(); // extract drawing tool from the buffers
		/*
		 * Graphics is class used to draw rectangles, ovals and all sorts of shapes and
		 * pictures so it's a tool used to draw on a buffer
		 * 
		 * Graphics: https://docs.oracle.com/javase/7/docs/api/java/awt/Graphics.html
		 */

		// draw background
		drawBackground(g);

		// draw snake
		snake.draw(g);

		// draw food
		food.draw(g);

		// actually draw
		g.dispose();
		buffer.show();

	}

	/**
	 * Draw background
	 * 
	 * @param g Graphics used to draw on the Canvas
	 */
	private void drawBackground(Graphics g) {
		// black background
		g.setColor(Color.black);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * update settings and move all objects
	 */
	public void update() {
		// update snake (movements)
		snake.update(food);
		
	}

	/**
	 * start of the program
	 */
	public static void main(String[] args) {
		new Game();
	}

}