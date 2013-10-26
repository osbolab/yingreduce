package edu.asu.ying.wellington.dfs.server;

import javax.annotation.Nullable;

import edu.asu.ying.wellington.dfs.PageIdentifier;
import edu.asu.ying.wellington.mapreduce.server.RemoteNode;

/**
 * {@code PageResponsibilityRecord} tracks a node which is responsible for (carries a copy of,
 * and is viable for task regarding) a page.
 */
public final class PageResponsibilityRecord {

  // The node responsible
  private final String nodeName;
  // The node reference, if available
  @Nullable
  private RemoteNode node;
  // The page for which the node is responsible
  private final PageIdentifier pageId;

  public PageResponsibilityRecord(PageIdentifier pageId, String nodeName) {
    this.pageId = pageId;
    this.nodeName = nodeName;
  }

  public PageResponsibilityRecord(PageIdentifier pageId, String nodeName,
                                  @Nullable RemoteNode node) {
    this(pageId, nodeName);
    this.node = node;
  }

  public String getNodeName() {
    return nodeName;
  }

  public void setNode(@Nullable RemoteNode node) {
    this.node = node;
  }

  @Nullable
  public RemoteNode getNode() {
    return node;
  }

  public PageIdentifier getPageId() {
    return pageId;
  }
}