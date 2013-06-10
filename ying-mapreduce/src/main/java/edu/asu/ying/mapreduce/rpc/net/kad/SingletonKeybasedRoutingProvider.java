package edu.asu.ying.mapreduce.rpc.net.kad;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Random;

import com.google.inject.Guice;
import com.google.inject.Injector;

import il.technion.ewolf.kbr.KeybasedRouting;
import il.technion.ewolf.kbr.openkad.KadNetModule;

/**
 * Provides instances of a local Kademlia node.
 */
public final class SingletonKeybasedRoutingProvider
	implements KeybasedRoutingProvider
{
	/*
	 * Allow sharing of a single local Kademlia node between Server and Client channels.
	 */
	private static class KeybasedRoutingContainer {
		private static final int DEFAULT_KEYSIZE = 20;
		private static final int DEFAULT_BUCKETSIZE = 20;
		private static final int DEFAULT_PORT = 5000;
		private static KeybasedRouting INSTANCE;

		/*
		 * Initialize a singleton instance of the Kademlia node
		 */
		static {
			// FIXME: Remove second testing instance
			// FIXME: Don't use default parameters
			final int rnd = (new Random()).nextInt(1000);
			Injector injector = Guice.createInjector(new KadNetModule()
				.setProperty("openkad.keyfactory.keysize", String.valueOf(DEFAULT_KEYSIZE))
				.setProperty("openkad.bucket.kbuckets.maxsize", String.valueOf(DEFAULT_BUCKETSIZE))
				.setProperty("openkad.seed", String.valueOf(DEFAULT_PORT+rnd))
				.setProperty("openkad.net.udp.port", String.valueOf(DEFAULT_PORT+rnd)));
			
			INSTANCE = injector.getInstance(KeybasedRouting.class);
		
			try {
				INSTANCE.create();
				//INSTANCE2.create();
			} catch (final IOException e) {
				throw new RuntimeException(e);
			}
		}
		private KeybasedRoutingContainer() {
			INSTANCE = null;
		}
	}
	
	/**
	 * Returns an instance of the {@link KeybasedRouting} kademlia router.
	 */
	public final KeybasedRouting getKeybasedRouting() {
		return KeybasedRoutingContainer.INSTANCE;
	}

	/**
	 * Makes a kademlia-compatible URI.
	 * <p>
	 * The simplest example is the localhost:
	 * <p>
	 * <code>//localhost:5000</code><br>
	 * <code>//127.0.0.1:5000</code>
	 * <p>
	 * become<p>
	 * <code>openkad.udp://localhost:5000/</code><br>
	 * <code>openkad.udp://127.0.0.1:5000/</code>
	 * @param uri the URI to transform. Necessary components are the host and the port. 
	 * @return a URI compatible with {@link KeybasedRouting#join}.
	 * @throws URISyntaxException
	 */
	public final URI makeURI(final URI uri) throws URISyntaxException {
		return new URI(String.format("openkad.udp://%s:%d/",
		                             uri.getHost(), uri.getPort()));
	}
	/**
	 * Makes a kademlia-compatible URI.
	 * @param address the address of the remote node.
	 * @param port the port on which the remote node has a {@link KeybasedRouting} instance
	 * listening. 
	 * @return a URI compatible with {@link KeybasedRouting#join}.
	 * @throws URISyntaxException
	 */
	public final URI makeURI(final InetAddress address, final int port) throws URISyntaxException {
		return new URI(String.format("openkad.udp://%s:%d/",
		                             address.getHostAddress(), port));
	}
}
