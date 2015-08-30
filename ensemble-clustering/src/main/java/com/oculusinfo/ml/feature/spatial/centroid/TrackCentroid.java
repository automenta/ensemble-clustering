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

import com.oculusinfo.geometry.geodesic.Track;
import com.oculusinfo.ml.centroid.Centroid;
import com.oculusinfo.ml.feature.spatial.TrackFeature;

import java.util.Collection;
import java.util.Collections;

/**
 * A Centroid for TrackFeatures that represents the centroid by computing the average track
 * 
 * @author nkronenfeld
 */
public class TrackCentroid<K> implements Centroid<TrackFeature<K>>{


    private String label;
    private double _weight;
    private Track  _centroid;
    private int    _changes;

    public TrackCentroid () {
        _weight = 0;
        _centroid = null;
    }

    public int getChanges () {
        return _changes;
    }

    @Override
    public void add(TrackFeature feature) {
        ++_changes;
        Track newTrack = feature.getValue();
        // add the track to the list of members
        if (null == _centroid) {
            _centroid = newTrack;
            _weight = feature.getWeight();
        } else {
            // Weight according to how many are already there
            _centroid = _centroid.weightedAverage(newTrack, _weight, feature.getWeight());
            _weight = _weight + feature.getWeight();
        }
    }

    @Override
    public void remove (TrackFeature feature) {
        ++_changes;
        Track toRemove = feature.getValue();

        //remove the track from the list of members
        double newWeight = _weight - feature.getWeight();
        if (newWeight <= 0) {
            _centroid = null;
            _weight = 0.0;
        } else {
            _centroid = _centroid.weightedAverage(toRemove, _weight, -feature.getWeight());
            _weight = newWeight;
        }
    }

    @Override
    public void setLabel(String name) {
        this.label = name;
    }

    @Override
    public String getName() {
        return this.label;
    }


    @Override
    public Collection<TrackFeature<K>> getAggregatableCentroid () {
        return Collections.singleton(getCentroid());
    }

    @Override
    public TrackFeature<K> getCentroid() {
        TrackFeature<K> centroid = new TrackFeature(label);
        centroid.setValue(_centroid);
        centroid.setWeight(_weight);
        return centroid;
    }

	@Override
	public void reset() {
		_weight = 0;
		_centroid = null;
		_changes = 0;
	}
}
