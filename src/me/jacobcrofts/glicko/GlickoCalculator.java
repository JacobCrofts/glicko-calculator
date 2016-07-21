package me.jacobcrofts.glicko;

import java.util.ArrayList;
import java.util.Iterator;

public final class GlickoCalculator {
	
	private static final double q = Math.log(10) / 400;
	
	private GlickoCalculator() {}
	
	public static int getNewRating(GlickoCompetitor player) {
		
		int rating = player.getRating();
		double deviation = player.getRatingDeviation();
		
		ArrayList<Double> outcomes = player.getProfile().getOutcomes();
		ArrayList<GlickoCompetitor> opponents = player.getProfile().getOpponents();
		
		return getNewRating(rating, deviation, opponents, outcomes);
		
	}
	
	public static double getNewRatingDeviation(GlickoCompetitor player) {
		
		int rating = player.getRating();
		double deviation = player.getRatingDeviation();
		
		ArrayList<GlickoCompetitor> opponents = player.getProfile().getOpponents();
		
		return getNewRatingDeviation(rating, deviation, opponents);
		
	}
	
	private static double dSquared(int rating, ArrayList<GlickoCompetitor> opponents) {
		double sum = 0;
		
		Iterator<GlickoCompetitor> it = opponents.iterator();
		while (it.hasNext()) {
			GlickoCompetitor opponent = it.next();
			double g = g(opponent.getRatingDeviation());
			double e = E(rating, opponent.getRating(), opponent.getRatingDeviation());
			sum += g * g * e * (1 - e);
		}
		
		return 1 / (q * q * sum);
	}
	
	private static double E(int playerRating, int opponentRating, double opponentRatingDeviation) {
		return 1 / (1 + Math.pow(10, g(opponentRatingDeviation) * (playerRating - opponentRating) / -400));
	}
	
	private static double g(double ratingDeviation) {
		return 1 / (Math.sqrt(1 + (3 * q * q * ratingDeviation * ratingDeviation) / (Math.PI * Math.PI)));
	}
	
	private static double getNewRatingDeviation(int rating, double ratingDeviation, ArrayList<GlickoCompetitor> opponents) {
		return Math.sqrt(1 / ((1 / Math.pow(ratingDeviation, 2)) + (1 / dSquared(rating, opponents))));
	}
	
	private static int getNewRating(int originalRating, double ratingDeviation, ArrayList<GlickoCompetitor> opponentData, ArrayList<Double> outcomes) {
				
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
			sum += (g(opponent.getRatingDeviation()) * (outcome - E(originalRating, opponent.getRating(), opponent.getRatingDeviation())));
		}
		
		double prefix = q / ((1 / Math.pow(ratingDeviation, 2)) + (1 / (dSquared(originalRating, opponentData))));
		
		return (originalRating + Math.round((float) (prefix * sum)));
	}

}
