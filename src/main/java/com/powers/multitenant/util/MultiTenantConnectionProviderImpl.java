package com.powers.multitenant.util;

import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.datasource.lookup.DataSourceLookup;

/**
 * It gets the connection based on different datasources. 
 */
public class MultiTenantConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl
{
	
	Log logger = LogFactory.getLog(getClass());	

	private static final long serialVersionUID = 14535345L;
	
	@Autowired
	@Qualifier("defaultDataSource")
	private DataSource defaultDataSource;
	
	@Autowired
	@Qualifier("dataSourceLookup")
	private DataSourceLookup dataSourceLookup;

	/**
	 * Select datasources in situations where not tenantId is used (e.g. startup processing).
	 */
	@Override
	protected DataSource selectAnyDataSource() {
		logger.trace("Select any dataSource: " + defaultDataSource);		
		return defaultDataSource;
	}

	/**
	 * Obtains a DataSource based on tenantId
	 */
	@Override
	protected DataSource selectDataSource(String tenantIdentifier) {
		System.out.println("Selecting tenantIdentifier: " + tenantIdentifier);
		if(dataSourceLookup!=null){
			DataSource ds = dataSourceLookup.getDataSource(tenantIdentifier.trim());
			System.out.println("Select dataSource from "+ tenantIdentifier+ ": " + ds);
			logger.trace("Select dataSource from "+ tenantIdentifier+ ": " + ds);
			return ds;
		}
		return defaultDataSource;
	}

	
}