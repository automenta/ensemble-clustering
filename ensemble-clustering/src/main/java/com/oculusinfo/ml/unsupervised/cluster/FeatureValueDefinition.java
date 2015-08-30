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

import com.oculusinfo.ml.centroid.Centroid;
import com.oculusinfo.ml.distance.DistanceFunction;

import java.io.Serializable;
import java.util.function.Supplier;

public class FeatureValueDefinition<F,V> implements Serializable {
	private static final long serialVersionUID = -8378567604749382148L;
	public final F featureName;
	@SuppressWarnings("rawtypes")
	public final DistanceFunction distFunc;
	@SuppressWarnings("rawtypes")
	public final Supplier<Centroid<F,V>> builder;
	
	@SuppressWarnings("rawtypes")
	public FeatureValueDefinition(F featureName,
								  Supplier<Centroid<F,V>> builder,
								  //Class<? extends Centroid> centroidClass,
								  DistanceFunction distFunc) {
		this.distFunc = distFunc;
		this.featureName = featureName;
		this.builder = builder;
	}
}
