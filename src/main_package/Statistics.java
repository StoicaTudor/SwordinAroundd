package main_package;

public class Statistics {
	
	private int victories;
	private int looses;
	private int totalKills;
	
	public Statistics(){
		
		this.victories = this.looses = this.totalKills = 0;
	}
	
	public int getTotalGames() {
		return victories+looses;
	}
	
	public double getAverageKills() {
		
		return (totalKills/getTotalGames());
	}
}
