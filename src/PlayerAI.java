import java.util.ArrayList;


public class PlayerAI {
	
	private Player p;
	private Player o;
	private Birdie b;
	double targetOffset;
	double servePos;
	private static int serveWait = 50;
	private int waitingToServe = 50;
	
	private ArrayList<Instruction> todo;
	
	public PlayerAI(Player p, Player o, Birdie b) {
		this.p = p;
		this.b = b;
		this.o = o;
		targetOffset = p.width/3;
		servePos = p.toPixels(1.98);
		todo = new ArrayList<Instruction>();
	}
	public void opponentHit() {
		System.out.println("new instruction");
		todo = new ArrayList<Instruction>();
		double x = b.predictXforY(p.height*1.4, 25/1000.0);
		x -= targetOffset;
		todo.add(this.new Instruction(InstructType.MOVETO, x));
		todo.add(this.new Instruction(InstructType.HIT, x));
		System.out.println(x + " " + b.pos_x);
	}
	public void update(double dt) {
		p.hitReleased();
		/*if (b.lastHitBy == p) {
			if (p.pos_x > (p.max_x + p.min_x) / 2) {
				p.leftPressed();
				p.rightReleased();
			} else {
				p.rightPressed();
				p.leftReleased();
			}
		} else {
			if (p.pos_x + targetOffset > b.pos_x) {
				p.leftPressed();
				p.rightReleased();
			} else {
				p.rightPressed();
				p.leftReleased();
			}
			if (p.inRange(b) && !p.isServing) {
				
				p.leftPressed();
				p.hitPressed();
			}
		}*/
		if (todo.size() > 0) {
			Instruction i = todo.get(0);
			i.execute();
			if (i.isDone) {
				todo.remove(0);
			}
			System.out.println(i.t + " " + i.isDone + " " + i.v + " " + p.pos_x);
		}
		if (o.isServing) {
			todo.clear();
			p.v_x = 0;
			p.pos_x = servePos + p.min_x;
		}
		if (p.isServing && waitingToServe == 0) {
			
			p.hitPressed();
			waitingToServe = serveWait;
		}
		if (p.isServing && waitingToServe > 0) {
			
			waitingToServe--;
		}
	}
	private  enum InstructType {HIT, MOVETO}
	
	private class Instruction {
		
		public InstructType t;
		private double v;
		public boolean isDone;
		private double last_x;
		private boolean hasHit;
		public Instruction(InstructType type, double x) {
			t = type;
			v = x;
			isDone = false;
			hasHit = false;
			last_x = p.pos_x;
		}
		public void execute() {
			switch(t) {
			case HIT:
				if (hasHit) {
					p.leftReleased();
					isDone = true;
				}
				if (p.inRange(b)) {
					
					p.leftPressed();
					p.hitPressed();
					hasHit = true;
				} 
				break;
			case MOVETO:
				if (p.pos_x > v) {
					p.leftPressed();
					p.rightReleased();
				} else {
					p.rightPressed();
					p.leftReleased();
				}
				if (Math.signum(last_x-v) != Math.signum(p.pos_x-v)) {
					isDone = true;
					p.leftReleased();
					p.rightReleased();
				}
				break;
				
			}
			
			last_x = p.pos_x;
		}
	}

}
