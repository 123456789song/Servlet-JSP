JAVA I/O流
输入输出流：可以从其中读入一个字节序列的对象叫输入流，可以向其中写入一个字节序列的  
对象的叫输出流
1. 抽象输入流类InputStream：abstract int read()表示读入一个字节，返回读入的字节  
   具体输入流类FileInputStream：read()表示从某个文件中读入一个字节  
2. 抽象输出流类OutputStream:abstract void write(int b)表示向某个位置写一个字节    
   具体输出流类FileOutputStream：
3. close()关闭I/O流 ; flush()刷新缓冲区
4. OutputStreamWriter类使用指定的字符编码方式，把Unicode字符流转化为字节流
5. InputStreamWriter类将包含字节的输入流转化为可以产生Unicode码元的读入器
6. 以二进制读写数据DataOutputStram和DataInputStream
7. 写出文本的输出,以文本格式打印字符串和数字

    PrintWriter out = new PrintWriter("test.txt");
    out.println("text");        
8. 读入文本输入，以文本格式写入数据

//IO常用的类

    File  文件类(文件特征与管理)
    InputStream  字节输入流(二进制格式操作)
    OutputStream 字节输出流(二进制格式操作)
    Reader 字符输入流(文件格式操作)
    Writer 字符输出流 (文件格式操作)  
    RandomAccessFile（随机文件操作）     
                    

  
![avatar](https://s1.ax1x.com/2018/09/19/imC3fs.png)  
![avatar](https://s1.ax1x.com/2018/09/19/imCGpn.png)  





        
            
