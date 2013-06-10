package edu.asu.ying.mapreduce;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.*;

import edu.asu.ying.mapreduce.ui.Observable;
import edu.asu.ying.mapreduce.ui.ObservableProperties;
import edu.asu.ying.mapreduce.io.DelimitedTextSource;
import edu.asu.ying.mapreduce.io.table.SimpleServerTableProxyProvider;
import edu.asu.ying.mapreduce.rpc.channels.kad.KadReceiveChannel;
import edu.asu.ying.mapreduce.rpc.channels.kad.KadSendChannel;
import edu.asu.ying.mapreduce.rpc.messaging.ExceptionMessage;
import edu.asu.ying.mapreduce.rpc.messaging.Message;
import edu.asu.ying.mapreduce.rpc.messaging.MessageDispatch;
import edu.asu.ying.mapreduce.rpc.messaging.PageGetRequest;
import edu.asu.ying.mapreduce.rpc.messaging.PageGetResponse;
import edu.asu.ying.mapreduce.table.*;
import edu.asu.ying.mapreduce.logging.Logger;

/**
 * Manages the hosting of tables on the node.
 */
public final class TableServerDaemon
	implements Observable
{
	private final Map<String, Serializable> properties;
	
	// Provides proxies for table data storage
	private ServerTableProxyProvider proxyProvider;
	// Provides table instances to handle messages
	private ServerTableProvider tableProvider;
	
	// Redirects messages to appropriate handlers
	private MessageDispatch messageDispatch;
	// Listens for messages from the network and sends them to the dispatch
	private KadReceiveChannel receiveChannel;
	// Connects to an existing network and sends messages to it
	private KadSendChannel sendChannel;
	
	public TableServerDaemon() {
		this(null);
	}
	public TableServerDaemon(final Map<String, Serializable> properties) {
		if (properties != null) {
			this.properties = properties;
		} else {
			this.properties = new HashMap<String, Serializable>();
		}
		
		this.init();
	}
	
	private final void init() {
		Logger.get().info("Starting Kademlia server");
		// The default implementation creates a folder for each table
		// and a file for each page
		// TODO: Use Guice here
		this.proxyProvider = new SimpleServerTableProxyProvider();
		// The table provider will serve tables that use the proxy provider
		this.tableProvider = new ServerTableProvider(this.proxyProvider);
		// The table provider will listen for table messages from the dispatch
		this.messageDispatch = new MessageDispatch();
		this.tableProvider.registerForMessages(this.messageDispatch);
		// The receive channel will pipe incoming messages to the dispatch
		// The default implementation doesn't join any network to begin
		this.receiveChannel = new KadReceiveChannel(this.messageDispatch);
		
		final int serverPort = (Integer) this.receiveChannel.getTransportSink().getProperties().get("port");
		Logger.get().info("Kademlia server is listening on port ".concat(String.valueOf(serverPort)));
		
		this.sendChannel = new KadSendChannel();
	}
	
	public void stop() {
		this.receiveChannel.close();
	}
	
	public final SimpleServerTableProxyProvider getProxyProvider() { return (SimpleServerTableProxyProvider) this.proxyProvider; }
	
	public final ElementSource getElementSource(final TableID tableId, final InputStream stream) {
		final ClientTable table = new ClientTable(tableId, this.getSendChannel().getMessageSink());
		return new DelimitedTextSource(stream, table, '\n', ',');
	}
	
	public final InputStream getTableStream(final TableID tableId) {
		final List<Page> pages = new ArrayList<Page>();
		
		int totalPages = 5;
		for (int i = 0; ; i++) {
			final PageGetRequest pageGet = new PageGetRequest(tableId, i);
			final Message resp = this.sendChannel.getMessageSink().processMessage(pageGet);
			if (resp instanceof ExceptionMessage) {
				break;
			}
			final Page page = ((PageGetResponse)resp).getPage();
			totalPages = Math.max(totalPages, page.getNumPages());
			pages.add(page);
		}
		
		final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final PrintWriter writer = new PrintWriter(outStream);
		
		for (final Page page : pages) {
			for (final Map.Entry<Serializable, Serializable> entry : page.entrySet()) {
				writer.write(String.format("%s=%s\n", entry.getKey(), entry.getValue()));
			}
		}
		writer.flush();
		
		return new ByteArrayInputStream(outStream.toByteArray());
	}
	
	@Override
	public List<ObservableProperties> getExposedProps() {
		return this.receiveChannel.getExposedProps();
	}
	
	public final KadReceiveChannel getReceiveChannel() { return this.receiveChannel; }
	public final KadSendChannel getSendChannel() { return this.sendChannel; }
}