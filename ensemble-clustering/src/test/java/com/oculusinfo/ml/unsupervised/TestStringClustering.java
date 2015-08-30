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
package com.oculusinfo.ml.unsupervised;

import com.oculusinfo.ml.DataSet;
import com.oculusinfo.ml.Instance;
import com.oculusinfo.ml.feature.string.StringFeature;
import com.oculusinfo.ml.feature.string.centroid.StringMedianCentroid;
import com.oculusinfo.ml.feature.string.distance.EditDistance;
import com.oculusinfo.ml.unsupervised.cluster.Cluster;
import com.oculusinfo.ml.unsupervised.cluster.ClusterResult;
import com.oculusinfo.ml.unsupervised.cluster.kmeans.KMeans;

public class TestStringClustering {

	private static final String FEATURE_NAME1 = "tokens";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		DataSet<String> ds = new DataSet<String>();
		
		Instance<String> inst = new Instance<String>("1");
		StringFeature feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jack black");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("2");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jack black");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("3");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jack");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("4");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jack l. black");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("5");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("j. black");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("6");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("j black");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("7");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("black");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("8");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jackie black");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("9");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jack brown");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("10");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jackie green");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("11");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("bob");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("12");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("bobbie");
		inst.add(feature);
		ds.add(inst);
		
		inst = new Instance<String>("13");
		feature = new StringFeature(FEATURE_NAME1);
		feature.setValue("jackie");
		inst.add(feature);
		ds.add(inst);
		
		KMeans clusterer = new KMeans(4, 5, false);
		clusterer.registerFeatureType(
				FEATURE_NAME1,
				StringMedianCentroid.class, 
				new EditDistance(1.0));
		
		ClusterResult clusters = clusterer.doCluster(ds);
		for (Cluster c : clusters) {
			System.out.println(c.toString(true));
		}
		clusterer.terminate();
	}
}
