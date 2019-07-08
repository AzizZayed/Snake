package com.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;

import javax.swing.JFrame;

public class GameManager extends Canvas implements Runnable {

	private static final long serialVersionUID = 1L;

	public static final int CELL_SIZE = 15; // in pixels

	public final static int GRID_WIDTH = 40; // in cells
	public final static int GRID_HEIGHT = 40; // in cells

	public final static int WIDTH = CELL_SIZE * GRID_WIDTH; // width of the screen in pixels
	public final static int HEIGHT = CELL_SIZE * GRID_HEIGHT; // height of the screen in pixels

	public boolean running = false; // true if the game is running
	private boolean paused = false; //updating or not
	private Thread gameThread; // thread where the game is updated AND drawn (single thread game)

	// the actual snake
	private Snake snake;

	// the food
	private Food food;
	/**
	 * Constructor: Create and initialize the JFrame and the canvas
	 */
	public GameManager() {

		canvasSetup();
		initialize();

		newWindow();

		/*
		 * add something that will detect events on the keyboard like key presses and
		 * key releases. That thing is called a keyListener
		 */
		this.addKeyListener(new KeyAdapter() {

			/**
			 * Detect key presses on the keyboard
			 * 
			 * @Param e: the information on the key press
			 */
			@Override
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();

				if (key == KeyEvent.VK_UP) { // up array key presses
					snake.setyDirection((byte) -1);
				} else if (key == KeyEvent.VK_DOWN) { // down array key presses
					snake.setyDirection((byte) 1);
				} else if (key == KeyEvent.VK_RIGHT) { // right array key presses
					snake.setxDirection((byte) 1);
				} else if (key == KeyEvent.VK_LEFT) { // left array key presses
					snake.setxDirection((byte) -1);
				} else if (key == KeyEvent.VK_SPACE){
					paused = !paused;
				}
			}
		});

		this.setFocusable(true);
	}

	/**
	 * Create the window where our Canvas will go (this class inherits from Canvas)
	 * The window will be a JFrame
	 */
	private void newWindow() {
		JFrame frame = new JFrame("Snake");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

		start(); // start the thread and the game
	}

	/**
	 * initialize all our game objects
	 */
	private void initialize() {
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
	 * 
	 * meat of our game
	 */
	@Override
	public void run() {
		// I have a full video explaining this game loop on my channel Coding Heaven

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

		stop(); // stop the thread and the game
	}

	/**
	 * start the thread and the game
	 */
	public synchronized void start() {
		gameThread = new Thread(this);
		/*
		 * since "this" is the "GameManager" Class you are in right now and it
		 * implements the Runnable Interface we can give it to a thread constructor.
		 * That thread with call it's "run" method which this class inherited (it's
		 * directly above)
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
	 * draw the background and all the objects using buffers
	 */
	public void render() {
		// Initialize drawing tools first before drawing

		BufferStrategy buffer = this.getBufferStrategy(); // extract buffer so we can use them
		// a buffer is basically like a blank canvas we can draw on

		if (buffer == null) { // if it does not exist, we can't draw! So create it please
			this.createBufferStrategy(3); // Creating a Triple Buffer
			/*
			 * triple buffering basically means we have 3 different canvases. This is used
			 * to improve performance but the drawbacks are the more buffers, the more
			 * memory needed so if you get like a memory error or something, put 2 instead
			 * of 3, or even 1...if you run a computer from 2002...
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

		// actually draw. If this isn't called, we won't see what we draw on the buffer
		buffer.show();

	}

	/**
	 * Draw background
	 * 
	 * @param g Graphics used to draw on the Canvas
	 */
	private void drawBackground(Graphics g) {
		// black background
		g.setColor(Color.white);
		g.fillRect(0, 0, WIDTH, HEIGHT);
	}

	/**
	 * update settings and update all objects
	 */
	public void update() {
		// update snake (movements and collisions)
		if (!paused) {
			snake.update(food);
		}
	}

	/**
	 * main method: the very start of the program
	 */
	public static void main(String[] args) {
		new GameManager();
	}

}
