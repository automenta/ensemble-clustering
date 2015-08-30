package com.oculusinfo.ml.unsupervised.cluster;

import java.util.Map;
import java.util.UUID;


public class DefaultClusterFactory extends ClusterFactory<UUID,String,Object> {


    public DefaultClusterFactory(Map<String, FeatureValueDefinition<String,Object>> featureTypeDefs, boolean onlineUpdate) {
        super(featureTypeDefs, onlineUpdate);
    }

	@Override
	public Cluster<UUID, String, Object> create() {
		return new Cluster(UUID.randomUUID(), featureTypeDefs.values(), onlineUpdate);
	}

}
