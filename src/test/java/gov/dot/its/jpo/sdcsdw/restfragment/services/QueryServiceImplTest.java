package gov.dot.its.jpo.sdcsdw.restfragment.services;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import gov.dot.its.jpo.sdcsdw.restfragment.config.MongoConfigFileProperty;
import gov.dot.its.jpo.sdcsdw.restfragment.model.Query;
import gov.dot.its.jpo.sdcsdw.restfragment.util.QueryOptions;
import gov.dot.its.jpo.sdcsdw.websocketsfragment.mongo.InvalidQueryException;

public class QueryServiceImplTest {

	private Query setDefaultsQuery;
	private Query validationQuery;
	private QueryServiceImpl impl;
	
	@Before
	public void setup() {
		this.setDefaultsQuery = new Query();
		this.validationQuery = new Query();
		this.impl = new QueryServiceImpl(null);
		validationQuery.setSystemQueryName("SDW 2.3");
		validationQuery.setResultEncoding("hex");
		validationQuery.setResultPackaging("none");
		validationQuery.setOrderByField("none");
		validationQuery.setOrderByOrder("ascending");
		validationQuery.setSkip(0);
		validationQuery.setLimit(0);
		validationQuery.setDialogId("advSitDataDep");
		validationQuery.setNwLat(84.2);
		validationQuery.setNwLon(80.1);
		validationQuery.setSeLat(40.3);
		validationQuery.setSeLon(60.5);
		validationQuery.setStartDate("2014-09-16T14:25:07.609Z");
		validationQuery.setStartDateOperator("GTE");
		validationQuery.setEndDate("2014-09-19T14:45:07.609Z");
		validationQuery.setEndDateOperator("LTE");
	}
	
	//Test defaults are set when initial value is not provided
	@Test
	public void testSetDefaults() {
		this.impl.setDefaults(this.setDefaultsQuery);
		assertEquals(this.setDefaultsQuery.getResultEncoding(), QueryOptions.getDefaultResultEncoding());
		assertEquals(this.setDefaultsQuery.getResultPackaging(), QueryOptions.getDefaultResultPackaging());
		assertEquals(this.setDefaultsQuery.getOrderByField(), QueryOptions.getDefaultOrderByField());
		assertEquals(this.setDefaultsQuery.getOrderByOrder(), QueryOptions.getDefaultOrderByOrder());
		assertEquals(this.setDefaultsQuery.getSkip().intValue(), QueryOptions.getDefaultSkip());
		assertEquals(this.setDefaultsQuery.getLimit().intValue(), QueryOptions.getDefaultLimit());
		assertEquals(this.setDefaultsQuery.getStartDateOperator(), QueryOptions.getDefaultStartDateOperator());
		assertEquals(this.setDefaultsQuery.getEndDateOperator(), QueryOptions.getDefaultEndDateOperator());
	}
	
	//Test valid query
	@Test
	public void testValidateQuery1() throws Exception {
		impl.validateQuery(validationQuery);
	}
	
	//Test null system query name
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery2() throws Exception {
		this.validationQuery.setSystemQueryName(null);
		this.impl.validateQuery(this.validationQuery);
	}
	
	//Test null dialodId
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery3() throws Exception {
		validationQuery.setDialogId(null);
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid dialodId
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery4() throws Exception {
		validationQuery.setDialogId("unsupported");
		impl.validateQuery(validationQuery);
	}
	
	//Test null start date
	@Test
	public void testValidateQuery5() throws Exception {
		validationQuery.setStartDate(null);
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid start date
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery6() throws Exception {
		validationQuery.setStartDate("invalid");
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid start date operator
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery7() throws Exception {
		validationQuery.setStartDateOperator("unsupported");
		impl.validateQuery(validationQuery);
	}

	//Test null end date
	@Test
	public void testValidateQuery8() throws Exception {
		validationQuery.setEndDate(null);
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid end date
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery9() throws Exception {
		validationQuery.setEndDate("invalid");
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid end date operator
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery10() throws Exception {
		validationQuery.setEndDateOperator("unsupported");
		impl.validateQuery(validationQuery);
	}
	
	//Test incomplete set of coordinates
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery11() throws Exception {
		validationQuery.setNwLat(null);
		impl.validateQuery(validationQuery);
	}
	
	//Test all coordinates null
	@Test
	public void testValidateQuery12() throws Exception {
		validationQuery.setNwLat(null);
		validationQuery.setNwLon(null);
		validationQuery.setSeLat(null);
		validationQuery.setSeLon(null);
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid order by order
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery13() throws Exception {
		validationQuery.setOrderByOrder("unsupported");
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid order by field
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery14() throws Exception {
		validationQuery.setOrderByField("unsupported");
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid result packaging
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery15() throws Exception {
		validationQuery.setResultPackaging("unsupported");
		impl.validateQuery(validationQuery);
	}
	
	//Test invalid result encoding
	@Test(expected = InvalidQueryException.class)
	public void testValidateQuery16() throws Exception {
		validationQuery.setResultEncoding("unsupported");
		impl.validateQuery(validationQuery);
	}
}