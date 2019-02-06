package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.List;
import java.util.Random;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.Models.AsdRecords;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;


public class BundlingServiceImpl implements BundlingService {

	@Override
	public AdvisorySituationBundle bundle(List<AdvisorySituationData> asd, Query query) {
		AdvisorySituationBundle bundle = new AdvisorySituationBundle();
		AsdRecords recs = new AsdRecords();
		//recs.setAdvisoryBroadcast(MessageCreator.extractTimsAndGenerateBroadcasts(asd).toArray());
			
		bundle.setAsdRecords(recs);
		bundle.setBundleId(String.format("%08X", new Random().nextInt()));
		bundle.setBundleNumber("1");
			
		return bundle;
	} 
	
	
	
	@Override
	public AdvisorySituationDataDistribution distribution(List<AdvisorySituationData> asd, Query query) {
		return null;
	}

}