package me.jacobcrofts.glicko;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class GlickoMatchSet {
	
	private static final double q = Math.log(10) / 400;
	
	private List<GlickoMatch> matches;
	
	public GlickoMatchSet() {
		this.matches = new ArrayList<GlickoMatch>();
	}
	
	public void addMatch(GlickoMatch match) {
		this.matches.add(match);
	}
	
	public int getNewRating(GlickoCompetitor player) {
		
		int rating = player.getOriginalRating();
		double deviation = player.getOriginalRatingDeviation();
		
		ArrayList<Double> outcomes = new ArrayList<Double>();
		ArrayList<GlickoCompetitor> opponentData = new ArrayList<GlickoCompetitor>();
//		LinkedHashMap<Integer, Double> opponentData = new LinkedHashMap<Integer, Double>();
		
		for (GlickoMatch match : this.matches) {
			if (match.involves(player)) {
				outcomes.add(match.getOutcome(player).getValue());
				GlickoCompetitor opponent = match.getOpponent(player);
				opponentData.add(opponent);
//				opponentData.put(opponent.getOriginalRating(), opponent.getOriginalRatingDeviation());
			}
		}
		
		return this.getNewRating(rating, deviation, opponentData, outcomes);
		
	}
	
	public double getNewRatingDeviation(GlickoCompetitor player) {
		
		int rating = player.getOriginalRating();
		double deviation = player.getOriginalRatingDeviation();
		
		ArrayList<GlickoCompetitor> opponentData = new ArrayList<GlickoCompetitor>();
		
		for (GlickoMatch match : this.matches) {
			if (match.involves(player)) {
				GlickoCompetitor opponent = match.getOpponent(player);
				opponentData.add(opponent);
			}
		}
		
		return this.getNewRatingDeviation(rating, deviation, opponentData);
		
	}
	
	public void assignNewDataToAllCompetitors() {		
		for (GlickoCompetitor player : this.getCompetitors()) {
			player.setRating(this.getNewRating(player));
			player.setRatingDeviation(this.getNewRatingDeviation(player));
		}
	}
	
	private List<GlickoCompetitor> getCompetitors() {
		List<GlickoCompetitor> competitors = new ArrayList<GlickoCompetitor>();
		for (GlickoMatch match : this.matches) {
			competitors.add(match.getFirstPlayer());
			competitors.add(match.getSecondPlayer());
		}
		return competitors;
	}
	
	private double getNewRatingDeviation(int rating, double ratingDeviation, ArrayList<GlickoCompetitor> opponents) {
		return Math.sqrt(1 / ((1 / Math.pow(ratingDeviation, 2)) + (1 / dSquared(rating, opponents))));
	}
	
	private int getNewRating(int originalRating, double ratingDeviation, ArrayList<GlickoCompetitor> opponentData, ArrayList<Double> outcomes) {
		
//		System.out.println(opponentData.size() + " : " + outcomes.size());
		
		if (opponentData.size() != outcomes.size()) {
			throw new IllegalArgumentException("Invalid opponent data passed to Glicko calculator.");
		}
		
		for (double outcome : outcomes) {
			if (outcome != 1 && outcome != 0.5 && outcome != 0) {
				throw new IllegalArgumentException("Invalid outcome data passed to Glicko calculator.");
			}
		}
		
		double sum = 0;
		
		Iterator<GlickoCompetitor> opponentIterator = opponentData.iterator();
		Iterator<Double> outcomeIterator = outcomes.iterator();
		
		while (opponentIterator.hasNext() && outcomeIterator.hasNext()) {
			GlickoCompetitor opponent = opponentIterator.next();
			double outcome = outcomeIterator.next();
			sum += (g(opponent.getOriginalRatingDeviation()) * (outcome - E(originalRating, opponent.getOriginalRating(), opponent.getOriginalRatingDeviation())));
		}
		
		double prefix = q / ((1 / Math.pow(ratingDeviation, 2)) + (1 / (dSquared(originalRating, opponentData))));
		
		return (originalRating + Math.round((float) (prefix * sum)));
	}
	
	private double dSquared(int rating, ArrayList<GlickoCompetitor> opponents) {
		double sum = 0;
		
		Iterator<GlickoCompetitor> it = opponents.iterator();
		while (it.hasNext()) {
			GlickoCompetitor opponent = it.next();
			double g = g(opponent.getOriginalRatingDeviation());
			double e = E(rating, opponent.getOriginalRating(), opponent.getOriginalRatingDeviation());
			sum += g * g * e * (1 - e);
		}
		
		return 1 / (q * q * sum);
	}
	
	private double E(int playerRating, int opponentRating, double opponentRatingDeviation) {
		return 1 / (1 + Math.pow(10, g(opponentRatingDeviation) * (playerRating - opponentRating) / -400));
	}
	
	private double g(double ratingDeviation) {
		return 1 / (Math.sqrt(1 + (3 * q * q * ratingDeviation * ratingDeviation) / (Math.PI * Math.PI)));
	}

}
