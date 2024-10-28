package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import game.Character;
import game.GameProperties;
import game.Obstacle;


// Open app, prompt for user name
// Search database for user data, load score
// Set up frog and cars, game starts
// 


public class GamePrep extends JFrame implements KeyListener, ActionListener {

	//Frog
	private Character frog;
	private JLabel frogLabel;
	
	//GUI variables
	private Container content;
	private JLabel backgroundLabel;
	private ImageIcon backgroundIcon;

	//Cars
	//private Obstacle car1,car2,car3,car4;
	private Obstacle[] row1Cars;
	private Obstacle[] cars;
	
	//Logs
	private Obstacle[] logs;
	
	//private JLabel[] carLables;
	//private JLabel[] logLables;
	
	private JButton startButton;
	
	
	//GUI setup
	public GamePrep() {
		super("Frogger");
		
		frog = new Character(GameProperties.SCREEN_WIDTH / 2 - GameProperties.FROG_WIDTH, GameProperties.SCREEN_HEIGHT - GameProperties.FROG_HEIGHT - 30, 
				GameProperties.FROG_HEIGHT, GameProperties.FROG_WIDTH, GameProperties.FROG_IMAGE);
		
		
		/*Obstacle car1 = new Obstacle(0, 570, 50, 50, 20, "car.png", "colcar.png");
		Obstacle car2 = new Obstacle(80, 570, 50, 50, 20, "car.png", "colcar.png");
		Obstacle car3 = new Obstacle(160, 570, 50, 50, 20, "car.png", "colcar.png");
		Obstacle car4 = new Obstacle(400, 570, 50, 50, 20, "car.png", "colcar.png");*/
		
		
		cars = new Obstacle[] {
				new Obstacle(0, 560, 50, 50, 20, "car.png", "colcar.png"),
				new Obstacle(80, 560, 50, 50, 20, "car.png", "colcar.png"),
				new Obstacle(160, 560, 50, 50, 20, "car.png", "colcar.png"),
				new Obstacle(400, 560, 50, 50, 20, "car.png", "colcar.png"),

				new Obstacle(0, 500, 50, 50, 30, "car.png", "colcar.png"),
				new Obstacle(80, 500, 50, 50, 30, "car.png", "colcar.png"),
				new Obstacle(160, 500, 50, 50, 30, "car.png", "colcar.png"),
				new Obstacle(400, 500, 50, 50, 30, "car.png", "colcar.png"),

				new Obstacle(0, 450, 50, 50, 20, "car.png", "colcar.png"),
				new Obstacle(80, 450, 50, 50, 20, "car.png", "colcar.png"),
				new Obstacle(160, 450, 50, 50, 20, "car.png", "colcar.png"),
				new Obstacle(400, 450, 50, 50, 20, "car.png", "colcar.png"),
		};
		
		logs = new Obstacle[] {
				new Obstacle(0, 130, 40, 70, 30, "log.png", "colcar.png"),
				new Obstacle(80, 130, 40, 70, 30, "log.png", "colcar.png"),
				new Obstacle(160, 130, 40, 70, 30, "log.png", "colcar.png"),
				new Obstacle(400, 130, 40, 70, 30, "log.png", "colcar.png"),
				new Obstacle(0, 200, 40, 70,20, "log.png", "colcar.png"),
				new Obstacle(80, 200, 40, 70, 20, "log.png", "colcar.png"),
				new Obstacle(160, 200, 40, 70, 20, "log.png", "colcar.png"),
				new Obstacle(400, 200, 40, 70, 20, "log.png", "colcar.png"),
				new Obstacle(0, 270, 40, 70, 30, "log.png", "colcar.png"),
				new Obstacle(80, 270, 40, 70, 20, "log.png", "colcar.png"),
				new Obstacle(160, 270, 40, 70, 30, "log.png", "colcar.png"),
				new Obstacle(400, 270, 40, 70, 20, "log.png", "colcar.png"),
		};
		
		//set up screen
		setSize(GameProperties.SCREEN_WIDTH, 
				     GameProperties.SCREEN_HEIGHT);
		content = getContentPane();
		content.setBackground(Color.gray);
		setLayout(null);

		
		backgroundIcon = new ImageIcon(getClass().getResource("images/background.jpg"));

		backgroundLabel = new JLabel();
		
		backgroundLabel.setIcon(backgroundIcon);
		backgroundLabel.setSize(
				GameProperties.SCREEN_WIDTH, 
			     GameProperties.SCREEN_HEIGHT
		);
		backgroundLabel.setLocation(0,0);
		
		
		frogLabel= new JLabel();
		
		frogLabel.setIcon(new ImageIcon(
				getClass().getResource("images/" + frog.getImage()
						)));
		frogLabel.setSize(
				frog.getWidth(),
				frog.getHeight()
		);
		frogLabel.setLocation(
				frog.getX(), frog.getY() );

		add(frogLabel);

		for(int i = 0;i<cars.length;i++) {

			addObstacle(cars[i], true);
		
		}
		for(int i = 0;i<logs.length;i++) {
			addObstacle(logs[i], false);
		}

		add(backgroundLabel);
		
		
		content.addKeyListener(this);
		content.setFocusable(true);

		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void addObstacle(Obstacle obstacle, Boolean isCar) {
		Obstacle car = obstacle;
		JLabel carLabel = new JLabel();
		carLabel.setIcon(new ImageIcon(
				getClass().getResource("images/" + car.getImage()
						)));
		carLabel.setSize(
				car.getWidth(),
				car.getHeight()
				);
		carLabel.setLocation(
				car.getX(), car.getY());
		

		car.setFrog(frog);
		car.setFrogLabel(frogLabel);
		car.setLabel(carLabel);
		car.setCar(isCar);
		car.setGame(this);
		
		
		car.startThread();
		
		add(carLabel);
		
	}
	
	public static void main(String[] args) {
		GamePrep myGame = new GamePrep();
		myGame.setVisible(true);
	}


	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void keyPressed(KeyEvent e) {
		//get current position
		int x = frog.getX();
		int y = frog.getY();
		
		//detect direction
		if ( e.getKeyCode() == KeyEvent.VK_UP ) {
			
			y -= GameProperties.CHARACTER_STEP;
			
			if ( y + frog.getHeight() <=  0) {

				y = GameProperties.SCREEN_HEIGHT;

			}
			
		} else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
			
			if ( y >= GameProperties.SCREEN_HEIGHT - frog.getHeight()) 
				y += GameProperties.CHARACTER_STEP;
			
			if ( y >= GameProperties.SCREEN_HEIGHT - frog.getHeight()) {
				
				y = -1 * frog.getHeight();
				
			}
			
		} else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
			
			x -= GameProperties.CHARACTER_STEP;
			
			if (x + frog.getWidth() <= 0) {

				x = GameProperties.SCREEN_WIDTH;

			}
			
		}  else if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			
			x += GameProperties.CHARACTER_STEP;
			
			if ( x >= GameProperties.SCREEN_WIDTH ) {

				x = -1 * frog.getWidth();

			}
			
		} 
		
		//update frog
		frog.setX(x);
		frog.setY(y);
		
		//move label
		frogLabel.setLocation(
				frog.getX(), frog.getY() );
		
		if(frog.getLanded() && frog.getLog() != null) {
			frog.getLog().setMoveFrog(false);
			frog.setLog(null);
			

			System.out.println("Left Log!");
		}
		
		if(y < 50) {
			if(frog.getLanded()) {
				JOptionPane.showMessageDialog(null, "You won!");
			} else {
				JOptionPane.showMessageDialog(null, "You're dead");
			}
		}
		
	}
	
	public void handleCarCollision(Obstacle car) {
		for(int i = 0;i < 4;i++) {
			cars[i].stopThread();
		}
		JOptionPane.showMessageDialog(null, "You're dead");
	}
	
	public void handleLogLanding(Obstacle log) {
		if(!frog.getLanded()) {
			System.out.println("Landed!");
			frog.setLog(log);
			log.setMoveFrog(true);
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		
		/*if ( e.getSource() == visibilityButton ) {
			
			System.out.println("visibilityButton pressed");	
			
			if ( character2.getVisible() ) {
				//character2.hide();
			} else {
				//character2.show();
			}
		
		}*/

		if ( e.getSource() == startButton ) {
			
			System.out.println("startButton pressed");	
			
			for(int i = 0;i < 4;i++) {
				row1Cars[i].startThread();
			}
		
		}
		
	}

}
