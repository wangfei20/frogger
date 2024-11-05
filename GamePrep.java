package game;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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
	private Connection conn;
	private JLabel nameLabel;
	private JLabel scoreLabel;
	private int score;
	private String name;
	
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
	public GamePrep(Connection conn) {
		super("Frogger");
		
		this.conn = conn;

		nameLabel = new JLabel();
		scoreLabel = new JLabel();
		scoreLabel.setSize(200,30);
		nameLabel.setLocation(50, 0);
		scoreLabel.setLocation(300, 0);
		nameLabel.setSize(200,30);
		
		add(nameLabel);
		add(scoreLabel);
		
		frog = new Character(GameProperties.SCREEN_WIDTH / 2 - GameProperties.FROG_WIDTH, GameProperties.SCREEN_HEIGHT - GameProperties.FROG_HEIGHT - 30, 
				GameProperties.FROG_HEIGHT, GameProperties.FROG_WIDTH, GameProperties.FROG_IMAGE);
		
		
		/*Obstacle car1 = new Obstacle(0, 570, 50, 50, 20, "car.png", "colcar.png");
		Obstacle car2 = new Obstacle(80, 570, 50, 50, 20, "car.png", "colcar.png");
		Obstacle car3 = new Obstacle(160, 570, 50, 50, 20, "car.png", "colcar.png");
		Obstacle car4 = new Obstacle(400, 570, 50, 50, 20, "car.png", "colcar.png");*/
		
		
		cars = new Obstacle[] {
				new Obstacle(0, 560, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(80, 560, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(160, 560, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(300, 560, 50, 50, 10, "car.png", "colcar.png"),

				new Obstacle(0, 500, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(80, 500, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(160, 500, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(300, 500, 50, 50, 10, "car.png", "colcar.png"),

				new Obstacle(0, 450, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(80, 450, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(160, 450, 50, 50, 10, "car.png", "colcar.png"),
				new Obstacle(300, 450, 50, 50, 10, "car.png", "colcar.png"),
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
		

		String inputName = javax.swing.JOptionPane.showInputDialog("Input your name:");
		String sqlSelectFilter = 
				"SELECT * FROM PLAYERS WHERE name = ?";
		System.out.println(inputName);
        try (PreparedStatement pstmtSelectLike = 
        		      conn.prepareStatement(sqlSelectFilter)) {
        	pstmtSelectLike.setString(1, inputName);
            ResultSet rs = pstmtSelectLike.executeQuery();
            if(rs.next()) {
            	int id = rs.getInt("id");
                System.out.println("Id:"+id);
            	name = rs.getString("name");
            	score = rs.getInt("score");
            } else {
            	String sqlInsert = "INSERT INTO PLAYERS (NAME, SCORE) VALUES (?, ?)";
                try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

                  	//execute calls to prepared statement
                  	pstmtInsert.setString(1, inputName);
                  	pstmtInsert.setInt(2, 0);
                  	pstmtInsert.executeUpdate();
                  	
                  	System.out.println("1 records inserted");
                }
                score = 0;
                name = inputName;
            }
            
            
            System.out.println("Name:"+name);
            System.out.println("Score:"+score);
            
            rs.close();
         	
         } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
	
	private void startGame() {
		
		frog.setX(GameProperties.SCREEN_WIDTH / 2 - GameProperties.FROG_WIDTH);
		frog.setY(GameProperties.SCREEN_HEIGHT - GameProperties.FROG_HEIGHT - 30);
		
		//move label
		frogLabel.setLocation(
				frog.getX(), frog.getY() );
        nameLabel.setText("Name:"+name);
        scoreLabel.setText("Score:"+score);
        
        
	}
	
	public static void main(String[] args) {
		
		Connection conn = null;
		
		try {
			
			Class.forName("org.sqlite.JDBC");
			System.out.println("Driver Loaded");
			
			String dbURL = "jdbc:sqlite:products.db";
			conn = DriverManager.getConnection(dbURL);
			
			if (conn != null) {
				System.out.println("connected to database");
				
				DatabaseMetaData db = (DatabaseMetaData) conn.getMetaData();
				
                String sqlCreateTable = "CREATE TABLE IF NOT EXISTS PLAYERS " +
                        "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " NAME TEXT NOT NULL, " +
                        " SCORE INT NOT NULL)";

                try (PreparedStatement pstmtCreateTable = conn.prepareStatement(sqlCreateTable)) {
                	pstmtCreateTable.executeUpdate();
                	System.out.println("Table Successfully Created");
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GamePrep myGame = new GamePrep(conn);
		myGame.startGame();
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
			
			if ( y - GameProperties.CHARACTER_STEP >= 0) {
				y -= GameProperties.CHARACTER_STEP;
			}

			
		} else if ( e.getKeyCode() == KeyEvent.VK_DOWN ) {
			
			if ( y + GameProperties.CHARACTER_STEP <= (GameProperties.SCREEN_HEIGHT - frog.getHeight() - 30)) {
				y += GameProperties.CHARACTER_STEP;

			}
			
			
		} else if ( e.getKeyCode() == KeyEvent.VK_LEFT ) {
			
			if(x - GameProperties.CHARACTER_STEP >= 0) {
				x -= GameProperties.CHARACTER_STEP;
			}
			
		}  else if ( e.getKeyCode() == KeyEvent.VK_RIGHT ) {
			
			if(x + GameProperties.CHARACTER_STEP <= GameProperties.SCREEN_WIDTH - frog.getWidth()) {
				x += GameProperties.CHARACTER_STEP;
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
				gameWon();
			} else {
				gameLost();
			}
		}
		
	}
	
	public void handleCarCollision(Obstacle car) {
		gameLost();
	}
	
	public void handleLogLanding(Obstacle log) {
		if(!frog.getLanded()) {
			System.out.println("Landed!");
			frog.setLog(log);
			log.setMoveFrog(true);
		}
	}
	
	public void gameWon() {
		JOptionPane.showMessageDialog(null, "You won! Game restarted!");
		score+=50;
		updatePlayerData();
	}
	
	public void gameLost() {
		JOptionPane.showMessageDialog(null, "You're dead! Game restarted!");
		if(score >= 50) {
			score -= 50;
			updatePlayerData();
		}
		
	}
	
	private void updatePlayerData(){
		
        
        
		String sqlUpdate = "UPDATE PLAYERS SET score = ? WHERE name = ?";
        try (PreparedStatement pstmtUpdate = this.conn.prepareStatement(sqlUpdate)) {

        	pstmtUpdate.setInt(1, score);
        	pstmtUpdate.setString(2, name);
        	pstmtUpdate.executeUpdate();
        	
        } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        startGame() ;
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
