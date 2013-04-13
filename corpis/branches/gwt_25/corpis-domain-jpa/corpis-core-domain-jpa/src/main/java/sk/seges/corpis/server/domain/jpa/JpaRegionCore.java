package sk.seges.corpis.server.domain.jpa;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

import sk.seges.corpis.server.domain.server.model.base.RegionCoreBase;

@MappedSuperclass
@SequenceGenerator(name = JpaRegionCore.SEQ_REGIONS, sequenceName = "seq_web_id_aware_regions", initialValue = 1)
public class JpaRegionCore extends RegionCoreBase {

	private static final long serialVersionUID = 5925648057837515258L;
	
	protected static final String SEQ_REGIONS = "seqWebIDAwareRegions";

	@Override
	@Column
	public String getDescription() {
		return super.getDescription();
	}

	@Override
	@Column
	public String getName() {
		return super.getName();
	}

	@Override
	@Id
	@GeneratedValue(generator = SEQ_REGIONS)
	public Long getId() {
		return super.getId();
	}

	
}
