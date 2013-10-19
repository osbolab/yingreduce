package edu.asu.ying.wellington.dfs.persistence;

import java.io.IOException;

import edu.asu.ying.wellington.dfs.PageIdentifier;
import edu.asu.ying.wellington.dfs.io.PageInputStream;
import edu.asu.ying.wellington.dfs.io.PageWriter;

/**
 * A {@code Persistence} provides uniform access to an underlying persistence engine
 * be it a file, relational database, or memory cache.
 */
public interface Persistence {

  PageWriter getWriter() throws IOException;

  PageInputStream getInputStream(PageIdentifier id) throws IOException;
}
