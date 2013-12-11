import java.util.ArrayList;


public class PlayerAI {
	
	private Player p;
	private Player o;
	private Birdie b;
	double targetOffset;
	double servePos;
	private static int serveWait = 50;
	private int waitingToServe = 50;
	private double court_width;
	
	private ArrayList<Instruction> todo;
	
	public PlayerAI(Player p, Player o, Birdie b, double court_width) {
		this.p = p;
		this.b = b;
		this.o = o;
		targetOffset = p.width/3;
		servePos = p.toPixels(1.98);
		todo = new ArrayList<Instruction>();
		this.court_width = court_width;
	}
	public void opponentHit() {
		//System.out.println("new instruction");
		todo = new ArrayList<Instruction>();
		double x = b.predictXforY(p.height*1.4, 25/1000.0);
		x -= targetOffset;
		todo.add(this.new Instruction(InstructType.MOVETO, x));
		if (x < p.min_x+court_width/8)
			todo.add(this.new Instruction(InstructType.DROP, x));
		else if (x < p.min_x + court_width/4)
			todo.add(this.new Instruction(InstructType.HIT, x));
		else if (x < p.min_x + court_width/2) {
			//System.out.println(x + " " + p.min_x + " " + court_width + " " + ( p.min_x + court_width));
			todo.add(this.new Instruction(InstructType.CLEAR, x));
		}
		System.out.println(x + " " + b.pos_x);
	}
	public void update(double dt) {
		p.hitReleased();
		
		if (todo.size() > 0) {
			Instruction i = todo.get(0);
			i.execute();
			if (i.isDone) {
				todo.remove(0);
				if (i.t == InstructType.CLEAR || i.t == InstructType.DROP ||
					i.t == InstructType.HIT) {
					todo.add(this.new Instruction(InstructType.MOVETO, p.min_x + court_width/4));
				}
			}
			//System.out.println(i.t + " " + i.isDone + " " + i.v + " " + p.pos_x);
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
	private  enum InstructType {CLEAR, DROP, HIT, MOVETO}
	
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
					
					isDone = true;
				}
				if (p.inRange(b)) {
					
					
					p.hitPressed();
					hasHit = true;
				} 
				break;
			case CLEAR:
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
			case DROP: 
				if (hasHit) {
					p.rightReleased();
					isDone = true;
				}
				if (p.inRange(b)) {
					
					p.rightPressed();
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
