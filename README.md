# RRPC
Rafa's Remote Procedure Call implementation using a custom protocol. It allows a client to connect to a server and execute functions which are implemented in the server, so that the client only has to provide the function to be executed and its methods and the server will respond with a result.

Unlike XML-RPC, this protocol uses interfaces that the client has, so that the IDE can help the developer when calling specific methods, instead of passing the method to be executed as a string.
