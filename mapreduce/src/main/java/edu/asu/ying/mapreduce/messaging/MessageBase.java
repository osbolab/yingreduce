package edu.asu.ying.mapreduce.messaging;

import com.google.common.base.Optional;
import edu.asu.ying.mapreduce.common.Properties;
import edu.asu.ying.mapreduce.net.resource.ResourceIdentifier;

import java.io.Serializable;
import java.util.UUID;


/**
 * Base class for a basic {@link Message}.
 * <p>
 * The following properties are defined on this message:
 * <ul>
 *     <li>{@code message.id} - the universally unique identifier of this message.</li>
 *     <li>{@code message.uri.destination} - the URI of the node for which this message is destined.</li>
 *     <li>{@code message.uri.source} - the URI of the node from which this node originated.</li>
 *     <li>{@code message.exception} - an exception, if one was thrown.</li>
 * </ul>
 */
public abstract class MessageBase
	implements Message
{
	private static final long SerialVersionUID = 1L;

	/**
	 * Defines the keys of the properties defined by this message.
	 */
	public static final class Property {
		public static final String Scheme = "";
		public static final String MessageId = "message.id";
		public static final String DestinationURI = "message.uri.destination";
		public static final String SourceURI = "message.uri.source";
		public static final String Exception = "exception";
		public static final String Arguments = "arguments";
	}

	protected final Properties properties = new Properties();

	/*
	 * Constructors
	 */

	/**
	 * Initializes the message with a random ID.
	 */
	public MessageBase() {
		this.setId();
	}
	public MessageBase(final String id) {
		this.setId(id);
	}
	public MessageBase(final ResourceIdentifier destinationUri) {
		this.setDestinationUri(destinationUri);
	}
	public MessageBase(final String id, final ResourceIdentifier destinationUri) {
		this.setDestinationUri(destinationUri);
	}

	/*
	 * Accessors
	 */
	/**
	 * Initializes the message ID with a random {@link UUID}.
	 */
	public void setId() {
		this.setId(UUID.randomUUID().toString());
	}
	public void setId(final String id) {
		this.properties.put(Property.MessageId, id);
	}
	public void setId(final UUID id) {
		this.setId(id.toString());
	}

	@Override
	public String getId() {
		final Optional<Serializable> id = Optional.fromNullable(this.properties.get(Property.MessageId));
		if (!id.isPresent()) {
			// We can't have no id; set a random one.
			this.setId();
			return this.getId();
		}

		return String.valueOf(id.get());
	}

	@Override
	public Properties getProperties() {
		return this.properties;
	}

	public void setSourceUri(final ResourceIdentifier uri) {
		this.properties.put(Property.SourceURI, uri);
	}
	@Override
	public ResourceIdentifier getSourceUri() {
		return this.properties.getDynamicCast(Property.SourceURI, ResourceIdentifier.class);
	}

	public void setDestinationUri(final ResourceIdentifier uri) {
		this.properties.put(Property.DestinationURI, uri);
	}
	@Override
	public ResourceIdentifier getDestinationUri() {
		return this.properties.getDynamicCast(Property.DestinationURI, ResourceIdentifier.class);
	}

	public final void setException(final Throwable exception) {
		this.properties.put(Property.Exception, exception);
	}
	public final Throwable getException() {
		return this.properties.getDynamicCast(Property.Exception, Throwable.class);
	}

	public final void setArguments(final Properties args) {
		this.properties.put(Property.Arguments, args);
	}
	public final Properties getArguments() {
		return this.properties.getDynamicCast(Property.Arguments, Properties.class);
	}

	/**
	 * Replication is the maximum number of hosts matching the destination URI to which this message will be delivered.
	 * @return a number equal to or greater than 1 (default).
	 */
	@Override
	public int getReplication() {
		return this.getDestinationUri().getReplication();
	}
}