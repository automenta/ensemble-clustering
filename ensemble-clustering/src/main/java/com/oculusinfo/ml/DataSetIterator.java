package com.oculusinfo.ml;

import java.util.Iterator;
import java.util.Map;


final public class DataSetIterator<K> implements Iterator<Instance<K>> {
    final Iterator<Map.Entry<K, Instance<K>>> iterator;

	public DataSetIterator(Map<K, Instance<K>> map) {
        this.iterator = map.entrySet().iterator();
    }

	@Override
    public final boolean hasNext() {
        return iterator.hasNext();
    }

	@Override
    public final Instance<K> next() {
        return iterator.next().getValue();
    }

	@Override
    public final void remove() {
        iterator.remove();
    }
}
