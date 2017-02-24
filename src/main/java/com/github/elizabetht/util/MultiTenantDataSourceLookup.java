package com.github.elizabetht.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.MapDataSourceLookup;
import org.springframework.stereotype.Component;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * It lookup the correct datasource to use, we have one per tenant
 * 
 * The tenant datasource has default properties from database.properties and
 * also properties in database.{tenantId}.properties whose properties override
 * the default ones.
 * 
 * @author jose.mgmaestre
 *
 */
@Component(value = "dataSourceLookup")
public class MultiTenantDataSourceLookup extends MapDataSourceLookup {

	Log logger = LogFactory.getLog(getClass());

	private String tenantDbConfigs =  "classpath*:/*database.properties"; // For testing
	private String tenantDbConfigsOverride = "classpath*:/*database.properties"; // For production
	private String tenantRegex = "@\"^.*?(?=-)\"";

	@Autowired
	public MultiTenantDataSourceLookup(BoneCPDataSource defaultDataSource) {
		super();

		try {
			initializeDataSources(defaultDataSource);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * It initialize all the datasources. If multitenancy is activated it also
	 * add dataSources for different tenants on tenantDbConfigs or
	 * tenantDbConfigsOverride
	 * 
	 * @param tenantResolver
	 * @throws IOException
	 */
	private void initializeDataSources(BoneCPDataSource defaultDataSource) throws IOException {
		//Get the path where server is stored, we will save configurations there,
		//so if we redeploy it will not be deleted
		String catalinaBase = System.getProperties().getProperty("catalina.base");

		logger.info("MultiTenancy configuration: ");
		logger.info("---------------------------------------------------");

		// Add the default tenant and datasource
		addDataSource("nga", defaultDataSource);
		logger.info("Configuring default tenant: DefaultTenant - Properties: " + defaultDataSource.toString());

		// Add the other tenants
		logger.info("-- CLASSPATH TENANTS --");
		addTenantDataSources(defaultDataSource, tenantDbConfigs);
		logger.info("-- GLOBAL TENANTS --");
		addTenantDataSources(defaultDataSource,  tenantDbConfigsOverride);
		logger.info("---------------------------------------------------");
	}

	/**
	 * Add Tenant datasources based on the default properties on
	 * defaultDataSource and the configurations in dbConfigs.
	 * 
	 * @param defaultDataSource
	 * @param dbConfigs
	 */
	private void addTenantDataSources(BoneCPDataSource defaultDataSource, String dbConfigs) {
		// Add the custom tenants and datasources
		Pattern tenantPattern = Pattern.compile(this.tenantRegex);
		PathMatchingResourcePatternResolver fileResolver = new PathMatchingResourcePatternResolver();

		InputStream dbProperties = null;

		try {
			Resource[] resources = fileResolver.getResources(dbConfigs);
			for (Resource resource : resources) {
				// load properties
				Properties props = new Properties(defaultDataSource.getClientInfo());
				dbProperties = resource.getInputStream();
				props.load(dbProperties);

				// Get tenantId using the filename and pattern
				String tenantId = getTenantId(tenantPattern, resource.getFilename());
				logger.info("+++++++-----TENANT-ID: " + tenantId);
				// Add new datasource with own configuration per tenant
				BoneCPDataSource customDataSource = createTenantDataSource(props, defaultDataSource);
				addDataSource(tenantId, customDataSource); // It replace if tenantId was already there.

				logger.info("Configured tenant: " + tenantId + " - Properties: " + customDataSource.toString());

			}
		} catch (FileNotFoundException fnfe) {
			logger.warn("Not tenant configurations or path not found: " + fnfe.getMessage());
		} catch (IOException ioe) {
			logger.error("Error getting the tenants: " + ioe.getMessage());
		} finally {
			if (dbProperties != null) {
				try {
					dbProperties.close();
				} catch (IOException e) {
					logger.error("Error closing a property tenant: " + dbProperties.toString());
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Create a datasource with tenant properties, if a property is not found in Properties 
	 * it takes the property from the defaultDataSource
	 *
	 * @param defaultDataSource a default datasource
	 * @return a BoneCPDataSource based on tenant and default properties
	 */
	private BoneCPDataSource createTenantDataSource(Properties tenantProps, BoneCPDataSource defaultDataSource)
	{
		BoneCPDataSource customDataSource = new BoneCPDataSource();
		
		//url, username and password must be unique per tenant so there is not default value
		customDataSource.setJdbcUrl(tenantProps.getProperty("database.url")); 
		customDataSource.setUsername(tenantProps.getProperty("database.username")); 
		customDataSource.setPassword(tenantProps.getProperty("database.password"));
		//These has default values in defaultDataSource
		customDataSource.setDriverClass(tenantProps.getProperty("database.driverClassName", defaultDataSource.getDriverClass()));
		customDataSource.setIdleConnectionTestPeriodInMinutes(Long.valueOf(tenantProps.getProperty(
				"database.idleConnectionTestPeriod",String.valueOf(defaultDataSource.getIdleConnectionTestPeriodInMinutes()))));
		customDataSource.setIdleMaxAgeInMinutes(Long.valueOf(tenantProps.getProperty(
				"database.idleMaxAge", String.valueOf(defaultDataSource.getIdleMaxAgeInMinutes()))));
		customDataSource.setMaxConnectionsPerPartition(Integer.valueOf(tenantProps.getProperty(
				"database.maxConnectionsPerPartition", String.valueOf(defaultDataSource.getMaxConnectionsPerPartition()))));
		customDataSource.setMinConnectionsPerPartition(Integer.valueOf(tenantProps.getProperty(
				"database.minConnectionsPerPartition", String.valueOf(defaultDataSource.getMinConnectionsPerPartition()))));
		customDataSource.setPartitionCount(Integer.valueOf(tenantProps.getProperty(
				"database.partitionCount", String.valueOf(defaultDataSource.getPartitionCount()))));
		customDataSource.setAcquireIncrement(Integer.valueOf(tenantProps.getProperty(
				"database.acquireIncrement", String.valueOf(defaultDataSource.getAcquireIncrement()))));
		customDataSource.setStatementsCacheSize(Integer.valueOf(tenantProps.getProperty(
				"database.statementsCacheSize",String.valueOf(defaultDataSource.getStatementCacheSize()))));
		customDataSource.setReleaseHelperThreads(Integer.valueOf(tenantProps.getProperty(
				"database.releaseHelperThreads", String.valueOf(defaultDataSource.getReleaseHelperThreads()))));customDataSource.setDriverClass(tenantProps.getProperty("database.driverClassName"));
		
		return customDataSource;
	}

	/**
	 * Get the tenantId from filename using the pattern
	 * 
	 * @param tenantPattern
	 * @param filename
	 * @return tenantId
	 * @throws IOException
	 */
	private String getTenantId(Pattern tenantPattern, String filename) throws IOException {
		
	//	Matcher matcher = tenantPattern.matcher(filename);
		System.out.println("------>GETTING tenant_id: " + " " + filename.replace("-database.properties",""));
	//	boolean findMatch = matcher.matches();
	//	if (!findMatch) {
	//		throw new IOException("Error reading tenant name in the filename");
	//	}
		//return matcher.group(1);
		return filename.replace("-database.properties","");
	}

}