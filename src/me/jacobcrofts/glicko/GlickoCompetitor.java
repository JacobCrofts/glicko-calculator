package me.jacobcrofts.glicko;

import java.util.ArrayList;

public final class GlickoCompetitor {
	
	private static double c = 35;
	
	public static void setDefaultC(double c) {
		GlickoCompetitor.c = c;
	}
	
	private final String id;
	private final int originalRating;
	private int newRating;
	private final double originalRatingDeviation;
	private double newRatingDeviation;
	private MatchProfile profile;
	
	public GlickoCompetitor(String id, int rating, double oldRatingDeviation, int timeSinceLastParticipation) {
		this.id = id;
		this.originalRating = rating;
		this.originalRatingDeviation = Math.min(350, Math.sqrt(Math.pow(oldRatingDeviation, 2) + Math.pow(c, 2) * timeSinceLastParticipation));
		this.profile = new MatchProfile();
	}
	
	public GlickoCompetitor(String id, int rating, double oldRatingDeviation) {
		this.id = id;
		this.originalRating = rating;
		this.originalRatingDeviation = oldRatingDeviation;
		this.profile = new MatchProfile();
	}
	
	public class MatchProfile {
		
		private ArrayList<GlickoCompetitor> opponents;
		private ArrayList<Double> outcomes;
		
		private MatchProfile() {
			this.opponents = new ArrayList<GlickoCompetitor>();
			this.outcomes = new ArrayList<Double>();
		}
		
		public void registerMatch(GlickoMatch match) {
			this.opponents.add(match.getOpponent(GlickoCompetitor.this));
			this.outcomes.add(match.getOutcome(GlickoCompetitor.this).getValue());
		}
		
		public ArrayList<GlickoCompetitor> getOpponents() {
			return this.opponents;
		}
		
		public ArrayList<Double> getOutcomes() {
			return this.outcomes;
		}
		
	}
	
	public MatchProfile getProfile() {
		return this.profile;
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
