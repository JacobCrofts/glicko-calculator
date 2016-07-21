package me.jacobcrofts.glicko;

import java.util.ArrayList;

public final class GlickoCompetitor {
	
	private static double c = 35;
	
	public static void setDefaultC(double c) {
		GlickoCompetitor.c = c;
	}
	
	private final String id;
	private int rating;
	private double ratingDeviation;
	private MatchProfile profile;
	
	public GlickoCompetitor(String id, int rating, double oldRatingDeviation, int timeSinceLastParticipation) {
		this.id = id;
		this.rating = rating;
		this.ratingDeviation = Math.min(350, Math.sqrt(Math.pow(oldRatingDeviation, 2) + Math.pow(c, 2) * timeSinceLastParticipation));
		this.profile = new MatchProfile();
	}
	
	public GlickoCompetitor(String id, int rating, double oldRatingDeviation) {
		this.id = id;
		this.rating = rating;
		this.ratingDeviation = oldRatingDeviation;
		this.profile = new MatchProfile();
	}
	
	public MatchProfile getProfile() {
		return this.profile;
	}
	
	public String getID() {
		return this.id;
	}
	
	public int getRating() {
		return this.rating;
	}
	
	public void setRating(int newRating) {
		this.rating = newRating;
	}
	
	public double getRatingDeviation() {
		return this.ratingDeviation;
	}
	
	public void setRatingDeviation(double newRatingDeviation) {
		this.ratingDeviation = newRatingDeviation;
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
		
		public void clear() {
			this.opponents.clear();
			this.outcomes.clear();
		}
		
	}

}
