package game;

public class Character extends Sprite {
	
	private Boolean landed, leftLog;
	private Obstacle log;
	
	public Obstacle getLog(){
		return this.log;
	}

	public Boolean leftLog(){
		return this.leftLog;
	}
	
	public Boolean getLanded() {
		return landed;
	}
	
	public void setLog(Obstacle l) {
		this.log = l;
		if(l != null)
			this.landed = true;
		else 
			this.leftLog = true;
	}

	public Character() {
		super();
		init();
		// TODO Auto-generated constructor stub
	}

	public Character(int x, int y, int height, int width, String image) {
		super(x, y, height, width, image);
		init();
		// TODO Auto-generated constructor stub
	}
	
	public void init() {
		this.landed = false;
		this.leftLog = false;
	}

}
