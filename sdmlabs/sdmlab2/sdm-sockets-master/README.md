# SDM Lab sockets


## Ex1

Java provides a client-side socket class Socket and a server-side class ServerSocket. A server application will create an instance of a ServerSocket which will listen on a particular port. When the server accepts an incoming request a Socket object is created to encapsulate this connection and the client and the server will be able to communicate using Input and OutputStreams. On the client-side the client will create a Socket object that will open a connection to the server. The server application's address and port number must be known.
Let's implement an application where a server listens on port 2999. When a client initiates the connection the server will ask the birthdate, the client will answer in a yyyy-MM-dd format and the server will return “happy birthday” if the user celebrates his birthday today.

For the first exercise run TCPServerSimple and then TCPClientSimple in ex1 package.  

## Ex2

In the previous example the program allowed only one “conversation” between a server and a client at a time. The server exposed a single socket that was tied to a client. When the exchange of data finished, the server can expose a new socket.
We want to create a real server that exposes resources to multiple clients. This means that our server can receive multiple connections and handle the requests in separate threads. 

For the second exercise run TCPServerMulti and then run more TCPClientSimple in ex2 package.  

## Ex3

In TCP the nodes establish a connection, transmit the data, and then close the connection. All data sent over the channel is received in the same order in which it was sent. This is guaranteed by the channel. However, in UDP independent packets of information are sent called datagrams.  A datagram is an independent, self-contained message sent over the network whose arrival, arrival time, and content are not guaranteed.
Both the server and the client use DatagramSocket classes to communicate. In this case a server needs only one DatagramSocket that can send and receive packages to and from different destinations. Through DatagramSockets , DatagramPackage objects  are sent. Since we do not have a connection we must specify the address and the port of the destination for each package we send. Also, the server will require a package from the client to find out its address and port.
Let`s implement the same application as above this time using UDP and note the differences.

## Ex4

In previous TCP examples the communication was deterministic, each endpoint knew when to listen and when to write. Also while listening the thread was blocked. 
In an interactive application we cannot know in advance who will communicate, so we cannot be blocked listening on the IOStream while also being blocked on listening to the Scanner.
To solve this, each endpoint will have two threads - one listening from input from the network and one listening from input from the keyboard. 