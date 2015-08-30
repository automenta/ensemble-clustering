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
package com.oculusinfo.ml.feature;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;


/***
 * Feature represents one data type of an instance in a data set.
 *
 * Each feature must have a unique name to distinguish it from others.  
 *  
 * @author slangevin
 *
 * F is the type of key used to index them
 */
public class Feature<F, V> implements Serializable {
    private static final long serialVersionUID = 192274668774344842L;
    
    // the unique name of the feature
    protected F name;
   
    // The weight of this feature
    private double            weight;
	private V value;

	public Feature() {
		// empty constructor
	    this.weight = 1.0;
	}

	public Feature(F name) {
		this(name, 10);
	}

	public Feature(F name, double weight) {
		this.name = name;
        this.weight = weight;
	}

	public Feature(F name, V value) {
		this(name, 1.0);
		setValue(value);
	}

	public Feature(F name, V value, double weight) {
		this(name, weight);
		setValue(value);
	}

	public double getWeight () {
	    return weight;
	}

	public void setWeight (double weight) {
	    this.weight = weight;
	}

	public F getName() {
		return name;
	}
	
	public void setName(F name) {
		this.name = name;
	}
	
	@JsonIgnore
	public F getId() {
		return name;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	public V getValue() {
		return value;
	}

	public void setValue(V newValue) {
		this.value = newValue;
	}
}
