package me.jacobcrofts.glicko;

public final class GlickoCompetitor {
	
	private final String id;
	private final int originalRating;
	private int newRating;
	private final double originalRatingDeviation;
	private double newRatingDeviation;
	
	private static double c = 35;
	
	public static void setDefaultC(double c) {
		GlickoCompetitor.c = c;
	}
	
	public GlickoCompetitor(String id, int rating, double oldRatingDeviation, int timeSinceLastParticipation) {
		this.id = id;
		this.originalRating = rating;
		this.originalRatingDeviation = Math.min(350, Math.sqrt(Math.pow(oldRatingDeviation, 2) + Math.pow(c, 2) * timeSinceLastParticipation));
		// TODO: make sure this works...
	}
	
	public GlickoCompetitor(String id, int rating, double oldRatingDeviation) {
		this.id = id;
		this.originalRating = rating;
		this.originalRatingDeviation = oldRatingDeviation;
	}
	
	public String getID() {
		return this.id;
	}
	
	public int getOriginalRating() {
		return this.originalRating;
	}
	
	public int getNewRating() {
		return this.newRating;
	}
	
	public void setRating(int newRating) {
		this.newRating = newRating;
	}
	
	public double getOriginalRatingDeviation() {
		return this.originalRatingDeviation;
	}
	
	public double getNewRatingDeviation() {
		return this.newRatingDeviation;
	}
	
	public void setRatingDeviation(double newRatingDeviation) {
		this.newRatingDeviation = newRatingDeviation;
	}

}
