
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


class bgHolder extends JPanel{
	Scanner myInput;
	String idols;
	Image menu[] = new Image[5];
	Image currentImage;
	double endTime;
	Font luc = new Font("Lucida Sans Unicode", Font.PLAIN, 24);
	boolean finishLose = false, finishWin = false,hiscoresPage = false;;
	Image start,instructions,hiscores,about,aboutENTER,exit,instructionsENTER,gameOver,win,hiscoresENTER;
	bgHolder(){
		setPreferredSize(new Dimension(1024,720));
		try{
			start = ImageIO.read(new File("Images\\DigitusForteTitle(Start).jpg"));					//Importing all images
		    instructions = ImageIO.read(new File("Images\\DigitusForteTitle(Instructions).jpg"));
		    instructionsENTER =ImageIO.read(new File("Images\\Instructions.jpg"));
		    hiscores = ImageIO.read(new File("Images\\DigitusForteTitle(Hiscores).jpg"));
		    about = ImageIO.read(new File("Images\\DigitusForteTitle(About).jpg"));
		    aboutENTER =ImageIO.read(new File("Images\\About.jpg"));
		    exit = ImageIO.read(new File("Images\\DigitusForteTitle(Exit).jpg"));
		    gameOver = ImageIO.read(new File("Images\\GameOver.jpg"));
		    win = ImageIO.read(new File("Images\\Win.jpg"));
		    hiscoresENTER = ImageIO.read(new File("Images\\Hiscores.jpg"));
		    myInput = new Scanner(new File("Highscores\\Idols.txt"));
		    idols = myInput.nextLine();
		}catch(Exception e){
		}				//Assigning images to each index in the array
		menu[0]=start;
	    menu[1]=instructions;
	    menu[2]=hiscores;
	    menu[3]=about;
	    menu[4]=exit;
	    currentImage = menu[0];
	}


	//Painting
	public void paintComponent(Graphics g){
		g.drawImage(currentImage,0,0,this);
		
		
		
		g.setFont(luc);
		
		if (finishLose){
			g.setColor(Color.white);
			g.drawString(endTime+"",700,635);
			if (endTime>Integer.parseInt(idols)){
				g.drawString(endTime+"", 700, 678);
			}else{
				g.drawString(idols, 700, 678);
			}
		}else if(finishWin){
			g.setColor(Color.white);
			g.drawString(endTime+"",770,529);
			if (endTime>Integer.parseInt(idols)){
				g.drawString(endTime+"", 770, 583);
			}else{
				g.drawString(idols,770,583);
			}
		}
		
		
		if (hiscoresPage){
			g.setColor(Color.black);
			g.drawString(idols,170,690);
		}
	}
}

public class navigation extends JFrame implements KeyListener{
	Image start,instructions,hiscores,about,aboutENTER,exit,instructionsENTER;
	JLabel menu[] = new JLabel[5];
	Clip clip;
	bgHolder navPanel = new bgHolder();
	boolean isPressedENTER = false, notMain=false;
	int menuIndex = 0;
	PrintWriter hiWrite;
	navigation(String title) throws IOException{
		
		//Puts string entered as title
		super(title);
		
		//Layout format set to BoxLayout but doesnt really matter cause theres only one panel
		setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));
		
		//adding main panel to frame
	    add(navPanel);
	    
	    //KeyListener declaration
	    addKeyListener(this);
	    
	    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE ); 
	}
	
	
	//sound
	public void LoopSound() throws Exception {
        File bgMenu = new File("Music\\Menu.wav");
        clip = AudioSystem.getClip();
        AudioInputStream yes = AudioSystem.getAudioInputStream( bgMenu );
        clip.open(yes);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
	
	
	public void run() throws Exception{
		while (true){
			navPanel.repaint();
			
			if (isPressedENTER){
				if (menuIndex==0){
					clip.close();
	        		this.setVisible(false);
	        		JFrame f = new JFrame();
	        	    StartGame game = new StartGame("Digitus Forte");
	        	    f.add(game);
	        	    f.pack();
	        	    f.setResizable(false);
	        	    f.setVisible( true );
	        	    f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	        	    navPanel.endTime = Math.round(game.run()*100)/100;
	        	    LoopSound();
	        	    if (navPanel.endTime<60){
	        	    	navPanel.currentImage = navPanel.gameOver;
	        	    	navPanel.finishLose = true;
	        	    }else if(navPanel.endTime>=60){
	        	    	navPanel.currentImage = navPanel.win;
	        	    	navPanel.finishWin = true;
	        	    }
	        	    
	        	    if (navPanel.endTime>Integer.parseInt(navPanel.idols)){
	        	    	hiWrite  = new PrintWriter("Highscores\\Idols.txt", "UTF-8");
	        	    	hiWrite.print((int)navPanel.endTime+"");
	        	    	hiWrite.close();
	        	    }
	        	    
	        	    this.setVisible(true);
	        	    f.setVisible(false);
	        	    f.dispose();
	        	    notMain = true;
	        	    isPressedENTER=false;
	        	    
	        	    
	        	    removeKeyListener(this);		//Temporarily disables keylistener to allow some downtime right after death.
	        	    try{
	    				Thread.sleep(2000);
	    			}catch(InterruptedException e){
	    			}
	    			addKeyListener(this);
	        	    
	        	   
	        	}
	        	else if(menuIndex==1){//instructions
	        		navPanel.currentImage = navPanel.instructionsENTER;
	        		notMain = true;
	        		isPressedENTER=false;
	        	}
	        	else if(menuIndex==2){//hiscores
	        		navPanel.currentImage = navPanel.hiscoresENTER;
	        		navPanel.hiscoresPage = true;
	        		navPanel.myInput = new Scanner(new File("Highscores\\Idols.txt"));
	        		navPanel.idols = navPanel.myInput.nextLine();
	        		notMain=true;
	        		isPressedENTER=false;
	        	}
	        	else if(menuIndex==3){//about
	        		navPanel.currentImage = navPanel.aboutENTER;
	        		notMain = true;
	        		isPressedENTER=false;
	        	}
	        	else{//exit
	        		setVisible(false);
	        		dispose();
	        		break;
	        	}
			}
		}
	}
	
	public void keyPressed(KeyEvent e) {
    }
	
	public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    	
    	//Down Arrow lift
        if(e.getKeyCode()== KeyEvent.VK_DOWN && notMain==false){
        	
        	
        	//If the menu index is at the last, re-loop to first, otherwise go to next JLabel
        	if (menuIndex==4)
        		menuIndex=0;
        	else
        		menuIndex++;
        	
        	//Set the selected Jlabel to visible
          navPanel.currentImage = navPanel.menu[menuIndex];
            
        }
        
        //Up arrow lift
        else if(e.getKeyCode()== KeyEvent.VK_UP && notMain==false){
        	if (menuIndex==0)
        		menuIndex=4;
        	else
        		menuIndex--;
        	navPanel.currentImage = navPanel.menu[menuIndex];
        }
        
        else if(e.getKeyCode()==KeyEvent.VK_ENTER && notMain==false){
        	isPressedENTER = true;
        }
         
        
        if (notMain){
        	navPanel.finishLose = false;
        	navPanel.finishWin = false;
        	navPanel.hiscoresPage = false;
        	navPanel.currentImage = navPanel.menu[0];
        	notMain=false;
        	menuIndex=0;
    	}
	
    }




	public static void main(String args[]) throws Exception{
		navigation titlescreen = new navigation("Digitus Forte");
		titlescreen.LoopSound();
		titlescreen.setResizable(false);
		titlescreen.pack();
	    titlescreen.setVisible( true );
	    titlescreen.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
	    titlescreen.run();
	    
	}
}
