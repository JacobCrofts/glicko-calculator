package me.jacobcrofts.glicko;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class GlickoMatchSet {
		
	private List<GlickoMatch> matches;
	
	public GlickoMatchSet() {
		this.matches = new ArrayList<GlickoMatch>();
	}
	
	public void addMatch(GlickoMatch match) {
		this.matches.add(match);
		match.register();
	}
	
	private Set<GlickoCompetitor> getCompetitors() {
		Map<GlickoCompetitor, Boolean> competitors = new HashMap<GlickoCompetitor, Boolean>();
		for (GlickoMatch match : this.matches) {
			competitors.put(match.getFirstPlayer(), true);
			competitors.put(match.getSecondPlayer(), true);
		}
		return competitors.keySet();
	}
	
	public void assignNewDataToAllCompetitors() {
		Map<GlickoCompetitor, Integer> ratingData = new HashMap<GlickoCompetitor, Integer>();
		Map<GlickoCompetitor, Double> deviationData = new HashMap<GlickoCompetitor, Double>();
		for (GlickoCompetitor player : this.getCompetitors()) {
			ratingData.put(player, GlickoCalculator.getNewRating(player));
			deviationData.put(player, GlickoCalculator.getNewRatingDeviation(player));
		}
		for (GlickoCompetitor player : this.getCompetitors()) {
			player.setRating(ratingData.get(player));
			player.setRatingDeviation(deviationData.get(player));
			player.getProfile().clear();
		}
	}

}
