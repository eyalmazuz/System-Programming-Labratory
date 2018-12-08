package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.TerminateBroadcast;
import bgu.spl.mics.application.messages.TickBroadcast;
import bgu.spl.mics.application.passiveObjects.Inventory;
import bgu.spl.mics.application.passiveObjects.MoneyRegister;
import bgu.spl.mics.application.passiveObjects.ResourcesHolder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * TimeService is the global system timer There is only one instance of this micro-service.
 * It keeps track of the amount of ticks passed since initialization and notifies
 * all other micro-services about the current time tick using {@link TickBroadcast}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link ResourcesHolder}, {@link MoneyRegister}, {@link Inventory}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class TimeService extends MicroService{

	private int tick;
	private int speed;
	private int tickPeriod;
	private Timer timer;

	public TimeService(int tickPeriod,int speed) {
		super("TimeService");
		this.tick = 0;
		this.speed = speed;
		this.tickPeriod=tickPeriod;
		this.timer = new Timer();
	}

	long curr;

	@Override
	protected void initialize() {
		subscribeBroadcast(TerminateBroadcast.class, br->{
			Thread.currentThread().interrupt();
		});
		curr = System.currentTimeMillis();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				tick++;
				System.out.println("Tick " + tick +" took " + (System.currentTimeMillis()- curr));
				curr = System.currentTimeMillis();
				sendBroadcast(new TickBroadcast("TickBroadcast"+tick,tick));
				if (tick == tickPeriod) {
					sendBroadcast(new TerminateBroadcast("TerminateBroadcast"));

					timer.cancel();
				}
			}
		},0,speed);


	}



}
