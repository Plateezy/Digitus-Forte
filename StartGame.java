import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JPanel;

class StartGame extends JPanel implements KeyListener{
	int x=500,y=300,xTreble=0,yTreble=0,count=0,randomGen1,randomGen2,randomGen3,totalDance=4,first=0;
	Image border,player,playerdance,yes;
	BufferedImage background;
	Scanner myInput = new Scanner(new File("Highscores\\Idols.txt"));
    String idols = myInput.nextLine();
	Clip clip;
	Graphics draw;
	double delay = 0, counter=1,danceDelay=0,explosionDelay=0;
	boolean left,right,up,down,dance=false;
	tclef trebleLine[]=new tclef[8], trebleLineRight[]=new tclef[8],trebleLine2[]=new tclef[8], trebleLineRight2[]=new tclef[8],trebleLine3[]=new tclef[8], trebleLineRight3[]=new tclef[8];
	tclef currentLine[], currentLine2[],currentLine3[];
	noteExplosion notePattern[] = new noteExplosion[8],notePattern2[] = new noteExplosion[8],notePattern3[] = new noteExplosion[8];;
	bclef bassLine[]=new bclef[16],bassLine2[]=new bclef[16],bassLine3[]=new bclef[16];
	Rectangle trebleRect[] = new Rectangle[8]; 
	noteExplosionEnemy noteEnemy[] = new noteExplosionEnemy[16];
	
	Rectangle enemyTreble;
	
	
	
	//sound
	public void LoopSound() throws Exception {
        File bgMenu = new File("Music\\Game.wav");
        clip = AudioSystem.getClip();
        AudioInputStream yes = AudioSystem.getAudioInputStream( bgMenu );
        clip.open(yes);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
	
	//initialization method
	StartGame(String title) throws Exception{
		LoopSound();
		trebleSPatternINIT();		//Creates all enemies
		trebleSPatternINIT2();
		trebleSPatternINIT3();
		noteExplosionINIT();
		noteExplosionINIT2();
		noteExplosionINIT3();
		noteExplosionEnemyINIT(-50,-50);
		bassClefINIT();
		bassClefINIT2();
		bassClefINIT3();
		setPreferredSize(new Dimension(1024,720));
		addKeyListener(this);
		setFocusable(true);
		
	}
	
	public double run(){
		try{
			player = ImageIO.read(new File("Images\\BoomBox.jpg"));
			background = ImageIO.read(new File("Images\\GameBackground.jpg"));
			playerdance = ImageIO.read(new File("Images\\BoomBoxDance.jpg"));
		}catch (IOException e){
			e.printStackTrace();
		}
		while(true){
			
			repaint();
			
			Rectangle playa = new Rectangle(x,y,20,20); //creates rect of player
				
			delay += 10; //counts the delay  (counts by the thread.sleep millis)

			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
			}
			//Checking events
			if (left){
				x-=4;
			}else if (right){
				x+=4;
			}if (down){
				y+=4;
			}else if (up){
				y-=4;
			}
			
			//Prevent offscreen
			if (x>=1004)
				x=1004;
			else if(x<=0)
				x=0;
			if (y>=700)
				y=700;
			else if(y<=0)
				y=0;
			
			
			
			
			//Moving treble clef line
			
			if (delay>3000){
				
				if (delay/2000%3==0){
					bassClefINIT();
				}
				else if (bassClefPattern(playa)==0){
					clip.close();
					return delay/1000;
				}

			}
			
			if (delay>10500 && delay<18000){
				first+=10;
				if (delay/3000%2==0 && first>3000)
					trebleSPatternINIT();
				else if (trebleSPattern(playa)==0){
					clip.close();
					return delay/1000;  //Returning to return to menu after collision
				}
				if (delay/3000%2==1)
					trebleSPatternINIT2();
				else if (trebleSPattern2(playa)==0){
					clip.close();
					return delay/1000;//Returning to return to menu after collision
				}
			}
			
			if (delay>18000){// sets time of activation
				if (delay/2000%3==1){	//initializes each enemy at set intervals depending on delay
					bassClefINIT2();
				}
				else if (bassClefPattern2(playa)==0){ //If it doesn't initialize it will update the enemy location instead
					clip.close();
					return delay/1000;
				}
				
				if (delay/2000%3==2){
					bassClefINIT3();
				}
				else if (bassClefPattern3(playa)==0){
					clip.close();
					return delay/1000;
				}
				
				first+=10;
				if (delay/2000%3==0 && first>2000)
					trebleSPatternINIT();
				else if (trebleSPattern(playa)==0){
					clip.close();
					return delay/1000;  //Returning to return to menu after collision
				}
				if (delay/2000%3==1)
					trebleSPatternINIT2();
				else if (trebleSPattern2(playa)==0){
					clip.close();
					return delay/1000;//Returning to return to menu after collision
				}
				
				if (delay/2000%3==2)
					trebleSPatternINIT3();
				else if (trebleSPattern3(playa)==0){
					clip.close();
					return delay/1000;//Returning to return to menu after collision
				}
				
				if (delay%10000==0){
					noteExplosionEnemyINIT(x,y);
					explosionDelay=0;
				}else{
					explosionDelay+=10;
					if (explosionDelay>3000){
						if (noteExplosionEnemyPattern(playa)==0){
							clip.close();
							return delay/1000; 
						}
					}
				}
				
				
			}
			
		
			
			//If spacebar was pressed in the last 8 seconds, run the note explosion patterns
			if (dance){
				danceDelay +=10;
				if (danceDelay/2000%3==0){
					noteExplosionINIT();
				}else{
					noteExplosionPattern();
				}
				if (danceDelay/2000%3==1)
					noteExplosionINIT2();
				else{
					noteExplosionPattern2();
				}
				if (danceDelay/2000%3==2)
					noteExplosionINIT3();
				else{
					noteExplosionPattern3();
				}
			}
			
			
			//Only keeps the dance event for 8 seconds (8000 milliseconds)
			if (danceDelay>7999){
				danceDelay=0;
				dance=false;
				
			}
		}
	}
	
	
	//Checks key press
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()== KeyEvent.VK_UP){
			up=true;
		}
		else if (e.getKeyCode()== KeyEvent.VK_DOWN){
			down=true;
		}
		if(e.getKeyCode()== KeyEvent.VK_RIGHT){
			right=true;
		}
		else if(e.getKeyCode()== KeyEvent.VK_LEFT){
			left=true;
		}
    }
	
	public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    	if(e.getKeyCode()== KeyEvent.VK_RIGHT){
			right=false;
		}
		else if(e.getKeyCode()== KeyEvent.VK_LEFT){
			left=false;
		}
		if (e.getKeyCode()== KeyEvent.VK_UP){
			up=false;
		}
		else if (e.getKeyCode()== KeyEvent.VK_DOWN){
			down=false;
		}
		if(e.getKeyCode()== KeyEvent.VK_SPACE){	//Turns on dance only if there are enough uses left
			if (!dance){
				totalDance-=1;
			}
			if (totalDance>0)
				dance=true;
			System.out.println(totalDance);
		}
    }
    
    public void paintComponent(Graphics g){
		g.drawImage(background,0,0,this);
		
		if (dance){		//If dance is on, changes animation every second. (1000 millis)
			if (Math.ceil(delay/1000)%2==0){
				yes = player;
			}
			else if (Math.ceil(delay/1000)%2==1)
				yes = playerdance;
		}else{
			yes = player;
		}
		g.drawImage(yes,x,y,this);
		
		
		
		//Paints all the trebles/bass in one line, checks if commanded to stop drawing
		for (tclef a: currentLine){
			if (!drawStopTclef(a.newTreble(a.x,a.y)))
				g.drawImage(a.trebleClef,a.x,a.y,this);
		}
		
		for (tclef a: currentLine2){
			if (!drawStopTclef(a.newTreble(a.x,a.y)))
				g.drawImage(a.trebleClef,a.x,a.y,this);
		}
		
		for (tclef a: currentLine3){
			if (!drawStopTclef(a.newTreble(a.x,a.y)))
				g.drawImage(a.trebleClef,a.x,a.y,this);
		}
		
		for (bclef a: bassLine){
			if (!drawStopTclef(a.newBass(a.x,a.y)))
				g.drawImage(a.bassClef,a.x,a.y,this);
		}
		
		for (bclef a: bassLine2){
			if (!drawStopTclef(a.newBass(a.x,a.y)))
				g.drawImage(a.bassClef,a.x,a.y,this);
		}
		
		for (bclef a: bassLine3){
			if (!drawStopTclef(a.newBass(a.x,a.y)))
				g.drawImage(a.bassClef,a.x,a.y,this);
		}
		
		//Paints the notes if dance was activated
		if (dance){
			for (noteExplosion a: notePattern){
				g.drawImage(a.note,a.x,a.y,this);
			}
			
			for (noteExplosion a: notePattern2){
				g.drawImage(a.note,a.x,a.y,this);
			}
			
			for (noteExplosion a: notePattern3){
				g.drawImage(a.note,a.x,a.y,this);
			}
		
		}
		
		for (noteExplosionEnemy a: noteEnemy){
			g.drawImage(a.note,a.x,a.y,this);
		}
		
		
		
		//paints time
		g.setColor(Color.white);
		g.setFont(new Font("Helvetica", Font.PLAIN, 24));
		g.drawString(Math.round((delay/1000)*100)/100+"",200,25);
		g.drawString(idols, 505, 25);
		
	}

	
    //Initializes a bass clef array
	public void bassClefINIT(){
		for (int i=0;i<16;i++){
			bassLine[i] = new bclef(i*64,-70);
		}
		bassLine[(int)(Math.random()*16)]=new bclef(-60, -110);  //Each potentially removes 1 from the 16 row.
		bassLine[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine[(int)(Math.random()*16)]=new bclef(-60, -110);
	}
	
	//Changes the position of bassClef and checks collision
	public int bassClefPattern(Rectangle player){
		for (bclef a: bassLine)
			if (!drawStopTclef(a.newBass(a.x,a.y))){
				a.y+=3;
					
						//Checking Collision with bass clef
				if (player.intersects(a.newBass(a.x, a.y))){
					return 0;
				}
				
				
				//If spacebar was pressed in the last 8 seconds, check for collisions with the notes.
				if (dance){
					for (noteExplosion i:notePattern){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
					for (noteExplosion i:notePattern2){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
					for (noteExplosion i:notePattern3){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
				}
			}
		return 1;
	}
	
	public void bassClefINIT2(){
		for (int i=0;i<16;i++){
			bassLine2[i] = new bclef(i*64,-70);
		}
		bassLine2[(int)(Math.random()*16)]=new bclef(-60, -110);  //Each potentially removes 1 from the 16 row.
		bassLine2[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine2[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine2[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine2[(int)(Math.random()*16)]=new bclef(-60, -110);
	}
	
	//Changes the position of bassClef and checks collision
	public int bassClefPattern2(Rectangle player){
		for (bclef a: bassLine2)
			if (!drawStopTclef(a.newBass(a.x,a.y))){
				a.y+=3;
					
						//Checking Collision with bass clef
				if (player.intersects(a.newBass(a.x, a.y))){
					return 0;
				}
				
				if (dance){
					for (noteExplosion i:notePattern){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
					for (noteExplosion i:notePattern2){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
					for (noteExplosion i:notePattern3){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
				}
			}
		return 1;
	}
	
	public void bassClefINIT3(){
		for (int i=0;i<16;i++){
			bassLine3[i] = new bclef(i*64,-70);
		}
		bassLine3[(int)(Math.random()*16)]=new bclef(-60, -110);  //Each potentially removes 1 from the 16 row.
		bassLine3[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine3[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine3[(int)(Math.random()*16)]=new bclef(-60, -110);
		bassLine3[(int)(Math.random()*16)]=new bclef(-60, -110);
	}
	
	//Changes the position of bassClef and checks collision
	public int bassClefPattern3(Rectangle player){
		for (bclef a: bassLine3)
			if (!drawStopTclef(a.newBass(a.x,a.y))){
				a.y+=3;
					
						//Checking Collision with bass clef
				if (player.intersects(a.newBass(a.x, a.y))){
					return 0;
				}
				
				
				if (dance){
					for (noteExplosion i:notePattern){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
					for (noteExplosion i:notePattern2){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
					for (noteExplosion i:notePattern3){
						if (a.newBass(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.x=-100;
						}
					}
				}
			}
		return 1;
	}
	
	
	//Initalizes a trebleclef array, randomizes the side the treble clef comes from.
	public void trebleSPatternINIT(){
		randomGen1 = (int)(Math.random()*2);
		System.out.println(randomGen1);
		if (randomGen1==0){
			for (int i=0;i<8;i++){
				trebleLine[i] = new tclef(-50,i*90);
			}
			trebleLine[(int)(Math.random()*8)]=new tclef(-50, -100);  //Each potentially removes 1 from the 8 row.
			trebleLine[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLine[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLine[(int)(Math.random()*8)]=new tclef(-50, -100);
			currentLine = trebleLine;
		}else{
			for (int i=0;i<8;i++){
				trebleLineRight[i] = new tclef(1074,i*90);
			}
			trebleLineRight[(int)(Math.random()*8)]=new tclef(-50, -100);  //Each potentially removes 1 from the 8 row.
			trebleLineRight[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLineRight[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLineRight[(int)(Math.random()*8)]=new tclef(-50, -100);
			currentLine = trebleLineRight;
		}
	}
	
	public void trebleSPatternINIT2(){
		randomGen2 = (int)(Math.random()*2);
		if (randomGen2==0){
			for (int i=0;i<8;i++){
				trebleLine2[i] = new tclef(-50,i*90);
			}
			trebleLine2[(int)(Math.random()*8)]=new tclef(-50, -100);  //Each removes 1 from the 8 row.
			trebleLine2[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLine2[(int)(Math.random()*8)]=new tclef(-50, -100);
			currentLine2 = trebleLine2;
		}else{
			for (int i=0;i<8;i++){
				trebleLineRight2[i] = new tclef(1074,i*90);
			}
			trebleLineRight2[(int)(Math.random()*8)]=new tclef(-50, -100);  //Each removes 1 from the 8 row.
			trebleLineRight2[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLineRight2[(int)(Math.random()*8)]=new tclef(-50, -100);
			currentLine2 = trebleLineRight2;
		}
	}
	
	
	//changes the position of each treble clef, as well as checking intersection
	public int trebleSPattern(Rectangle player){
		
		if (randomGen1==0){
			for (tclef a: trebleLine)
				if (!drawStopTclef(a.newTreble(a.x,a.y))){
					a.x+=3;

					//Checking Collision with treble clef
					if (player.intersects(a.newTreble(a.x, a.y))){
						return 0;
					}
					if (dance){
						for (noteExplosion i:notePattern){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern2){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern3){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
					}
				}
			return 1;
	}else{
		for (tclef a: trebleLineRight)
			if (!drawStopTclef(a.newTreble(a.x,a.y))){
				a.x-=3;
					
						//Checking Collision with treble clef
				if (player.intersects(a.newTreble(a.x, a.y))){
					return 0;
				}
				
				if (dance){
					for (noteExplosion i:notePattern){
						if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.y=-100;
						}
					}
					for (noteExplosion i:notePattern2){
						if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.y=-100;
						}
					}
					for (noteExplosion i:notePattern3){
						if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
							a.y=-100;
						}
					}
				}
				}
		return 1;
	}
	}
	
	//Changes and checks position of each treble clef as well as checks intersections
	public int trebleSPattern2(Rectangle player){
		
		if (randomGen2==0){
			for (tclef a: trebleLine2)
				if (!drawStopTclef(a.newTreble(a.x,a.y))){
					a.x+=3;
						
							//Checking Collision with treble clef
					if (player.intersects(a.newTreble(a.x, a.y))){
						return 0;
					}
					
					if (dance){
						for (noteExplosion i:notePattern){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern2){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x,i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern3){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
					}
				}
			
			return 1;
		}else{
			for (tclef a: trebleLineRight2)
				if (!drawStopTclef(a.newTreble(a.x,a.y))){
					a.x-=3;
						
							//Checking Collision with treble clef
					if (player.intersects(a.newTreble(a.x, a.y))){
						return 0;
					}
					
					if (dance){
						for (noteExplosion i:notePattern){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern2){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern3){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
					}
					
					}
			return 1;
		}
		}
	
	
	public void trebleSPatternINIT3(){
		randomGen3 = (int)(Math.random()*2);
		if (randomGen3==0){
			for (int i=0;i<8;i++){
				trebleLine3[i] = new tclef(-50,i*90);
			}
			trebleLine3[(int)(Math.random()*8)]=new tclef(-50, -100);  //Each potentially removes 1 from the 8 row.
			trebleLine3[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLine3[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLine3[(int)(Math.random()*8)]=new tclef(-50, -100);
			currentLine3 = trebleLine3;
		}else{
			for (int i=0;i<8;i++){
				trebleLineRight3[i] = new tclef(1074,i*90);
			}
			trebleLineRight3[(int)(Math.random()*8)]=new tclef(-50, -100);  //Each potentially removes 1 from the 8 row.
			trebleLineRight3[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLineRight3[(int)(Math.random()*8)]=new tclef(-50, -100);
			trebleLineRight3[(int)(Math.random()*8)]=new tclef(-50, -100);
			currentLine3 = trebleLineRight3;
		}
	}
	
	
public int trebleSPattern3(Rectangle player){
		if (randomGen3==0){
			for (tclef a: trebleLine3)
				if (!drawStopTclef(a.newTreble(a.x,a.y))){
					a.x+=3;
						
							//Checking Collision with treble clef
					if (player.intersects(a.newTreble(a.x, a.y))){
						return 0;
					}
					
					if (dance){
						for (noteExplosion i:notePattern){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern2){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x,i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern3){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
					}
				}
			
			return 1;
		}else{
			for (tclef a: trebleLineRight3)
				if (!drawStopTclef(a.newTreble(a.x,a.y))){
					a.x-=3;
						
							//Checking Collision with treble clef
					if (player.intersects(a.newTreble(a.x, a.y))){
						return 0;
					}
					
					if (dance){
						for (noteExplosion i:notePattern){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern2){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
						for (noteExplosion i:notePattern3){
							if (a.newTreble(a.x, a.y).intersects(i.newNote(i.x, i.y))){
								a.y=-100;
							}
						}
					}
					
					}
			return 1;
		}
		}
	
	
	public boolean drawStopTclef(Rectangle r){  //Stops the drawing of sprites that excede the border.
		if (r.x>1200 || r.y>850){
			return true;
		}
		return false;
	}
	
	//Initializes an array of noteExplosions
	public void noteExplosionINIT(){
		for (int i=0; i<8; i++){
			notePattern[i] = new noteExplosion(x-25,y-90);
		}
	}
	
	//Changes each individual note's x/y values accordingly
	public void noteExplosionPattern(){
		notePattern[0].y-=3;//up
		
		notePattern[1].y-=2;//Up and right
		notePattern[1].x+=2;
		
		notePattern[2].x+=3;//right
		
		notePattern[3].y+=2;// down and right
		notePattern[3].x+=2;
		
		notePattern[4].y+=3;//down
		
		notePattern[5].y+=2;//down and left
		notePattern[5].x-=2;
		
		notePattern[6].x-=3;//left
		
		notePattern[7].y-=2;//up and left
		notePattern[7].x-=2;
	}
	
	public void noteExplosionINIT2(){
		for (int i=0; i<8; i++){
			notePattern2[i] = new noteExplosion(x-25,y-90);
		}
	}
	
	public void noteExplosionPattern2(){
		notePattern2[0].y-=3;//up
		
		notePattern2[1].y-=2;//Up and right
		notePattern2[1].x+=2;
		
		notePattern2[2].x+=3;//right
		
		notePattern2[3].y+=2;// down and right
		notePattern2[3].x+=2;
		
		notePattern2[4].y+=3;//down
		
		notePattern2[5].y+=2;//down and left
		notePattern2[5].x-=2;
		
		notePattern2[6].x-=3;//left
		
		notePattern2[7].y-=2;//up and left
		notePattern2[7].x-=2;
		
	}
	
	public void noteExplosionINIT3(){
		for (int i=0; i<8; i++){
			notePattern3[i] = new noteExplosion(x-25,y-90);
		}
	}
	
	public void noteExplosionPattern3(){
		notePattern3[0].y-=3;//up
		
		notePattern3[1].y-=2;//Up and right
		notePattern3[1].x+=2;
		
		notePattern3[2].x+=3;//right
		
		notePattern3[3].y+=2;// down and right
		notePattern3[3].x+=2;
		
		notePattern3[4].y+=3;//down
		
		notePattern3[5].y+=2;//down and left
		notePattern3[5].x-=2;
		
		notePattern3[6].x-=3;//left
		
		notePattern3[7].y-=2;//up and left
		notePattern3[7].x-=2;
		
	}
	
	public void noteExplosionEnemyINIT(int a, int b){
		for (int i=0; i<16; i++){
			noteEnemy[i] = new noteExplosionEnemy(a,b);
		}
	}
	
	public int noteExplosionEnemyPattern(Rectangle player){
		noteEnemy[0].y-=2;//up
		
		noteEnemy[1].y-=2;//up and right a little
		noteEnemy[1].x+=1;
		
		noteEnemy[2].y-=1;
		noteEnemy[2].x+=1;//up and right
		
		noteEnemy[3].y-=1;//up a little and right
		noteEnemy[3].x+=2;
		
		noteEnemy[4].x+=2;//right
		
		noteEnemy[5].y+=1;//right and down a little
		noteEnemy[5].x+=2;
		
		noteEnemy[6].y+=1;//right and down
		noteEnemy[6].x+=1;
		
		noteEnemy[7].y+=2;//right a little and down
		noteEnemy[7].x+=1;
		
		noteEnemy[8].y+=2;//down
		
		noteEnemy[9].y+=2;//down and left a little
		noteEnemy[9].x-=1;
		
		noteEnemy[10].y+=1;//down and left
		noteEnemy[10].x-=1;
		
		noteEnemy[11].y+=1;//down a little and left
		noteEnemy[11].x-=2;
		
		noteEnemy[12].x-=2;//left
		
		noteEnemy[13].x-=2;//left and up a little
		noteEnemy[13].y-=1;
		
		noteEnemy[14].x-=1;//left and up
		noteEnemy[14].y-=1;
		
		noteEnemy[15].x-=1;//left a little and up
		noteEnemy[15].y-=2;	
		
		for (noteExplosionEnemy a:noteEnemy){
			if (player.intersects(a.newNote(a.x, a.y))){
				return 0;
			}
			
			if (dance){
				for (noteExplosion i:notePattern){
					if (a.newNote(a.x, a.y).intersects(i.newNote(i.x, i.y))){
						a.x=-1000;
					}
				}
				for (noteExplosion i:notePattern2){
					if (a.newNote(a.x, a.y).intersects(i.newNote(i.x, i.y))){
						a.x=-1000;
					}
				}
				for (noteExplosion i:notePattern3){
					if (a.newNote(a.x, a.y).intersects(i.newNote(i.x, i.y))){
						a.x=-1000;
					}
				}
			}
			
		}
		return 1;
	}
	
	
	
}


//Treble clef enemy class
class tclef extends JPanel{
	BufferedImage trebleClef;
	int x,y;
	public tclef( int a, int b ){
		try{
			trebleClef = ImageIO.read(new File("Images\\trebleClef.png"));
		}catch (IOException e){
			e.printStackTrace();
		}
		x = a;
		y = b;
	}
	
	public Rectangle newTreble(int x,int y){
		return new Rectangle(x+6,y+6,27,77);
	}
	
}

//Bass clef enemy class
class bclef extends JPanel{
	BufferedImage bassClef;
	int x,y;
	public bclef( int a, int b ){
		try{
			bassClef = ImageIO.read(new File("Images\\bassClef.png"));
		}catch (IOException e){
			e.printStackTrace();
		}
		x = a;
		y = b;
	}
	
	public Rectangle newBass(int x,int y){
		return new Rectangle(x+6,y+6,42,52);
	}
	
}


//Note explosion class
class noteExplosion extends JPanel{
	BufferedImage note;
	int x,y;
	public noteExplosion( int a, int b ){
		try{
			note = ImageIO.read(new File("Images\\EighthNote.png"));
		}catch (IOException e){
			e.printStackTrace();
		}
		x = a;
		y = b;
	}
	
	public Rectangle newNote(int x,int y){
		return new Rectangle(x+10,y+10,67,123);
	}
}

class noteExplosionEnemy extends JPanel{
	BufferedImage note;
	int x,y;
	public noteExplosionEnemy( int a, int b ){
		try{
			note = ImageIO.read(new File("Images\\wholeNote.png"));
		}catch (IOException e){
			e.printStackTrace();
		}
		x = a;
		y = b;
	}
	
	public Rectangle newNote(int x,int y){
		return new Rectangle(x,y,26,16);
	}
}