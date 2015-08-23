package com.oculusinfo.ml;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by me on 8/23/15.
 */
public class DataSetIterator implements Iterator<Instance> {
    final Iterator<Map.Entry<String, Instance>> iterator;

	public DataSetIterator(Map<String, Instance> map) {
        this.iterator = map.entrySet().iterator();
    }

	@Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

	@Override
    public Instance next() {
        return iterator.next().getValue();
    }

	@Override
    public void remove() {
        iterator.remove();
    }
}
