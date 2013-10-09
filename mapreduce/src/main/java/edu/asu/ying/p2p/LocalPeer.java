package edu.asu.ying.p2p;

import java.io.IOException;
import java.net.URI;
import java.util.Collection;

import edu.asu.ying.database.page.ServerPageSink;
import edu.asu.ying.mapreduce.mapreduce.scheduling.LocalScheduler;
import edu.asu.ying.p2p.rmi.Exportable;
import edu.asu.ying.p2p.rmi.RMIActivator;
import edu.asu.ying.p2p.rmi.RemoteImportException;


/**
 * Provides an interface to the local node and its listening facilities.
 */
public interface LocalPeer extends Exportable<RemotePeer> {

  /**
   * Gets the unique network identifier for this peer.
   */
  PeerIdentifier getIdentifier();

  /**
   * Connects this peer to an existing network of peers via the address of a peer in the network.
   *
   * @param bootstrap the address of a peer in an existing network to join.
   */
  void join(final URI bootstrap) throws IOException;

  /**
   * Gets the peers to which this peer is directly connected.
   */
  Collection<RemotePeer> getNeighbors();

  /**
   * Gets the activator capable of exposing objects to the network through this peer.
   */
  RMIActivator getActivator();

  /**
   * Gets the local scheduler which creates and manages tasks on this peer.
   */
  LocalScheduler getScheduler();

  /**
   * Gets a sink which exports pages to the network via this peer.
   */
  ServerPageSink getPageSink();

  /**
   * Finds a peer on any network of which this peer is a part.
   *
   * @return a public, network accessible, interface to the remote peer, if found.
   */
  RemotePeer findPeer(final PeerIdentifier identifier) throws PeerNotFoundException,
                                                              RemoteImportException;
}
