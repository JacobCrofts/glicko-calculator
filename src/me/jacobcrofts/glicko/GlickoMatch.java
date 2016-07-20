package me.jacobcrofts.glicko;

public final class GlickoMatch {
	
	private final GlickoCompetitor firstPlayer;
	private final GlickoCompetitor secondPlayer;
	private final GlickoMatchOutcome outcome;
	
	public GlickoMatch(GlickoCompetitor firstPlayer, GlickoCompetitor secondPlayer, GlickoMatchOutcome outcome) {
		this.firstPlayer = firstPlayer;
		this.secondPlayer = secondPlayer;
		this.outcome = outcome;
	}
	
	public GlickoCompetitor getFirstPlayer() {
		return this.firstPlayer;
	}
	
	public GlickoCompetitor getSecondPlayer() {
		return this.secondPlayer;
	}
	
	public GlickoMatchOutcome getOutcome(GlickoCompetitor competitor) {
		
		if (competitor.getID().equals(this.firstPlayer.getID())) {
			return this.outcome;
		}
		
		if (competitor.getID().equals(this.secondPlayer.getID())) {
			return this.outcome.getOpposite();
		}
		
		return null;
		
	}
	
	public boolean involves(GlickoCompetitor player) {
		String id = player.getID();
		return id.equals(this.firstPlayer.getID()) || id.equals(this.secondPlayer.getID());
	}
	
	public GlickoCompetitor getOpponent(GlickoCompetitor player) {
		String id = player.getID();
		
		if (id.equals(this.firstPlayer.getID())) {
			return this.secondPlayer;
		}
		
		if (id.equals(this.secondPlayer.getID())) {
			return this.firstPlayer;
		}
		
		return null;
	}
	
	public enum GlickoMatchOutcome {
		
		WIN(1d),
		LOSS(0d),
		DRAW(0.5);
		
		private final double value;
		
		private GlickoMatchOutcome(double value) {
			this.value = value;
		}
		
		public double getValue() {
			return this.value;
		}
		
		public GlickoMatchOutcome getOpposite() {
			switch (this) {
			case WIN: return LOSS;
			case DRAW: return DRAW;
			default: return WIN;
			}
		}
		
	}

}
