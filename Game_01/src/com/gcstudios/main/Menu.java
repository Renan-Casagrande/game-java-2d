package com.gcstudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.gcstudios.world.World;

public class Menu {
	
	public String[] options = {"novo jogo", "carregar jogo", "sair"};

	public int currentOption = 0;
	public int maxOption = options.length -1;
	
	public boolean up,down,enter;
	public static boolean pause = false;
	
	public static boolean saveExists = false;
	public static boolean saveGame = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
		if(up) {
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if(enter) {
			enter = false;
			if(options[currentOption].equals("novo jogo") || options[currentOption].equals("continuar")) {
				Game.gameState = "NORMAL";
				pause = false;
				file = new File("save.txt");
				file.delete();
			}else if(options[currentOption].equals("carregar jogo")) {
				file = new File("save.txt");
				if(file.exists()) {
					String saver = loadGame(10);
					applySave(saver);
				}
			}else if(options[currentOption].equals("sair")) {
				System.exit(1);
			}
		}
	}
	
	public static void applySave(String str) {
		int level = 1;
		int life = 100;
		
		String[] spl = str.split("/");		
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			
			if(spl2[0].equals("level")) {
	            level = Integer.parseInt(spl2[1]);
	        } 
	        else if(spl2[0].equals("vida")) {
	            life = Integer.parseInt(spl2[1]);
	        }
	    }
		
		Game.CUR_LEVEL = level;

	    // 🔹 recria o mundo e o player
	    World.restartGame("level"+level+".png");

	    // 🔹 aplica a vida DaEPOIS
	    Game.player.life = life;

	    Game.gameState = "NORMAL";
	    pause = false;		
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try{
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i]-=encode;
							trans[1]+=val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		
		return line;
	}
	
	public static void saveGame(String[] val1 , int[] val2, int encode){
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n ++) {
				value[n]+=encode;
				current +=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1)
				write.newLine();
			}catch(IOException e) {}
		}
		
		try {
			write.flush();
			write.close();			
		}catch(IOException e) {}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 =  (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,36));
		g.drawString(">Game para estudo<", (Game.WIDTH*Game.SCALE) / 2 - 170, (Game.HEIGHT*Game.SCALE) / 2 - 160);
		
		//Opcoes de menu
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,24));
		if(pause == false)
			g.drawString("Novo jogo", (Game.WIDTH*Game.SCALE) / 2 - 50, 160);
		else
			g.drawString("Resumir", (Game.WIDTH*Game.SCALE) / 2 - 40, 160);
		g.drawString("Carregar jogo", (Game.WIDTH*Game.SCALE) / 2 - 70, 200);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) / 2 - 15, 240);
		
		if(options[currentOption].equals("novo jogo")) {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 80, 160);
		}else if(options[currentOption].equals("carregar jogo")) {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 100, 200);
		}else if(options[currentOption].equals("sair")) {
			g.drawString(">", (Game.WIDTH*Game.SCALE) / 2 - 40, 240);
		}		
	}
}
