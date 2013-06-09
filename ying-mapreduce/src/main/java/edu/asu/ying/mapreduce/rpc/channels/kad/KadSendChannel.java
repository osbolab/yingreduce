package edu.asu.ying.mapreduce.rpc.channels.kad;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import edu.asu.ying.mapreduce.rpc.channels.*;
import edu.asu.ying.mapreduce.rpc.formatting.SimpleSendFormatterSink;
import edu.asu.ying.mapreduce.rpc.messaging.MessageSink;
import edu.asu.ying.mapreduce.rpc.net.NodeNotFoundException;
import edu.asu.ying.mapreduce.ui.ObservableProperties;


/**
 * {@link KadSendChannel} creates a channel for {@link Message} objects to be serialized
 * across a Kademlia network.
 * <p>
 * The channel uses {@link SimpleSendFormatterSink} for serialization and
 * {@link KadSendTransportSink} for network transmission.
 */
public final class KadSendChannel
	implements SendChannel
{
	private final Map<? extends Serializable, ? extends Serializable> properties = new HashMap<Serializable, Serializable>();
	
	private final SimpleSendFormatterSink formatterSink;
	private final SendChannelTransportSink transportSink;
	
	/**
	 * Initialize the channel with the address of a node of the Kademlia network.
	 * @param remoteNode the address of a remote node of the network used to bootstrap
	 * our network connection.
	 * @throws URISyntaxException if the remote node address is not a valid URI.
	 * @throws NodeNotFoundException if the remote node cannot be found
	 */
	public KadSendChannel(final URI remoteNode) 
			throws URISyntaxException, NodeNotFoundException {
		this.transportSink = new KadSendTransportSink(remoteNode);
		this.formatterSink = new SimpleSendFormatterSink(this.transportSink);
	}
	
	public void close() {
		this.transportSink.close();
	}
	
	/**
	 * Returns a message sink that sends messages through this channel.
	 */
	@Override
	public MessageSink getMessageSink() { return this.formatterSink; }

	@Override
	public Map<? extends Serializable, ? extends Serializable> getProperties() { return this.properties; }

	@Override
	public SendChannelTransportSink getTransportSink() { return this.transportSink; }
	
	@Override
	public final ObservableProperties getExposedProps() {
		return this.transportSink.getExposedProps();
	}
}
