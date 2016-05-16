package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Barra
{

	public int barraNumber;

	public int x, y, largura = 30, altura = 200;

	public int score;

	public Barra(Pong pong, int barraNumber)
	{
		this.barraNumber = barraNumber;

		if (barraNumber == 1)
		{
			this.x = 0;
		}

		if (barraNumber == 2)
		{
			this.x = pong.largura - largura;
		}

		this.y = pong.altura / 2 - this.altura / 2;
	}

	public void render(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillRect(x, y, largura, altura);
	}

	public void move(boolean up)
	{
		int speed = 15;

		if (up)
		{
			if (y - speed > 0)
			{
				y -= speed;
			}
			else
			{
				y = 0;
			}
		}
		else
		{
			if (y + altura + speed < Pong.pong.altura)
			{
				y += speed;
			}
			else
			{
				y = Pong.pong.altura - altura;
			}
		}
	}

}
