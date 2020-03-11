# -
分布式锁






介绍
distributedLock提供了两种形式的分布式锁redis和zk
redis:支持可重入、加锁自动默认30秒过期（如30秒内任务未执行完成，锁的时间会自动延期）。

zk：支持可重入，加锁则在该key下面创建临时顺序节点，按节点的顺序从小到大执行，保证了锁得公平性，性能开销小，客户端会话失效，锁自动释放

使用说明：


引入pom依赖
<!--分布式锁-->
<dependency>

      <groupId>com.xiaoying</groupId>
      <artifactId>base-common-distributedLock</artifactId>
      <version>1.0.5-RELEASE</version>

</dependency>



注：1. 分布式锁组件中的netty相关包会跟cat的包冲突，所以接入时需排除cat相关netty包

 <!--cat-->
<dependency>
      <groupId>com.xiaoying</groupId>
      <artifactId>cat-client</artifactId>
      <exclusions>
        <exclusion>
          <artifactId>netty-all</artifactId>
          <groupId>io.netty</groupId>
        </exclusion>
      </exclusions>
</dependency>

2. curator-client，curator-framework 版本为2.10.0，若jar包中有相应的不同版本的包，为避免冲突请移除

<exclusions>
      <exclusion>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-client</artifactId>
      </exclusion>
      <exclusion>
            <groupId>org.apache.curator</groupId>
            <artifactId>curator-framework</artifactId>
      </exclusion>
</exclusions>

两种使用方法：


1.实例化分布式锁对象

Dlock dlock=new Dlock(“key”, LockType.redis);

通过初始化Dlock生成一个分布式锁对象，构造参数为key（锁的键值）和LockType(锁种类：LockType.redis、LockType.zk)



Dlock内部提供了四种方法：

//阻塞的获取锁，如果获取到锁，从该方法返回
void lock();

//尝试非阻塞的获取锁，调用方法之后立马进行返回，如果能获取返回true,不能则返回false
boolean tryLock();

//可超时的尝试获取锁，如果能获取返回true,不能则返回false
boolean tryLock(Long waitTime, TimeUnit unit);

//释放锁
void unlock();



注：组件需要读取META-INF/app.properties中的app.name(如没有该文件请在key中加上项目名称作为前缀，保证所有项目key的唯一性)

2. 使用@DistributedLock注解
sping boot启动分布式锁自动配置
@EnableLock
 public class ApplicationBootstrap {
     public static void main(String[] args) {
      SpringApplication.run(ApplicationBootstrap.class, args);
     }
 }

sping xml启动分布式锁自动配置

<beans xmlns="http://www.springframework.org/schema/beans"
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:lock="http://www.xiaoying.tv/schema/lock"
xsi:schemaLocation="http://www.springframework.org/schema/beans
http://www.springframework.org/schema/beans/spring-beans-3.1.xsd
http://www.xiaoying.tv/schema/lock http://www.xiaoying.tv/schema/lock/lock-enable.xsd">
   <lock:enable>
   </lock:enable>
</beans>

推荐使用idea 输入<lock:enable></lock:enable>会自动提示引入配置





使用注解在需要方法上加锁，方法执行完成后自动解锁，如下：

注：通过@DistributedLock 加锁的方法必须使用@LockKey标明参数（否则启动会报错）。该锁为阻塞获取锁，若获取不到，则一直阻塞等待，获取到锁，直接返回



LockKey为参数时：
@DistributedLock(key = "method", lockType = LockType.redis)
public void method(@LockKey string duid);


LockKey为对象的话，可以在变量上添加@LockKey注解，如：
public class request {
  @LockKey
  public Long auid;
}


组件默认使用META-INF/app.properties中的app.name(如没有该文件推荐key中加上项目名称作为前缀，保证所有项目key的唯一性)和@DistributedLock中的key以及加锁方法中添加@LockKey的参数拼接为锁的键值



@DistributedLock注解的参数定义如下：

/**
* 锁命名
*/
String key() default "";


/**
* 锁类型 redis、zk
*/
LockType lockType() default LockType.redis;



注：

分布式锁内方法接入cat
对应name
      加锁：DistributedLock.Lock
      解锁：DistributedLock.Unlock
分布式锁支持可重入，重入次数需和解锁次数相同
