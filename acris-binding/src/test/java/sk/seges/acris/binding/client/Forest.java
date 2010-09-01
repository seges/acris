/**
 * 
 */
package sk.seges.acris.binding.client;

public class Forest {
	private boolean perilLevel;
	private Integer tigersInVicinity;
	private Integer lionsInVicinity;
	private Double fightProbability;
	private String description;

	public boolean isPerilLevel() {
		return perilLevel;
	}

	public void setPerilLevel(boolean perilLevel) {
		this.perilLevel = perilLevel;
	}

	public Integer getTigersInVicinity() {
		return tigersInVicinity;
	}

	public void setTigersInVicinity(Integer tigersInVicinity) {
		this.tigersInVicinity = tigersInVicinity;
	}

	public Integer getLionsInVicinity() {
		return lionsInVicinity;
	}

	public void setLionsInVicinity(Integer lionsInVicinity) {
		this.lionsInVicinity = lionsInVicinity;
	}

	public Double getFightProbability() {
		return fightProbability;
	}

	public void setFightProbability(Double fightProbability) {
		this.fightProbability = fightProbability;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}