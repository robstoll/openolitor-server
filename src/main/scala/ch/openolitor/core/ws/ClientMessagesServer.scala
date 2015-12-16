/*                                                                           *\
*    ____                   ____  ___ __                                      *
*   / __ \____  ___  ____  / __ \/ (_) /_____  _____                          *
*  / / / / __ \/ _ \/ __ \/ / / / / / __/ __ \/ ___/   OpenOlitor             *
* / /_/ / /_/ /  __/ / / / /_/ / / / /_/ /_/ / /       contributed by tegonal *
* \____/ .___/\___/_/ /_/\____/_/_/\__/\____/_/        http://openolitor.ch   *
*     /_/                                                                     *
*                                                                             *
* This program is free software: you can redistribute it and/or modify it     *
* under the terms of the GNU General Public License as published by           *
* the Free Software Foundation, either version 3 of the License,              *
* or (at your option) any later version.                                      *
*                                                                             *
* This program is distributed in the hope that it will be useful, but         *
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY  *
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for *
* more details.                                                               *
*                                                                             *
* You should have received a copy of the GNU General Public License along     *
* with this program. If not, see http://www.gnu.org/licenses/                 *
*                                                                             *
\*                                                                           */
package ch.openolitor.core.ws

import akka.actor._
import spray.can.Http
import ClientMessages._
import ch.openolitor.core._
import ch.openolitor.core.models.UserId
import ch.openolitor.core.ws.ControlCommands.SendToClient
import ch.openolitor.core.ws.ClientMessagesWorker.Push
import spray.json.RootJsonWriter

trait ClientReceiverComponent {
  val clientReceiver: ClientReceiver
}

object ControlCommands {
  case class SendToClient(senderUserId: UserId, msg: String, receivers: List[UserId] = Nil)
}

trait ClientReceiver extends EventStream {

  def broadcast[M >: ClientMessage](senderUserId: UserId, msg: M)(implicit writer: RootJsonWriter[M]) =
    publish(SendToClient(senderUserId, msgToString(msg)))

  /**
   * Send OutEvent to a list of receiving clients exclusing sender itself
   */
  def send[M](senderUserId: UserId, msg: M, receivers: List[UserId])(implicit writer: RootJsonWriter[M]) =
    publish(SendToClient(senderUserId, msgToString(msg), receivers))

  def send[M](senderUserId: UserId, msg: M)(implicit writer: RootJsonWriter[M]): Unit =
    send(senderUserId, msg, List(senderUserId))

  def msgToString[M](msg: M)(implicit writer: RootJsonWriter[M]) =
    writer.write(msg).compactPrint
}

object ClientMessagesServer {
  def props() = Props(classOf[ClientMessagesServer])
}
class ClientMessagesServer extends Actor with ActorLogging {

  import ClientMessagesJsonProtocol._

  def actorName(id: UserId): String = "uid:" + id.id.toString

  override def preStart() {
    super.preStart()
    //register ourself as listener to sendtoclient commands
    context.system.eventStream.subscribe(self, classOf[SendToClient])
  }

  override def postStop() {
    context.system.eventStream.unsubscribe(self, classOf[SendToClient])
    super.postStop()
  }

  def receive = {
    // when a new connection comes in we register a WebSocketConnection actor as the per connection handler
    case Http.Connected(remoteAddress, localAddress) =>
      val serverConnection = sender()

      //...how to we get userid?
      val userId = Boot.systemUserId

      val conn = context.actorOf(ClientMessagesWorker.props(serverConnection), actorName(userId))
      serverConnection ! Http.Register(conn)

    case SendToClient(senderUserId, msg, Nil) =>
      //broadcast to all      
      log.debug(s"Broadcast client message:$msg")
      context.children.map(c => c ! Push(msg))
    case SendToClient(senderUserId, msg, receivers) =>
      //send to specific clients only
      log.debug(s"send client message:$msg:$receivers")
      receivers.map { receiver =>
        context.child(actorName(receiver)).map(_ ! Push(msg))
      }
  }
}