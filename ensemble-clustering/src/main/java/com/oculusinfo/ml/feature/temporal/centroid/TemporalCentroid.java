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
package com.oculusinfo.ml.feature.temporal.centroid;

import com.oculusinfo.ml.centroid.Centroid;
import com.oculusinfo.ml.feature.temporal.TemporalFeature;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/***
 * A Centroid for TemporalFeatures that represents the centroid by the average start and end date 
 * 
 * @author slangevin
 *
 */
public class TemporalCentroid<K> implements Centroid<TemporalFeature<K>> {
	private static final long serialVersionUID = -8692407140201096772L;
	private String name;
	private double weight = 0.0;
	private long cstart = 0, cend = 0;
	
	@Override
	public void add(TemporalFeature<K> feature) {
	    double addedWeight = feature.getWeight();
		long start = 0, end = 0;
		
		start = feature.getStart().getTime();
		end = feature.getEnd().getTime();

		// Increase the weight of this feature according to the weight of the added feature
		weight += addedWeight;

		// incrementally revise the centroid start and end
		cstart += start * addedWeight;
		cend += end * addedWeight;
	}
	
	@Override
	public void remove(TemporalFeature<K> feature) {
	    double removedWeight = feature.getWeight();
		long start = 0, end = 0;
		
		start = feature.getStart().getTime();
		end = feature.getEnd().getTime();

		if (0 >= weight) {
			System.out.println("Attempt to remove from an empty temporal centroid");
		} else {
		    // decrement the centroid start and end
		    cstart -= start * removedWeight;
		    cend -= end * removedWeight; 

		    // decrease the weight of this feature according to the weight of the removed feature
		    weight = weight - removedWeight;
		}
	}

	@Override
	public Collection<TemporalFeature<K>> getAggregatableCentroid () {
	    return Collections.singleton(getCentroid());
	}

	@Override
	public TemporalFeature<K> getCentroid() {
		// create the centroid temporal feature set
		TemporalFeature centroid = new TemporalFeature(name);
		centroid.setValue(new Date(Math.round(cstart/weight)),
		           new Date(Math.round(cend/weight))); // compute average start/end
		centroid.setWeight(weight);
		
		return centroid;
	}

	@Override
	public void setLabel(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void reset() {
		weight = 0;
		cstart = 0; 
		cend = 0;
	}
}
