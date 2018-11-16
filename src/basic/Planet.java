package basic;

public class Planet {
	private int radius;
	private int nbSpaceship;
	private int power;
	private Player player;
	private Sprite sprite;
	private  int productionRate;
	
	public Planet(int radius, int nbSpaceship, int power, Player player, Sprite sprite, int productionRate){
		this.radius = radius;
		this.nbSpaceship = nbSpaceship;
		this.power = power;
		this.player = player;
		this.sprite = sprite;
		this.productionRate = productionRate;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public void setRadius(int radius) {
		this.radius = radius;
	}
	
	public int getNbSpaceship() {
		return nbSpaceship;
	}
	
	public void setNbSpaceship(int nbSpaceship) {
		this.nbSpaceship = nbSpaceship;
	}
	
	public int getPower() {
		return power;
	}
	
	public void setPower(int power) {
		this.power = power;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public Sprite getSprite() {
		return sprite;
	}
	
	public void setSprite(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public int getProductionRate() {
		return productionRate;
	}
	
	public void setProductionRate(int productionRate) {
		this.productionRate = productionRate;
	}
	
}
