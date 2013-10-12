package edu.asu.ying.wellington.dfs.table;

import java.util.UUID;

import edu.asu.ying.wellington.Identifier;

/**
 *
 */
public final class TableIdentifier extends Identifier {

  public static TableIdentifier random() {
    return new TableIdentifier(UUID.randomUUID().toString());
  }

  private static final long SerialVersionUID = 1L;

  private static final String TABLE_PREFIX = "tab";

  public TableIdentifier(String id) {
    super(TABLE_PREFIX, id);
  }

  public TableIdentifier forPage(int index) {
    return new TableIdentifier(this.id.concat("~").concat(Integer.toString(index)));
  }
}