package edu.asu.ying.mapreduce.net.resource;

import edu.asu.ying.mapreduce.messaging.Message;

import java.net.URISyntaxException;


/**
 * {@link ResourceResponse} is sent in response to {@link ResourceRequest} and contains either a reference to the
 * resource or an exception.
 * <p>
 * The ID of this message will be the same as that of the {@link ResourceRequest} that instigated it.
 * <p>
 * The following properties are defined on this message:
 * <ul>
 *     <li>{@code resource.reference} - (optional) the resource reference, if found.</li>
 *     <li>{@code throwable} - (optional) the exception if one was thrown.</li>
 * </ul>
 */
public final class ResourceResponse
	extends ResourceMessage
{
	public static ResourceResponse inResponseTo(final Message request)
			throws URISyntaxException {
		return new ResourceResponse(request);
	}
	public static ResourceResponse inResponseTo(final Message request, final RemoteResource resource)
			throws URISyntaxException {
		return new ResourceResponse(request, resource);
	}
	public static ResourceResponse inResponseTo(final Message request, final Throwable throwable)
			throws URISyntaxException {
		return new ResourceResponse(request, throwable);
	}

	public static final class Property {
		public static final String ResourceInstance = "resource.instance.ref";
	}

	private ResourceResponse(final Message request)
			throws URISyntaxException {

		super(request.getSourceUri());
		this.setId(request.getId());
	}
	/**
	 * Initializes the response with a resource reference.
	 * @param request the message to which this is a response.
	 * @param resource the resource reference.
	 */
	private ResourceResponse(final Message request, final RemoteResource resource)
			throws URISyntaxException {

		this(request);
		this.setResourceInstance(resource);
	}
	/**
	 * Initializes an exceptional response.
	 * @param request the message to which this is a response.
	 * @param throwable the throwable to return instead of the resource.
	 */
	private ResourceResponse(final Message request, final Throwable throwable)
			throws URISyntaxException {

		this(request);
		this.setException(throwable);
	}

	protected final void setResourceInstance(final RemoteResource resource) {
		this.properties.put(Property.ResourceInstance, resource);
	}
	public final RemoteResource getResourceInstance() {
		return this.properties.getDynamicCast(Property.ResourceInstance, RemoteResource.class);
	}
}
