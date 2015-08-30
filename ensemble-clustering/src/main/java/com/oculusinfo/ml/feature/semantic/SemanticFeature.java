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
package com.oculusinfo.ml.feature.semantic;

import com.gs.collections.api.tuple.Pair;
import com.gs.collections.impl.tuple.Tuples;
import com.oculusinfo.ml.feature.Feature;

/***
 * A SemanticFeature represents an instance of a semantic class in an Ontology
 *  
 * The SemanticFeature is associated with:
 *   * An Ontology Concept class name
 *   * A uri that uniquely identifies the instance
 *   * A label that describes the instance
 *   
 * Example:  Concept = foaf:Person,  URI = http://danbri.org/ , Label = Dan Brickley
 * 
 * @author slangevin
 *
 */
public class SemanticFeature<K> extends Feature<Pair<K,String>, Object> {

	private K id;
	private String concept;
	private String uri;
	private String label;
	
	public SemanticFeature() {
		super();
	}
	
	public SemanticFeature(K name) {
		super();
		this.id = name;
		setValue(null, null);
	}
	
	public void setValue(String concept, String uri) {
		this.uri = uri;
		if (concept.equals(this.concept))
			return;

		this.concept = concept;
		setName(Tuples.pair(id, concept));
	}
	
	public void setValue(String concept, String uri, String label) {
		setValue(concept, uri);
		this.label = label;
	}

	public String getConcept() {
		return concept;
	}
	
	public void setConcept(String concept) {
		if (concept.equals(this.concept))
			return;

		setValue(concept, uri);
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getLabel() {
		return label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public String toString() {
		return toString(false);
	}
	
	public String toString(boolean suppressLabel) {
		StringBuilder output = new StringBuilder();
		output.append(this.getId());
		if (label != null && !suppressLabel) {
			output.append(':').append(label);
		}
		return output.toString();
	}

}
