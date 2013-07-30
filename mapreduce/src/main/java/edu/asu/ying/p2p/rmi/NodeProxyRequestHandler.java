package edu.asu.ying.p2p.rmi;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.rmi.RemoteException;

import edu.asu.ying.p2p.LocalNode;
import edu.asu.ying.mapreduce.node.io.Channel;
import edu.asu.ying.mapreduce.node.io.MessageHandler;
import edu.asu.ying.mapreduce.node.io.message.Message;
import edu.asu.ying.mapreduce.node.io.message.ResponseMessage;

public final class NodeProxyRequestHandler implements MessageHandler {

  public static NodeProxyRequestHandler exposeNodeToChannel(final LocalNode node,
                                                            final Channel networkChannel) {
    return new NodeProxyRequestHandler(node, networkChannel);
  }

  private final LocalNode localNode;
  private final NodeProxy proxyInstance;

  private final String tag = "node.remote-proxy";

  private NodeProxyRequestHandler(final LocalNode localNode, final Channel networkChannel) {
    this.localNode = localNode;
    try {
      this.proxyInstance = ServerNodeProxy.createProxyTo(this.localNode);
    } catch (final RemoteException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }

    networkChannel.registerMessageHandler(this, this.tag);
  }

  @Override
  public void onIncomingMessage(final Message message) {
    // TODO: Logging
    throw new NotImplementedException();
  }

  @Override
  public Message onIncomingRequest(final Message request) {
    final ResponseMessage response = ResponseMessage.inResponseTo(request);

    // Bind the proxy to the local scheduler
    response.setContent(proxyInstance);

    return response;
  }

  @Override
  public final String getTag() {
    return this.tag;
  }
}