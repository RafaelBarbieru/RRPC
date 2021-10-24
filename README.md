# RRPC
Rafa's Remote Procedure Call (RRPC) is an implementation of the RPC protocol that uses a custom hand-made request-response language. It allows a client to connect to a server and execute functions which are implemented in the server, so that the client only has to provide the function to be executed and its methods and the server will respond with a result. It is meant to be a small personal project to understand how RPC works, so RRPC only allows the execution of remote methods that accept Strings and/or Integers as arguments.

Unlike XML-RPC, this protocol uses interfaces that the client has in their local machine, so that the IDE can help the developer when calling specific methods, instead of passing the method to be executed as a string.

![Client execution](./rrpc_client.jpg)

![Server execution](./rrpc_server.jpg)
