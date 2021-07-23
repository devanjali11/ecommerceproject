package com.luv2code.ecommerce.config;

import com.luv2code.ecommerce.entity.Product;
import com.luv2code.ecommerce.entity.ProductCategory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;

@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {
	
		//for auto-id generation
		private EntityManager entityManager;
		@Autowired
		public MyDataRestConfig(EntityManager theEntityManager) {
			entityManager=theEntityManager;
		}
	

    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {

        HttpMethod[] theUnsupportedActions = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE, HttpMethod.PATCH};

        // disable HTTP methods for Product: PUT, POST, DELETE and PATCH
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));

        // disable HTTP methods for ProductCategory: PUT, POST, DELETE and PATCH
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
        
        //call an internal helper method
        exposeIds(config);

    }
    
    private void  exposeIds(RepositoryRestConfiguration config)
    {
    	Set <EntityType<?>> entities=entityManager.getMetamodel().getEntities();
    	List<Class> entityClasses=new ArrayList<>();
    	
    	for(EntityType tempEntityType : entities) {
    		entityClasses.add(tempEntityType.getJavaType());
    	}
    		//expose entity id's for array 
    	Class[] domainTypes = entityClasses.toArray(new Class[0]);
    	config.exposeIdsFor(domainTypes);
    	
    }
    
}