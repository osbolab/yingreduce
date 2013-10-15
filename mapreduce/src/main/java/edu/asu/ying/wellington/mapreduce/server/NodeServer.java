package edu.asu.ying.wellington.mapreduce.server;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.ExportException;
import java.util.ArrayList;
import java.util.List;

import edu.asu.ying.p2p.LocalPeer;
import edu.asu.ying.p2p.RemotePeer;

/**
 * {@code NodeServer} is the layer between the network and the mapreduce services. The server
 * implements the {@link LocalNode} and {@link RemoteNode} interfaces.
 */
@Singleton
public final class NodeServer implements LocalNode {

  // Network layer
  private final LocalPeer localPeer;
  // Exported to network
  private final RemoteNode remoteNode;

  // Service layer
  private final NodeIdentifier identifier;


  @Inject
  private NodeServer(LocalPeer localPeer) {

    this.localPeer = localPeer;
    // Use the same node identifier as the underlying P2P node
    this.identifier = NodeIdentifier.forString(localPeer.getIdentifier().toString());

    try {
      this.remoteNode = localPeer.getActivator()
          .bind(RemoteNode.class)
          .to(this)
          .wrappedBy(new RemoteNodeWrapperFactory());

    } catch (ExportException e) {
      throw new RuntimeException("Failed to export remote node reference", e);
    }
  }

  @Override
  public NodeIdentifier getID() {
    return identifier;
  }

  @Override
  public RemoteNode getAsRemote() {
    return remoteNode;
  }

  @Override
  public RemoteNode findNode(String searchKey) throws IOException {
    return localPeer.findPeer(searchKey).getReference(RemoteNode.class);
  }

  @Override
  public List<RemoteNode> findNodes(String searchKey, int count) throws IOException {
    List<RemoteNode> nodes = new ArrayList<>();
    for (RemotePeer peer : localPeer.findPeers(searchKey, count)) {
      try {
        nodes.add(peer.getReference(RemoteNode.class));
      } catch (RemoteException e) {
        // TODO: Logging
        e.printStackTrace();
      }
    }
    return nodes;
  }

  @Override
  public List<RemoteNode> getNeighbors() {
    List<RemoteNode> neighbors = new ArrayList<>();
    for (RemotePeer peer : localPeer.getNeighbors()) {
      try {
        neighbors.add(peer.getReference(RemoteNode.class));
      } catch (RemoteException e) {
        // TODO: Logging
        e.printStackTrace();
      }
    }
    return neighbors;
  }
}
