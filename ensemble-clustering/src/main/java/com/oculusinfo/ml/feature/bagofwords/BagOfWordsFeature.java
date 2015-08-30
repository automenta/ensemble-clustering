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
package com.oculusinfo.ml.feature.bagofwords;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.oculusinfo.ml.feature.Feature;
import com.oculusinfo.ml.feature.string.StringFeature;
import com.oculusinfo.ml.stats.FeatureFrequency;
import com.oculusinfo.ml.stats.FeatureFrequencyTable;

import java.util.Collection;

/***
 * A BagOfWordsFeature represents a Set of Words each of which is associated with a frequency count.
 * 
 * Useful for representing Document or other Text fields in a DataSet.
 * 
 * @author slangevin
 *
 */
public class BagOfWordsFeature<K> extends Feature<K, Object> {
	private static final long serialVersionUID = 6927104885425283254L;
	private FeatureFrequencyTable<K> freqTable = new FeatureFrequencyTable();
	
	public BagOfWordsFeature() {
		super();
	}
	
	public BagOfWordsFeature(K name) {
		super(name);
	}
	
	public void setCount(FeatureFrequency<K> freq) {
		freqTable.remove(freq);
		freqTable.add(freq);
	}
	
	public void setCount(K term, int count) {
		FeatureFrequency<K> freq = new FeatureFrequency(new StringFeature(term));
		freq.frequency = count;
		setCount(freq);
	}
	
	public void incrementValue(K term) {
		freqTable.add(new StringFeature(term));
	}
	
	public void decrementValue(String value) {
		freqTable.decrement(new StringFeature(value));
	}
	
	public FeatureFrequency<K> getCount(K term) {
		return freqTable.get(new StringFeature(term));
	}
	
	@JsonIgnore
	public Collection<FeatureFrequency<K>> getValues() {
		return freqTable.getAll();
	}
	
	public FeatureFrequencyTable getFreqTable() {
		return this.freqTable;
	}
	
	public void setFreqTable(FeatureFrequencyTable<K> table) {
		freqTable = table;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(this.getName()).append(":[");
		int i=1;
		for (FeatureFrequency<K> f : freqTable.getAll()) {
			str.append(f.feature.getName()).append("=").append(f.frequency);
			if (i < freqTable.getAll().size()) str.append(';');
			i++;
		}
		str.append(']');
		
		return str.toString();
	}
}
