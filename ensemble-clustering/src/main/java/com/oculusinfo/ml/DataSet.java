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
package com.oculusinfo.ml;

import com.oculusinfo.ml.feature.Feature;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/***
 * DataSet represents a structured collection of data instances that is the input 
 * to oculus machine learning tools.  
 * 
 * @author slangevin
 *
 */
public class DataSet<K,F,V> implements Serializable, Iterable<Instance<K,F,V>> {

	private final Map<K, Instance<K,F,V>> map = new LinkedHashMap<>();

	public Set<K> getKeys() {
		return map.keySet();
	}
	
	/***
	 * Add an Instance to the Dataset
	 * @param inst the Instance to add
	 * @return whether an instance was replace with the same id
	 */
	public boolean add(Instance<K,F,V> inst) {
		return (map.put(inst.getId(), inst) != null);
	}
	
	/***
	 * Remove an instance from the Dataset
	 * @param inst the Instance to remove
	 * @return the removed Instance
	 */
	public Instance<K,F,V> remove(Instance<K,F,V> inst) {
		return map.remove(inst.getId());
	}
	public Instance<K,F,V> remove(K inst) {
		return map.remove(inst);
	}

	/***
	 * Get an instance by Id
	 * @param id the id of the Instance to return
	 * @return the Instance with id or null if no Instance exists
	 */
	public Instance<K,F,V> get(K id) {
		return map.get(id);
	}
	
	/***
	 * Return the number of Instances in the DataSet
	 * @return
	 */
	public int size() {
		return map.size();
	}
	
	/***
	 * Return a DataSet with random fraction number of Instances as this DataSet
	 * @param fraction fraction of Instances to return in the range of 0 and 1
	 * @return resulting sample DataSet
	 */
	public DataSet<K,F,V> sample(double fraction) {
		DataSet<K,F,V> sample = new DataSet<>();
		
		// max fraction is 1
		if (fraction > 1) fraction = 1;
		
		// no samples are requested - return an empty data set
		if (fraction <= 0) return sample;
		
		long numToSample = Math.round( map.size() * fraction );
		
		ArrayList<K> keys = new ArrayList<>(map.keySet());
		
		// randomly pick k instances as the initial k means
		ArrayList<K> indexes = new ArrayList<>(keys.size());
		for (int i = 0; i < indexes.size(); i++) {
			indexes.add( keys.get(i) );
		}
		Collections.shuffle(indexes);  // permutate the indexes
		for (int i = 0; i < numToSample; i++) {
			sample.add( get(indexes.get(i)) );
		}
		return sample;
	}
	

	static private void shuffle(final Instance[] array) {
		int n = array.length;

		final Random rnd = ThreadLocalRandom.current();
		while (n > 1) {
			int k = rnd.nextInt(n);
			n--;
			swap(array, n, k);
		}
	}

	private static void swap(final Instance[] array, final int n, final int k) {
		final Instance t = array[n];
		array[n] = array[k];
		array[k] = t;
	}

	/***
	 * Randomly split this DataSet into n similarly sized DataSets.
	 * @param n the number of folds to split the DataSet - value must be greater than 1 and not greater than the number of instances in DataSet
	 * @return a list of n DataSets
	 */
	public List<DataSet<K,F,V>> nFolds(int n) {
		// Make sure n is valid: each fold must have at least one instance!
		if (n > size() || n < 1) return null;
		
		List<DataSet<K,F,V>> folds = new LinkedList<>();
		
		Instance<K,F,V>[] instances = new Instance[size()];
		instances = map.values().toArray(instances);
		shuffle(instances);
	
		int sliceSize = size() / n;
		int extra = size() % n;
		
		// create n folds
		for (int i=0; i < n; i++) {
			DataSet<K,F,V> fold = new DataSet<>();
			folds.add(fold);
			
			int start = i*sliceSize;
			int end = start + sliceSize;
			for (int j=start; j < end; j++) {
				fold.add(instances[j]);
			}
		}
		
		// evenly distribute any extra instances
		for (int i=0; i < extra; i++) {
			int offset = instances.length - extra + i;
			folds.get(i).add(instances[offset]);
		}
		
		return folds;
	}
	
	/***
	 * Normalize the specified Feature for all Instances in this DataSet.
	 * 
	 * Currently only NumericVectorFeature types are supported.
	 * 
	 * @param featureName the name of the feature to normalize
	 */
	public void normalizeInstanceFeature(F featureName) {
		ArrayList<Feature<F, V>> allFeatures = new ArrayList<>();
		
		// gather up all matching features
		for (Instance<K,F,V> inst : this) {
			if (inst.containsFeature(featureName)) {
				allFeatures.add( inst.getFeature(featureName) );
			}
		}

		final int s = allFeatures.size();

		if (s == 0) return;

		double N = s;
		



		V vv = allFeatures.get(0).getValue();

		// currently only support normalizing numeric vector features
		if (!(vv instanceof double[])) return;

		double[] meanVector = ((double[])vv).clone();
		
		// compute mean of feature
		for (int i = 1; i < s; i++) {

			double[] vals = (double[])allFeatures.get(i).getValue();
			
			for (int j=0; j < meanVector.length; j++) {
				meanVector[j] += vals[j];
			}
		}
		for (int i=0; i < meanVector.length; i++) {
			meanVector[i] /= N;
		}
		
		double[] stdevVector = new double[meanVector.length];
		
		// compute stdev of feature
		for (int i = 0; i < s; i++) {
			double[] vals = (double[])allFeatures.get(i).getValue();

			
			for (int j=0; j < meanVector.length; j++) {
				stdevVector[j] += (vals[j] - meanVector[j])*(vals[j] - meanVector[j]);
			}
		}
		for (int i=0; i < stdevVector.length; i++) {
			stdevVector[i] = Math.sqrt( stdevVector[i] / (N-1) );
		}
		
		// normalize each feature vector
		for (int i = 0; i < s; i++) {
			double[] vals = (double[])allFeatures.get(i).getValue();
			
			for (int j=0; j < vals.length; j++) {
				vals[j] = (vals[j] - meanVector[j]) / stdevVector[j];
			}
		}
	}
	 
	@Override
	public Iterator<Instance<K,F,V>> iterator() {
		return new DataSetIterator(map);
	}

	/***
	 * Add a collection of Instances to the DataSet
	 * @param c the Instances to add
	 * @return true if an existing Instance with a matching id in the DataSet was replaced 
	 */
	public boolean addAll(Collection<Instance<K,F,V>> c) {
		boolean altered = false;
		
		for (Instance<K,F,V> i : c) {
			if ( add(i) ) altered = true;
		}
		return altered;
	}

	/***
	 * Remove all Instances from this DataSet
	 */
	public void clear() {
		map.clear();
	}

	/***
	 * Return true if the DataSet contains the specified Instance
	 * @param inst the Instance to test
	 * @return true if the Instance is a member of the DataSet
	 */
	public boolean contains(Instance<K,F,V> inst) {
		return map.containsValue(inst);
	}

	/***
	 * Return true if the DataSet contains all the specified Instances
	 * @param c the Instances to test
	 * @return true if the Instances are all members of the DataSet
	 */
	public boolean containsAll(Collection<Instance<K,F,V>> c) {
		for (Instance<K,F,V> i : c) {
			if (map.containsKey(i.getId()) == false) return false;
		}
		return true;
	}

	/***
	 * Returns true if this DataSet contains no Instances
	 * @return true if this DataSet contains no Instances
	 */
	public boolean isEmpty() {
		return map.isEmpty();
	}
}
