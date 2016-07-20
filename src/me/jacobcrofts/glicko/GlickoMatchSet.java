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
		LinkedHashMap<Integer, Double> opponentData = new LinkedHashMap<Integer, Double>();
		
		for (GlickoMatch match : this.matches) {
			if (match.involves(player)) {
				outcomes.add(match.getOutcome(player).getValue());
				GlickoCompetitor opponent = match.getOpponent(player);
				opponentData.put(opponent.getOriginalRating(), opponent.getOriginalRatingDeviation());
			}
		}
		
		return this.getNewRating(rating, deviation, opponentData, outcomes);
		
	}
	
	public double getNewRatingDeviation(GlickoCompetitor player) {
		
		int rating = player.getOriginalRating();
		double deviation = player.getOriginalRatingDeviation();
		
		LinkedHashMap<Integer, Double> opponentData = new LinkedHashMap<Integer, Double>();
		
		for (GlickoMatch match : this.matches) {
			if (match.involves(player)) {
				GlickoCompetitor opponent = match.getOpponent(player);
				opponentData.put(opponent.getOriginalRating(), opponent.getOriginalRatingDeviation());
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
	
	private double getNewRatingDeviation(int rating, double ratingDeviation, LinkedHashMap<Integer, Double> opponentData) {
		return Math.sqrt(1 / ((1 / Math.pow(ratingDeviation, 2)) + (1 / dSquared(rating, opponentData))));
	}
	
	private int getNewRating(int originalRating, double ratingDeviation, LinkedHashMap<Integer, Double> opponentData, ArrayList<Double> outcomes) {
		
		if (opponentData.size() != outcomes.size()) {
			throw new IllegalArgumentException("Invalid opponent data passed to Glicko calculator.");
		}
		
		for (double outcome : outcomes) {
			if (outcome != 1 && outcome != 0.5 && outcome != 0) {
				throw new IllegalArgumentException("Invalid outcome data passed to Glicko calculator.");
			}
		}
		
		double sum = 0;
		
		Iterator<Entry<Integer, Double>> dataIterator = opponentData.entrySet().iterator();
		Iterator<Double> outcomeIterator = outcomes.iterator();
		
		while (dataIterator.hasNext() && outcomeIterator.hasNext()) {
			Map.Entry<Integer, Double> nextDataPair = dataIterator.next();
			double outcome = outcomeIterator.next();
			sum += (g(nextDataPair.getValue()) * (outcome - E(originalRating, nextDataPair.getKey(), nextDataPair.getValue())));
		}
		
		double prefix = q / ((1 / Math.pow(ratingDeviation, 2)) + (1 / (dSquared(originalRating, opponentData))));
		
		return (originalRating + Math.round((float) (prefix * sum)));
	}
	
	private double dSquared(int rating, Map<Integer, Double> opponentRatingsAndDeviations) {
		double sum = 0;
		
		Iterator<Entry<Integer, Double>> it = opponentRatingsAndDeviations.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Integer, Double> nextPair = it.next();
			double g = g(nextPair.getValue());
			double e = E(rating, nextPair.getKey(), nextPair.getValue());
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
