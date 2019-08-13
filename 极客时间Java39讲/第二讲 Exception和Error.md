# 极客时间Java核心技术39讲 Exception 和 Error

## 1.请对比 Exception 和 Error，另外，运行时异常与一般异常有什么区别？

* Exception 和 Error 都是继承了 Throwable 类，在 Java 中只有 Throwable 类型的实例才可以被抛出（throw）或者捕获（catch），它是异常处理机制的基本组成类型。

* Exception 和 Error 体现了 Java 平台设计者对不同异常情况的分类。Exception 是程序正常运 行中，可以预料的意外情况，可能并且应该被捕获，进行相应处理。

* Error 是指在正常情况下，不大可能出现的情况，绝大部分的 Error 都会导致程序（比如 JVM 自身）处于非正常的、不可恢复状态。既然是非正常情况，所以不便于也不需要捕获，常见的比 如 OutOfMemoryError 之类，都是 Error 的子类。
* Exception 又分为可检查（checked）异常和不检查（unchecked）异常，可检查异常在源代码 里必须显式地进行捕获处理，这是编译期检查的一部分。前面我介绍的不可查的 Error，是 Throwable 不是 Exception。
* 不检查异常就是所谓的运行时异常，类似 NullPointerException、 ArrayIndexOutOfBoundsException 之类，通常是可以编码避免的逻辑错误，具体根据需要来 判断是否需要捕获，并不会在编译期强制要求。

## 2.异常处理的两个基本原则

* 尽量不要捕获类似 Exception 这样的通用异常，而是应该捕获特定异常,进一步讲，除非深思熟虑了，否则不要捕获 Throwable 或者 Error，这样很难保证我们能够正 确程序处理 OutOfMemoryError

* 不要生吞（swallow）异常。这是异常处理中要特别注意的事情，因为很可能会导致非常 难以诊断的诡异情况。

