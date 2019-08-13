# Java NIO

## 1. NIO 与 IO 的主要区别

| IO                       |        NIO               |
| ------                   | ------                    |
| 面向流(Stream Oriented)  | 面向缓冲区(Buffer Oriented)|
| 阻塞IO(Blocking IO)      | 非阻塞IO(Non Blocking IO) |
| (无)                     |   选择器(Selectors)       |

## 2. 通道和缓冲区

* 通道表示打开到 IO 设备(例如：文件、 套接字)的连接。
* 缓冲区（Buffer）：一个用于特定基本数据类型的容器。

## 3. 缓冲区（Buffer）

* Buffer 就像一个数组，可以保存多个相同类型的数据根据数据类型不同(boolean 除外) ，有以下 Buffer 常用子类：
  * ByteBuffer
  * CharBuffer
  * ShortBuffer
  * IntBuffer
  * LongBuffer
  * FloatBuffer
  * DoubleBuffer

* Buffer 的常用方法
  * Buffer clear() ：清空缓冲区并返回对缓冲区的引用
  * Buffer flip() ：将缓冲区的界限设置为当前位置，并将当前位置充值为 0
  * int limit() ：返回 Buffer 的界限(limit) 的位置

* 缓冲区的数据操作
  * 获取 Buffer 中的数据 : get()
  * 放入数据到 Buffer 中 : put()

* 直接与非直接缓冲区
  * 直接字节缓冲区 : JVM会尽最大努力直接在此缓冲区上执行本机 I/O 操作。也就是说,JVM会尽量避免将缓冲区的内容复制到中间缓冲区中
  * 直接字节缓冲区可以通过调用此类的 allocateDirect() 工厂方法来创建。
  * 直接字节缓冲区还可以通过FileChannel 的 map() 方法 将文件区域直接映射到内存中来创建

## 4. 通道（Channel）

* Channel 表示 IO 源与目标打开的连接。 Channel 类似于传统的“流”。只不过 Channel 本身不能直接访问数据，Channel 只能与 Buffer 进行交互。

* Channel 接口提供的最主要实现类如下：
  * FileChannel：用于读取、写入、映射和操作文件的通道。
  * DatagramChannel：通过 UDP 读写网络中的数据通道。
  * SocketChannel：通过 TCP 读写网络中的数据。
  * ServerSocketChannel：可以监听新进来的 TCP 连接，对每一个新进来的连接都会创建一SocketChannel

* 获取通道
  * 获取通道的一种方式是对支持通道的对象调用getChannel() 方法。
  * 获取通道的其他方式是使用 Files 类的静态方法 newByteChannel() 获取字节通道。或者通过通道的静态方法 open() 打开并返回指定通道。

* transferFrom() 和 transferTo()
  * 将数据从源通道传输到其他 Channel 中

## 5. NIO 的非阻塞式网络通信

* 传统的 IO 流都是阻塞式的 ：当一个线程调用 read()或 write() 时，该线程被阻塞，直到有一些数据被读取或写入，该线程在此期间不能执行其他任务。
* Java NIO 是非阻塞模式的 ：当线程从某通道进行读写数据时，若没有数据可用时，该线程可以进行其他任务。
* 选择器（Selector）
  * Selector 可 以同时监控多个SelectableChannel 的 IO 状况，也就是说，利用 Selector 可使一个单独的线程管理多个 Channel。Selector 是非阻塞 IO 的核心。
