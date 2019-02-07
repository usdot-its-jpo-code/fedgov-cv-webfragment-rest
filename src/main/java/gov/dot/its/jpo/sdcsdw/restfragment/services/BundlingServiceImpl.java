package gov.dot.its.jpo.sdcsdw.restfragment.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationBundle;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationData;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistribution;
import gov.dot.its.jpo.sdcsdw.Models.AdvisorySituationDataDistributionList;
import gov.dot.its.jpo.sdcsdw.Models.AsdBundles;
import gov.dot.its.jpo.sdcsdw.Models.DialogID;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.udpdialoghandler.service.MessageCreator;



public class BundlingServiceImpl implements BundlingService {
	@Override
	public List<AdvisorySituationBundle> bundle(List<AdvisorySituationData> asd, Query query) {
		List<AdvisorySituationDataDistribution> distributionList = distribution(asd, query);
		List<AdvisorySituationBundle> bundleList = new ArrayList<AdvisorySituationBundle>();
		
		for (AdvisorySituationDataDistribution distribution : distributionList) {
			AsdBundles bundles = distribution.getAsdBundles();
			bundleList.addAll(Arrays.asList(bundles.getAdvisorySituationBundle()));
		}
		
		return bundleList;
	} 
	
	
	@Override
	public List<AdvisorySituationDataDistribution> distribution(List<AdvisorySituationData> asd, Query query) {
		DialogID dialogIDObject = new DialogID();
		dialogIDObject.setAdvSitDatDist(query.getDialogId());
		AdvisorySituationDataDistributionList distributionListObject = MessageCreator.createAdvisorySituationDataDistributionList(asd, dialogIDObject, "00 00 00 00", "00 00 00 00");
		
		return distributionListObject.getDistributionList();
	}

}
