package tOther;

import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;

public abstract class GameLoopFX extends Canvas {

	public GameLoopFX(Parent root, double w, double h) {
		super();
	}

	public String CURRENT_VERSION = "0.0";

	private int fps;
	private int tps;
	private int frameCount;
	private int tickCount;
	private double tickRate = 0.5;
	private double sinceLastTick = 0;
	private double elapsedTime = 0;
	private final int maxFps = 20;
	private boolean paused;
	private boolean running;

	/**
	 * default 0.5
	 * @param rate
	 */
	public void setTickRate(double rate){
		tickRate = rate;
	}
	
	public double getTickRate(){
		return tickRate;
	}
	
	public int getTickCount(){
		return tickCount;
	}
	
	public int getFrameCount(){
		return frameCount;
	}
	
	protected abstract void gameTick();

	public double getElapsedTime(){
		return elapsedTime;
	}

	public int getFps(){
		return fps;
	}

	public int getMaxFps(){
		return maxFps;
	}

	public double getTimeSinceLastTick(){
		return sinceLastTick;
	}

	public int getTps(){
		return tps;
	}

	public boolean hasStarted(){
		return running;
	}

	public boolean isPaused(){
		return paused;
	}

	public void setPaused(boolean b){
		paused = b;
	}

	public void endLoop(){
		running = false;
	}
	
	public void startLoop() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Everything beyond this point is a mystery
				if (!running) {
					running = true;
					long lastLoopTime = System.nanoTime();
					long optimal_time = 1000000000 / maxFps;
					long lastFpsTime = 0;
					while (running) {
						long now = System.nanoTime();
						long updateLength = now - lastLoopTime;
						lastLoopTime = now;
						double delta = ((double) updateLength) / (100000000);
						lastFpsTime += updateLength;
						tick(delta);
						frameCount++;
						if (lastFpsTime >= 1000000000) {
							fps = frameCount;
							tps = tickCount;
							lastFpsTime = 0;
							frameCount = 0;
							tickCount = 0;
						}

						try {
							long waitTime = (lastLoopTime - System.nanoTime() + optimal_time) / 1000000;
							if (waitTime > 0) {
								Thread.sleep(waitTime);
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}).start();
	}

	private void tick(double delta) {
		elapsedTime += delta;
		sinceLastTick += delta;

		// this knows when to fire gameTick()
		if (tickRate > 0 && sinceLastTick >= tickRate) {
			if (!paused) {
				tickCount++;
				gameTick();
				sinceLastTick -= tickRate;
			}
		}
	}
}
