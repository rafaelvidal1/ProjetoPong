package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Bola
{

	public int x, y, largura = 25, altura = 25;

	public int deslocX, deslocY;

	public Random random;

	private Pong pong;

	public int amountOfHits;

	public Bola(Pong pong)
	{
		this.pong = pong;

		this.random = new Random();

		spawn();
	}

	public void update(Barra barra1, Barra barra2)
	{
		int veloc = 5;

		this.x += deslocX * veloc;
		this.y += deslocY * veloc;

		if (this.y + altura - deslocY > pong.altura || this.y + deslocY < 0)
		{
			if (this.deslocY < 0)
			{
				this.y = 0;
				this.deslocY = random.nextInt(4);

				if (deslocY == 0)
				{
					deslocY = 1;
				}
			}
			else
			{
				this.deslocY = -random.nextInt(4);
				this.y = pong.altura - altura;

				if (deslocY == 0)
				{
					deslocY = -1;
				}
			}
		}

		if (checkCollision(barra1) == 1)
		{
			this.deslocX = 1 + (amountOfHits / 5);
			this.deslocY = -2 + random.nextInt(4);

			if (deslocY == 0)
			{
				deslocY = 1;
			}

			amountOfHits++;
		}
		else if (checkCollision(barra2) == 1)
		{
			this.deslocX = -1 - (amountOfHits / 5);
			this.deslocY = -2 + random.nextInt(4);

			if (deslocY == 0)
			{
				deslocY = 1;
			}

			amountOfHits++;
		}

		if (checkCollision(barra1) == 2)
		{
			barra2.score++;
			spawn();
		}
		else if (checkCollision(barra2) == 2)
		{
			barra1.score++;
			spawn();
		}
	}

	public void spawn()
	{
		this.amountOfHits = 0;
		this.x = pong.largura / 2 - this.largura / 2;
		this.y = pong.altura / 2 - this.altura / 2;

		this.deslocY = -2 + random.nextInt(4);

		if (deslocY == 0)
		{
			deslocY = 1;
		}

		if (random.nextBoolean())
		{
			deslocX = 1;
		}
		else
		{
			deslocX = -1;
		}
	}

	public int checkCollision(Barra barra)
	{
		if (this.x < barra.x + barra.largura && this.x + largura > barra.x && this.y < barra.y + barra.altura && this.y + altura > barra.y)
		{
			return 1;               //resultado
		}
		else if ((barra.x > x && barra.barraNumber == 1) || (barra.x < x - largura && barra.barraNumber == 2))
		{
			return 2;               //placar
		}

		return 0;                   //nada
	}

	public void render(Graphics g)
	{
		g.setColor(Color.WHITE);
		g.fillOval(x, y, largura, altura);
	}

}
