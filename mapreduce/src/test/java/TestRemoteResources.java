import com.google.inject.*;
import edu.asu.ying.mapreduce.common.events.FilteredValueEvent;
import edu.asu.ying.mapreduce.common.events.FilteredValueEventBase;
import edu.asu.ying.mapreduce.io.MessageOutputStream;
import edu.asu.ying.mapreduce.net.messaging.Message;
import edu.asu.ying.mapreduce.io.SendMessageStream;
import edu.asu.ying.mapreduce.net.resources.ResourceMessageEvent;
import edu.asu.ying.mapreduce.net.resources.client.RemoteResourceFinder;
import edu.asu.ying.mapreduce.rmi.activator.Activator;
import edu.asu.ying.mapreduce.net.resources.ResourceIdentifier;
import edu.asu.ying.mapreduce.net.resources.ResourceResponse;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;


/**
 * Installs a mock {@link edu.asu.ying.mapreduce.io.MessageOutputStream} that tests instead of sending data.
 */
public class TestRemoteResources
		extends AbstractModule
{
	// Simulates a message handler getting messages from the network
	private final FilteredValueEvent<Message> onIncomingMessages = new FilteredValueEventBase<>();

	private class MockActivator
			implements Activator {
		private final int i;
		public MockActivator(final int i) {
			this.i = i;
		}
		@Override
		public <T extends Remote> T getReference(final Class<T> type, final Map<String, String> properties)
				throws RemoteException { return null; }
		@Override
		public String echo(final String message) throws RemoteException {
			return message.concat(String.valueOf(this.i));
		}
		@Override
		public ResourceIdentifier getResourceUri() throws RemoteException {
			return null;
		}
	}

	private class MockOutputStream
			implements MessageOutputStream {
		@Override
		public int write(final Message message) throws IOException {
			try {
				message.setSourceUri(new ResourceIdentifier("node\\localhost"));
				for (int i = 0; i < message.getReplication(); i++) {
					TestRemoteResources.this.onIncomingMessages.fire(this, ResourceResponse.inResponseTo(message,
				                                                                                     new MockActivator(i))
																	);

				}
				return message.getReplication();
			} catch (final URISyntaxException e) {
					throw new IOException(e);
			}
		}
	}

	@Override
	protected void configure() {
		bind(MessageOutputStream.class).annotatedWith(SendMessageStream.class).toInstance(new MockOutputStream());
		bind(RemoteResourceFinder.class);
	}

	@Provides
	@ResourceMessageEvent
	private FilteredValueEvent<Message> provideIncomingMessagesEvent() {
		return this.onIncomingMessages;
	}

	@Provides
	private RemoteResourceFinder<?> provideRemoteResources() {
		return (Guice.createInjector(this)).getInstance(RemoteResourceFinder.class);
	}

	@Test
	public void ItSendsRequests() throws Exception {
		Guice.createInjector(this).getInstance(RemoteResourcesTester.class).Test();
	}
}
