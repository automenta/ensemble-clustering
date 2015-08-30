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
package com.oculusinfo.ml.feature.semantic.centroid;

import com.gs.collections.api.tuple.Pair;
import com.oculusinfo.ml.centroid.Centroid;
import com.oculusinfo.ml.feature.semantic.SemanticFeature;
import com.oculusinfo.ml.stats.FeatureFrequency;
import com.oculusinfo.ml.stats.FeatureFrequencyTable;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/***
 * A Centroid for SemanticFeatures that represents the centroid as the top 10 semantic features with the highest frequency
 * 
 * @author slangevin
 *
 */
public class SemanticCentroid<K> implements Centroid<SemanticFeature<K>> {

	private String name;
	private static final int MAX_CENTROID_FEATURES = 5;
	private static final int MAX_ENTITIES_PER_FEATURE = 3;
	
	protected final FeatureFrequencyTable<Pair<K,String>> freqTable = new FeatureFrequencyTable<>();
	protected final HashMap<Pair<K,String>, FeatureFrequencyTable<Pair<K,String>>> entityFreqTable = new LinkedHashMap<>();



	@Override
	public void add(SemanticFeature<K> feature) {
		//StringFeature<K> feature = new StringFeature<K>(feature.getName());
		freqTable.add(feature);

		//TODO use computeIfAbsent
		if (!entityFreqTable.containsKey(feature.getId())) {
			entityFreqTable.put(feature.getId(), new FeatureFrequencyTable());
		}
		SemanticFeature sem = new SemanticFeature(feature.getName());
		sem.setValue(feature.getConcept(), feature.getUri());
		entityFreqTable.get(feature.getId()).add(sem);
	}



	@Override
	public void remove(SemanticFeature<K> feature) {
		//StringFeature<K> feature = new StringFeature(feature.getName());
		freqTable.decrement(feature);
		if (entityFreqTable.containsKey(feature.getId())) {
			entityFreqTable.get(feature.getId()).decrement(feature);
		}
	}


	@Override
	public Collection<SemanticFeature<K>> getAggregatableCentroid () {
        Collection<SemanticFeature<K>> rawCounts = new LinkedList<>();

		Collection<FeatureFrequency<Pair<K, String>>> freqs = freqTable.getAll();
        for (FeatureFrequency<Pair<K, String>> freq : freqs) {
            Collection<FeatureFrequency<Pair<K, String>>> topEntities = entityFreqTable.get(freq.feature.getId()).getAll();
            for (FeatureFrequency topEntity : topEntities) {
                rawCounts.add((SemanticFeature) topEntity.feature);
            }
        }
        
        return rawCounts;
    }

    @Override
	public SemanticFeature<K> getCentroid() {
		Collection<SemanticFeature<K>> medoid = new LinkedList<>();
		
		// semantic medoid is the top N most frequent semantic features
		Collection<FeatureFrequency<Pair<K, String>>> freqs = freqTable.getTopN(MAX_CENTROID_FEATURES);
		for (FeatureFrequency freq : freqs) {
			Collection<FeatureFrequency<Pair<K, String>>> topEntities = entityFreqTable.get(freq.feature.getId()).getTopN(MAX_ENTITIES_PER_FEATURE);
			for (FeatureFrequency topEntity : topEntities) {
				medoid.add((SemanticFeature) topEntity.feature);
			}
		}
		
		// TODO this needs to be refactored!
		return null;
	}

	@Override
	public void setLabel(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}


	public FeatureFrequencyTable getFreqTable() {
		return this.freqTable;
	}

	/*
	public HashMap<String, FeatureFrequencyTable> getEntityFreqTable() {
		return this.entityFreqTable;
	}
	*/

	@Override
	public void reset() {
		freqTable.clear();
		entityFreqTable.clear();
	}
}
