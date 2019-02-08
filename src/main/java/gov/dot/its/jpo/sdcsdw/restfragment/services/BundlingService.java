package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;

public interface BundlingService {
	public List<AdvisorySituationBundle> createBundleList(List<AdvisorySituationData> asd, Query query);
	public List<AdvisorySituationDataDistribution> createDistributionList(List<AdvisorySituationData> asd, Query query);
}
