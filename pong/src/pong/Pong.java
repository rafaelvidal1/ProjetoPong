package pong;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener
{

	public static Pong pong;

	public int largura = 1200, altura = 700;

	public Renderer renderer;

	public Barra jogador1;

	public Barra jogador2;

	public Bola bola;

	public boolean bot = false, dificuldade;

	public boolean r, f, up, down;

	public int gameStatus = 0, scoreLimit = 3, vencedor; //0 = Menu, 1 = Jogo pausado, 2 = Jogo rodando, 3 = fim de jogo

	public int nivel, botMoves, botCooldown = 0;

	public Random random;

	public JFrame jframe;

	public Pong()
	{
		Timer timer = new Timer(20, this);
		random = new Random();

		jframe = new JFrame("Pong");

		renderer = new Renderer();

		jframe.setSize(largura + 15, altura + 35);
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.add(renderer);
		jframe.addKeyListener(this);

		timer.start();
	}

	public void start()
	{
		gameStatus = 2;
		jogador1 = new Barra(this, 1);
		jogador2 = new Barra(this, 2);
		bola = new Bola(this);
	}

	public void update()
	{
		if (jogador1.score >= scoreLimit)
		{
			vencedor = 1;
			gameStatus = 3;
		}

		if (jogador2.score >= scoreLimit)
		{
			gameStatus = 3;
			vencedor = 2;
		}

		if (r)
		{
			jogador1.move(true);
		}
		if (f)
		{
			jogador1.move(false);
		}

		if (!bot)
		{
			if (up)
			{
				jogador2.move(true);
			}
			if (down)
			{
				jogador2.move(false);
			}
		}
		else
		{
			if (botCooldown > 0)
			{
				botCooldown--;

				if (botCooldown == 0)
				{
					botMoves = 0;
				}
			}

			if (botMoves < 10)
			{
				if (jogador2.y + jogador2.altura / 2 < bola.y)
				{
					jogador2.move(false);
					botMoves++;
				}

				if (jogador2.y + jogador2.altura / 2 > bola.y)
				{
					jogador2.move(true);
					botMoves++;
				}

				if (nivel == 0)
				{
					botCooldown = 20;
				}
				if (nivel == 1)
				{
					botCooldown = 15;
				}
				if (nivel == 2)
				{
					botCooldown = 10;
				}
			}
		}

		bola.update(jogador1, jogador2);
	}

	public void render(Graphics2D g)
	{
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, largura, altura);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (gameStatus == 0)
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));

			g.drawString("PONG", largura / 2 - 75, 50);
			g.drawString("T - 18 Campeã da OI 2016!!", largura / 2 - 300, 100);

			if (!dificuldade)
			{
				g.setFont(new Font("Arial", 1, 30));

				g.drawString("Aperte espaço para jogar", largura / 2 - 150, altura / 2 - 25);
				g.drawString("Aperte shift para jogar com a máquina", largura / 2 - 200, altura / 2 + 25);
				g.drawString("Máxima pontuação:" + scoreLimit, largura / 2 - 150, altura / 2 + 75);
			}
		}

		if (dificuldade)
		{
			String string = nivel == 0 ? "Fácil" : (nivel == 1 ? "Médio" : "Difícil");

			g.setFont(new Font("Arial", 1, 30));

			g.drawString("<< Dificuldade: " + string + " >>", largura / 2 - 180, altura / 2 - 25);
			g.drawString("Aperte espaço para jogar", largura / 2 - 150, altura / 2 + 25);
		}

		if (gameStatus == 1)
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("PAUSA", largura / 2 - 103, altura / 2 - 25);
		}

		if (gameStatus == 1 || gameStatus == 2)
		{
			g.setColor(Color.WHITE);

			g.setStroke(new BasicStroke(5f));

			g.drawLine(largura / 2, 0, largura / 2, altura);

			g.setStroke(new BasicStroke(2f));

			g.drawOval(largura / 2 - 150, altura / 2 - 150, 300, 300);

			g.setFont(new Font("Arial", 1, 50));

			g.drawString(String.valueOf(jogador1.score), largura / 2 - 90, 50);
			g.drawString(String.valueOf(jogador2.score), largura / 2 + 65, 50);

			jogador1.render(g);
			jogador2.render(g);
			bola.render(g);
		}

		if (gameStatus == 3)
		{
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));

			g.drawString("PONG", largura / 2 - 75, 50);

			if (bot && vencedor == 2)
			{
				g.drawString("Você perdeu :/", largura / 2 - 170, 200);
			}
			else
			{
				g.drawString("Vitória do jogador " + vencedor, largura / 2 - 165, 200);
			}

			g.setFont(new Font("Arial", 1, 30));

			g.drawString("Aperte espaço para jogar de novo", largura / 2 - 185, altura / 2 - 25);
			g.drawString("Aperte ESC para ir ao Menu", largura / 2 - 140, altura / 2 + 25);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		if (gameStatus == 2)
		{
			update();
		}

		renderer.repaint();
	}

	public static void main(String[] args) {
		pong = new Pong();
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_R)
		{
			r = true;
		}
		else if (id == KeyEvent.VK_F)
		{
			f = true;
		}
		else if (id == KeyEvent.VK_UP)
		{
			up = true;
		}
		else if (id == KeyEvent.VK_DOWN)
		{
			down = true;
		}
		else if (id == KeyEvent.VK_RIGHT)
		{
			if (dificuldade)
			{
				if (nivel < 2)
				{
					nivel++;
				}
				else
				{
					nivel = 0;
				}
			}
			else if (gameStatus == 0)
			{
				scoreLimit++;
			}
		}
		else if (id == KeyEvent.VK_LEFT)
		{
			if (dificuldade)
			{
				if (nivel > 0)
				{
					nivel--;
				}
				else
				{
					nivel = 2;
				}
			}
			else if (gameStatus == 0 && scoreLimit > 1)
			{
				scoreLimit--;
			}
		}
		else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3))
		{
			gameStatus = 0;
		}
		else if (id == KeyEvent.VK_SHIFT && gameStatus == 0)
		{
			bot = true;
			dificuldade = true;
		}
		else if (id == KeyEvent.VK_SPACE)
		{
			if (gameStatus == 0 || gameStatus == 3)
			{
				if (!dificuldade)
				{
					bot = false;
				}
				else
				{
					dificuldade = false;
				}

				start();
			}
			else if (gameStatus == 1)
			{
				gameStatus = 2;
			}
			else if (gameStatus == 2)
			{
				gameStatus = 1;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
		int id = e.getKeyCode();

		if (id == KeyEvent.VK_R)
		{
			r = false;
		}
		else if (id == KeyEvent.VK_F)
		{
			f = false;
		}
		else if (id == KeyEvent.VK_UP)
		{
			up = false;
		}
		else if (id == KeyEvent.VK_DOWN)
		{
			down = false;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e)
	{

	}
}
