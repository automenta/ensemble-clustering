/**
 * Copyright (c) 2013 Oculus Info Inc.
 * http://www.oculusinfo.com/
 *
 * Released under the MIT License.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
 * of the Software, and to permit persons to whom the Software is furnished to do
 * so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.oculusinfo.ml.unsupervised.cluster;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oculusinfo.ml.Instance;
import com.oculusinfo.ml.centroid.Centroid;
import com.oculusinfo.ml.feature.Feature;

import java.util.*;

/***
 * A class that represents a cluster.  
 * 
 * Each cluster has a set of Instances that belong to the cluster and centroids that 
 * summarize the Features of the Instance members.  The centroids are used when computing 
 * distances of Instances with clusters during clustering. 
 *   
 * @author slangevin
 *
 * K = key, F = feature key, V = feature value
 */
public class Cluster<K,F,V> extends Instance<K,F,V> {

	
	protected boolean onlineUpdate = false;  	// default to not updating centroid when new members are added

	protected final Map<F,Centroid<F,V>> centroids = new HashMap<>();
	protected final Set<Instance<K,F, V>> members = new LinkedHashSet<>();

//	public Cluster() {
//		super(UUID.randomUUID().toString());
//	}
	
	@SuppressWarnings("rawtypes")
	public Cluster(K id, Collection<FeatureValueDefinition<F,V>> types, boolean onlineUpdate) {
		super(id);

		//TODO determine size of the collections from 'types'.size()
		for (FeatureValueDefinition<F,V> def : types) {
			try {
				Centroid<F,V> feature = def.builder.get();
				feature.setName(def.featureName);
				centroids.put(def.featureName, feature);
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		}

		this.onlineUpdate = onlineUpdate;
	}
	
	@SuppressWarnings("rawtypes")
	public void reset() {
		this.members.clear();
		centroids.forEach((o, centroid) -> {
			try {
				centroid.reset();
			} catch (Exception e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
			}
		});

	}
	
	public void updateCentroid() {
		for (Map.Entry<F, Centroid<F,V>> stringCentroidEntry : centroids.entrySet()) {
			Centroid<F,V> vv = stringCentroidEntry.getValue();
			add(vv.getCentroid());
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void updateCentroids(Instance<K,F,V> inst, boolean removefrom) {
		for (Map.Entry<F, Centroid<F,V>> e : centroids.entrySet()) {
			Centroid m = e.getValue();
			Feature<F, V> feature = inst.getFeature(e.getKey());
			if (feature != null) {
				if (removefrom) {
					m.remove(feature);
				}
				else {
					m.add(feature);
				}
				
				if (onlineUpdate) {
					add(m.getCentroid());
				}
			}
		}
	}
	
	public boolean add(Instance<K,F,V> inst) {
		boolean isNew = members.add(inst);
		updateCentroids(inst, false);
		return isNew;
	}
	
	public boolean remove(Instance<K,F,V> inst) {
		boolean isAltered = members.remove(inst);
		updateCentroids(inst, false);
		return isAltered;
	}
	
	public boolean replace(Instance<K,F,V> oldValue, Instance<K,F,V> newValue) {
		boolean altered = remove(oldValue);
		
		if (altered) {
			add(newValue);
		}
		return altered;
	}

	public boolean contains(Instance inst) {
		return members.contains(inst);
	}

	@Override
	@JsonIgnore
	public boolean isEmpty() {
		return members.isEmpty();
	}
	
	@SuppressWarnings("rawtypes")
	@JsonIgnore
	public Map<F, Centroid<F,V>> getCentroids() {
		return centroids;
	}
	
	@SuppressWarnings("rawtypes")
	@JsonIgnore
	public void setCentroids(Map<F, Centroid<F,V>> centroids) {
		this.centroids.putAll(centroids);
	}
	
	public Set<Instance<K,F,V>> getMembers() {
		return members;
	}
	
	public void setMembers(Set<Instance<K,F,V>> members) {
		this.members.addAll(members);
	}
	
//	@SuppressWarnings("rawtypes")
//	@JsonIgnore
//	public Collection<FeatureValueDefinition<F,V>> getTypeDefs() {
//		Collection<FeatureValueDefinition<F,V>> typedefs = new LinkedList<>();
//		for (Centroid<F,V> centroid : centroids.values()) {
//			typedefs.add(new FeatureValueDefinition(centroid.getName(),
//					centroid.builder(),
//					null));
//		}
//		return typedefs;
//	}

	public int size() {
		return members.size();
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean printMembers) {
		StringBuilder str = new StringBuilder();
		str.append(super.toString());
		
		if (printMembers) {
			str.append("\nMembers:\n");
			for (Instance inst : this.getMembers()) {
				str.append(inst.toString()).append('\n');
			}
		}
		return str.toString();
	}

	/**
	 * A method that can be used to spit out information on this cluster every iteration.
	 */
    public String getIterationDebugInfo () {
        return members.size()+" members";
    }
}
