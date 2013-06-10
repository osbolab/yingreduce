package edu.asu.ying.mapreduce.table;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

/**
 * {@link TableID} uniquely identifies a distributed table on the network.
 */
public final class TableID
	implements Serializable
{
	private static final long serialVersionUID = -7246845240300781593L;

	private final String id;
	private final byte[] id_bytes;
	
	public TableID(final String id) {
		this.id = id;
		try {
			this.id_bytes = id.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new AssertionError("UTF-8 encoding is not available.", e);
		}
	}
	
	public final String getId() {
		return this.id;
	}
	
	public final byte[] toByteArray() {
		return this.id_bytes;
	}
	
	@Override
	public final String toString() {
		return this.id;
	}
	
	@Override
	public final boolean equals(final Object rhs) {
		if (rhs == this)
			return true;
		if (rhs == null || rhs.getClass() != this.getClass())
			return false;
		
		return this.id.equals(((TableID) rhs).getId());
	}
	
	@Override
	public final int hashCode() {
		return this.id.hashCode();
	}
}
