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
package com.oculusinfo.ml.feature.spatial.centroid;

import com.oculusinfo.ml.centroid.Centroid;
import com.oculusinfo.ml.feature.Feature;
import com.oculusinfo.ml.feature.spatial.GeoSpatialFeature;

import java.util.Collection;
import java.util.Collections;

/***
 * A Centroid for GeoSpatialFeatures that represents the centroid using a naive average of latitude and longitude
 *   
 * @author slangevin
 *
 */
public class FastGeoSpatialCentroid<F> implements Centroid<F,double[]> {

    private F name;
	private double weight = 0.0;

	public final double[] latlon = new double[2];

	@Override
	public void setName(F name) {
		this.name = name;
	}

	@Override
	public void add(Feature<F,double[]> feature) {
	    double addedWeight = feature.getWeight();
	    double newWeight = weight + addedWeight;

	    // incrementally revise the centroid coordinates
		latlon[0] = (latlon[0] * weight + feature.getValue()[0] * addedWeight) / newWeight;
		latlon[1] = (latlon[1] * weight + feature.getValue()[1] * addedWeight) / newWeight;

		weight = newWeight;
	}
	
	@Override
	public void remove(Feature<F,double[]> feature) {
	    double removedWeight = feature.getWeight();
	    double newWeight = weight - removedWeight;

	    if (weight <= 0.0) {
	        System.out.println("Attempt to remove from empty GeoSpatialCentroid");
	    } else {
	        latlon[0] = (latlon[0] * weight - feature.getValue()[0] * removedWeight) / newWeight;
			latlon[1] = (latlon[1] * weight - feature.getValue()[1] * removedWeight) / newWeight;
	        weight = newWeight;
	    }
	}

	@Override
	public Collection<Feature<F,double[]>> getAggregatableCentroid () {
	    return Collections.singleton(getCentroid());
	}

	@Override
	public GeoSpatialFeature<F> getCentroid() {

		// create the centroid geospatial feature set
		GeoSpatialFeature<F> centroid = new GeoSpatialFeature(name);
		centroid.setValue( latlon[0], latlon[1] ); //(clat), (clon) );  // return average lat, lon - very crude method of determining centroid for geo
		centroid.setWeight(weight);
		return centroid;
	}


	@Override
	public F getName() {
		return this.name;
	}


	@Override
	public void reset() {
		weight = 0;
		latlon[0] = latlon[1] = 0;
	}
}
