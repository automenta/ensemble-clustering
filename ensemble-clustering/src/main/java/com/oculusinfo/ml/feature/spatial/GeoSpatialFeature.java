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
package com.oculusinfo.ml.feature.spatial;

import com.oculusinfo.ml.feature.Feature;

/***
 * A GeoSpatialFeature represents a latitude and longitude geo-spatial coordinate
 * 
 * @author slangevin
 *
 */
public class GeoSpatialFeature<F> extends Feature<F, double[]> {

	final double[] latlon = new double[2];
	
	@Override
	public String toString() {
		return (this.getName() + ":[" + latlon[0] + ';' + latlon[1] + ']');
	}
	
	public GeoSpatialFeature() {
		super();
	}
	
	public GeoSpatialFeature(F name) {
		super(name);
	}

	public void setValue(double latitude, double longitude) {
		latlon[0] = latitude;
		latlon[1]  = longitude;
	}
	
	public double getLatitude() {
		return latlon[0];
	}
	
	public void setLatitude(double latitude) {
		latlon[0] = latitude;
	}
	
	public double getLongitude() {
		return latlon[1];
	}
	
	public void setLongitude(double longitude) {
		latlon[1] = longitude;
	}
}
