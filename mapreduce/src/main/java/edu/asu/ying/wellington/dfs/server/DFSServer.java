package edu.asu.ying.wellington.dfs.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.asu.ying.wellington.dfs.DFSService;
import edu.asu.ying.wellington.dfs.table.Table;
import edu.asu.ying.wellington.dfs.table.TableIdentifier;
import edu.asu.ying.wellington.dfs.table.TableNotFoundException;
import edu.asu.ying.wellington.mapreduce.server.LocalNode;

/**
 *
 */
@Singleton
public class DFSServer implements DFSService {

  private final LocalNode localNode;

  @Inject
  private DFSServer(LocalNode localNode) {
    this.localNode = localNode;
  }

  @Override
  public void start() {
  }

  @Override
  public Table getTable(TableIdentifier id) throws TableNotFoundException {
    return null;
  }
}
