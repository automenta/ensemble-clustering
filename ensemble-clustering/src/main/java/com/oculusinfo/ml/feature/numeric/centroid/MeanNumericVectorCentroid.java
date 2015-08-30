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
package com.oculusinfo.ml.feature.numeric.centroid;

import com.oculusinfo.ml.centroid.Centroid;
import com.oculusinfo.ml.feature.Feature;

import java.util.Collection;
import java.util.Collections;

/***
 * A Centroid for NumericVectorFeatures that represents the centroid as the average number of each vector component
 * 
 * @author slangevin
 *
 */
public class MeanNumericVectorCentroid<F> implements Centroid<F,double[]>  {
	//private NumericVectorFeature<F> name;
	private double weight;
	private double[] meanVector;
	private F name;

//	@Override
//	public Supplier<Feature<F, double[]>> builder() {
//		return () -> {
//			return new Feature();
//		};
//	}


	@Override
	public void add(Feature<F, double[]> feature) {
		double addedWeight = feature.getWeight();
		double newWeight = weight + addedWeight;

		if (meanVector == null) {
			if (feature.getValue() != null) {
				meanVector = feature.getValue().clone();
				weight = addedWeight;
			}
		}
		else {
			if (feature.getValue() != null) {
				// incrementally revise the centroid vector
				for (int i=0; i < meanVector.length; i++) {
					meanVector[i] = (meanVector[i] * weight + feature.getValue()[i] * addedWeight) / newWeight;
				}
				weight = newWeight;
			}
		}
	}

	@Override
	public void remove(Feature<F, double[]> feature) {
	    double removedWeight = feature.getWeight();
	    double newWeight = weight - removedWeight;

	    if (0.0 == weight) return;

		// decrement centroid vector
		for (int i=0; i < meanVector.length; i++) {
			meanVector[i] = (meanVector[i] * weight - feature.getValue()[i] * removedWeight) / newWeight;
		}
		weight = newWeight;
	}


	@Override
	public void setName(F name) {
		this.name = name;
	}

	@Override
	public F getName() {
		return name;
	}

	@Override
    public Collection<Feature<F,double[]>> getAggregatableCentroid() {
        return Collections.singleton(getCentroid());
    }

	@Override
	public Feature<F,double[]> getCentroid() {
		// create the centroid geospatial feature set
		//NumericVectorFeature<F> mean = new NumericVectorFeature(name);
		return new Feature(name, meanVector, weight);
	}

	@Override
	public void reset() {
		meanVector = null;
		weight = 0;
	}
}
