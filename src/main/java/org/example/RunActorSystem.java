package org.example;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class RunActorSystem {

	public static void main(String[] args) {
		
		final ActorSystem system = ActorSystem.create("my-actor-system");
		final ActorRef helloActor =  system.actorOf(Props.create(HelloWorldActor.class), "my-actor");
		helloActor.tell(new HelloWorldMsgProtocol.SayHello("World"), null);
		helloActor.tell(new HelloWorldMsgProtocol.SayHello("World Again"), null);
	}

}
