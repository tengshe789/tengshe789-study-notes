---
title: 我要学好分布式-RMI通信框架
date: 2018-07-26 19:28:30
tags: [技术,我要学好分布式]
---

分布式框架是最近几年的热门。可是要想理解分布式框架着实不易，为了努力跟上时代潮流，特此开了一个专题，起名“我要学好分布式”，通过博客来分享一下我的学习过程，加深我对分布式整体框架的理解。

想要解锁更多新姿势？请访问[我的博客](http://blog.tengshe789.tech/)

### 什么是RPC

英文就不说了。中文名远程进程调用协议。顾名思义，客户端在不知道细节的情况下，可以调用远程计算机的api，就像是调用本地方法一样。

RPC协议是一个规范。主流的PRC协议有`Dubbo`、`Thrif`、`RMI`、`Webservice`、`Hessain`

他又一个非常大的特点，网络协议和网络IO对于调用端和服务端来说是透明的（动态代理）

### RMI

RMI(remote method invocation)  , 可以认为是RPC的java版本 

RMI使用的是JRMP（Java Remote Messageing Protocol）, JRMP是专门为java定制的通信协议，所以他是纯java的分布式解决方案 。注意，这个RMI已经老旧过时了。

#### RMI Demo

1. 先写个测试用的远程接口，注意接口要抛异常

```java
public interface ISayHello extends Remote {
    public String satHello(String name) throws RemoteException;
}
```

​    2.实现远程接口，并且继承：`UnicastRemoteObject`

```java
public class SayHelloImpl extends UnicastRemoteObject implements ISayHello{

    protected SayHelloImpl() throws RemoteException {
    }

    public String satHello(String name) throws RemoteException {
        return "hello," + name;
    }
}
```

​     3.创建服务器程序： `createRegistry`方法注册远程对象

```java
import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class HelloServer {
    public static void main(String[] args) {
        try {
            ISayHello sayHello =new SayHelloImpl();
            LocateRegistry.createRegistry(8888);
            Naming.bind("rmi://localhost:8888/sayhello",sayHello);
            System.out.println("server start success");
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
```
​    4.创建客户端程序

```java
public class HelloClient {
    public static void main(String[] args) {
        try {
            ISayHello iSayHello = (ISayHello) Naming.lookup("rmi://localhost:8888/sayhello");
            System.out.println("hello");
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
```

#### RMI调用过程

**流程：**

1.去注册中心注册，server端启动服务。

2.注册中心联系stub（存根）。stub用于客户端 ，在j2ee中是这么说的：为屏蔽客户调用远程主机上的对象，必须提供某种方式来模拟本地对象,这种本地对象称为存根(stub),存根负责接收本地方法调用,并将它们委派给各自的具体实现对象 

3.server注册对象，然后返回注册对象

4.客户端访问注册中心，（动态代理）返回stub对象

5.stub（存根）远程调用skeleton （骨架 ）

6.skeleton 调用相应接口

#### 源码

让我看看核心的注册服务的源码实现

```java
public RegistryImpl(final int var1) throws RemoteException {
        this.bindings = new Hashtable(101);
    	//安全认证
        if (var1 == 1099 && System.getSecurityManager() != null) {
            try {
                AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                    public Void run() throws RemoteException {
                        LiveRef var1x = new LiveRef(RegistryImpl.id, var1);
                        RegistryImpl.this.setup(new UnicastServerRef(var1x, (var0) -> {
                            return RegistryImpl.registryFilter(var0);
                        }));
                        return null;
                    }
                }, (AccessControlContext)null, new SocketPermission("localhost:" + var1, "listen,accept"));
            } catch (PrivilegedActionException var3) {
                throw (RemoteException)var3.getException();
            }
        } else {
            //初始化远程引用UnicastServerRef对象
            LiveRef var2 = new LiveRef(id, var1);//《--------------------------
            this.setup(new UnicastServerRef(var2, RegistryImpl::registryFilter));
        }

    }
```

点进UnicastServerRef，找出实现的关系~

点进setup方法，用idea反编码

```java
public Remote exportObject(Remote var1, Object var2, boolean var3) throws RemoteException {
        Class var4 = var1.getClass();

        Remote var5;
        try {
            var5 = Util.createProxy(var4, this.getClientRef(), this.forceStubUse);//《--------------------
        } catch (IllegalArgumentException var7) {
            throw new ExportException("remote object implements illegal remote interface", var7);
        }

        if (var5 instanceof RemoteStub) {//《--------------------------
            this.setSkeleton(var1);
        }

        Target var6 = new Target(var1, this, var5, this.ref.getObjID(), var3);//《------------------------
        this.ref.exportObject(var6);
        this.hashToMethod_Map = (Map)hashToMethod_Maps.get(var4);
        return var5;
    }
```

发现在创建代理，判断当前的var是不是远程stub，如果是就设置骨架。如果不是，就构建target对象。点开代理

```java
public static Remote createProxy(Class<?> var0, RemoteRef var1, boolean var2) throws StubNotFoundException {
        Class var3;
        try {
            var3 = getRemoteClass(var0);//《--------------------------
        } catch (ClassNotFoundException var9) {
            throw new StubNotFoundException("object does not implement a remote interface: " + var0.getName());
        }

        if (var2 || !ignoreStubClasses && stubClassExists(var3)) {
            return createStub(var3, var1);//《--------------------------
        } else {
            final ClassLoader var4 = var0.getClassLoader();
            final Class[] var5 = getRemoteInterfaces(var0);
            final RemoteObjectInvocationHandler var6 = new RemoteObjectInvocationHandler(var1);

            try {
                return (Remote)AccessController.doPrivileged(new PrivilegedAction<Remote>() {
                    public Remote run() {
                        return (Remote)Proxy.newProxyInstance(var4, var5, var6);
                    }
                });
            } catch (IllegalArgumentException var8) {
                throw new StubNotFoundException("unable to create proxy", var8);
            }
        }
    }
```

发现在调用远程服务，然后创建了stub。继续点开getRemoteClass（）方法

```java
 private static Class<?> getRemoteClass(Class<?> var0) throws ClassNotFoundException {
        while(var0 != null) {
            Class[] var1 = var0.getInterfaces();//《--------------------------

            for(int var2 = var1.length - 1; var2 >= 0; --var2) {
                if (Remote.class.isAssignableFrom(var1[var2])) {
                    return var0;
                }
            }

            var0 = var0.getSuperclass();
        }

        throw new ClassNotFoundException("class does not implement java.rmi.Remote");
    }
```

发现现在在创建实例



好吧，回到createProxy方法，再看看顺着往下走，看看`Target var6 = new Target(var1, this, var5, this.ref.getObjID(), var3);`
        `this.ref.exportObject(var6);`的出口对象方法

```java
public void exportObject(Target var1) throws RemoteException {
        this.ep.exportObject(var1);
    }
```

```java
public interface Endpoint {
    Channel getChannel();

    void exportObject(Target var1) throws RemoteException;

    Transport getInboundTransport();

    Transport getOutboundTransport();
}
```

```java
public void exportObject(Target var1) throws RemoteException {
        this.transport.exportObject(var1);
    }
```

一路点下去，找到了tcp出口的方法。这是属于协议层的玩意。

```java
public void exportObject(Target var1) throws RemoteException {
        synchronized(this) {
            this.listen();
            ++this.exportCount;
        }
```

一路点下去，发现listen。

```java
private void listen() throws RemoteException {
        assert Thread.holdsLock(this);

        TCPEndpoint var1 = this.getEndpoint();
        int var2 = var1.getPort();
        if (this.server == null) {
            if (tcpLog.isLoggable(Log.BRIEF)) {
                tcpLog.log(Log.BRIEF, "(port " + var2 + ") create server socket");
            }

            try {
                this.server = var1.newServerSocket();//《--------------------------
                Thread var3 = (Thread)AccessController.doPrivileged(new NewThreadAction(new TCPTransport.AcceptLoop(this.server), "TCP Accept-" + var2, true));
                var3.start();
            } catch (BindException var4) {
                throw new ExportException("Port already in use: " + var2, var4);
            } catch (IOException var5) {
                throw new ExportException("Listen failed on port: " + var2, var5);
            }
        } else {
            SecurityManager var6 = System.getSecurityManager();
            if (var6 != null) {
                var6.checkListen(var2);
            }
        }
```

发现newServerSocket！！！

综上，总体流程和上图一样。

#### RMI缺陷

1.基于java，支持语言单一

2.服务注册只能注册到我上面分析的那个源码。注册中心挂了以后就完了

3.序列化是用java原生那个方法，效率不好

4.服务端底层是bio方式，性能不好

#### 手写RMI

步骤：

1.   编写服务器程序，暴露一个监听， 可以使用socket

2.   编写客户端程序，通过ip和端口连接到指定的服务器，并且将数据做封装（序列化）

3. 服务器端收到请求，先反序列化。再进行业务逻辑处理。把返回结果序列化返回



