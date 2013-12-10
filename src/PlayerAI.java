
public class PlayerAI {
	
	private Player p;
	private Player o;
	private Birdie b;
	double targetOffset;
	double servePos;
	private static int serveWait = 50;
	private int waitingToServe = 50;
	
	public PlayerAI(Player p, Player o, Birdie b) {
		this.p = p;
		this.b = b;
		this.o = o;
		targetOffset = p.width/2;
		servePos = p.toPixels(1.98);
	}
	
	public void update(double dt) {
		p.hitReleased();
		if (b.lastHitBy == p) {
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
				System.out.println("hit");
				p.leftPressed();
				p.hitPressed();
			}
		}
		if (o.isServing) {
			p.v_x = 0;
			p.pos_x = servePos + p.min_x;
		}
		if (p.isServing && waitingToServe == 0) {
			
			p.hitPressed();
			waitingToServe = serveWait;
		}
		if (p.isServing && waitingToServe > 0) {
			System.out.println(waitingToServe);
			waitingToServe--;
		}
	}

}
