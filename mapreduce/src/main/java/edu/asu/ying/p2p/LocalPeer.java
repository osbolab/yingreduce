package edu.asu.ying.p2p;

import java.io.IOException;
import java.net.URI;
import java.net.UnknownHostException;
import java.util.Collection;

import edu.asu.ying.mapreduce.common.RemoteSink;
import edu.asu.ying.mapreduce.database.page.Page;
import edu.asu.ying.mapreduce.mapreduce.scheduling.LocalScheduler;
import edu.asu.ying.p2p.rmi.RMIActivator;


/**
 * Provides an interface to the local node and its listening facilities.
 */
public interface LocalPeer {

  void join(final URI bootstrap) throws IOException;

  Collection<RemotePeer> getNeighbors();

  RMIActivator getActivator();

  LocalScheduler getScheduler();

  RemoteSink<Page> getPageSink();

  RemotePeer findNode(final PeerIdentifier identifier) throws UnknownHostException;

  PeerIdentifier getIdentifier();

  RemotePeer getProxy();
}