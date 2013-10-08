package edu.asu.ying.mapreduce.daemon;

import java.io.IOException;
import java.net.URI;

import edu.asu.ying.p2p.LocalNode;
import edu.asu.ying.p2p.node.kad.KadLocalNode;

/**
 *
 */
public final class Daemon {

  private final int port;
  private LocalNode localNode;

  public Daemon(final int port) {
    this.port = port;
    try {
      this.localNode = new KadLocalNode(port);
    } catch (final InstantiationException e) {
      e.printStackTrace();
    }
  }

  public final void join(final Daemon instance) {
    try {
      this.localNode.join(
          URI.create("//127.0.0.1:".concat(String.valueOf(instance.getPort()))));
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public final void join(final URI bootstrap) {
    try {
      this.localNode.join(bootstrap);
    } catch (final IOException e) {
      e.printStackTrace();
    }
  }

  public final LocalNode getLocalNode() {
    return this.localNode;
  }

  public final int getPort() {
    return this.port;
  }
}
