package cn.apimix.core.utils;

import cn.apimix.common.enums.HttpStatusEnum;
import cn.apimix.core.exception.HorApiException;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * @Author: Hor
 * @Date: 2025/1/23 09:42
 * @Version: 1.0
 */
@Slf4j
@Component
public class RedissonLockUtil {

    @Resource
    public RedissonClient redissonClient;

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param supplier     供应商
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                return supplier.get();
            }
            throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
        } catch (Exception e) {
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param supplier     供应商
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, String errorLogTitle, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                return supplier.get();
            }
            throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
        } catch (Exception e) {
            log.error(errorLogTitle, e);
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param supplier     供应商
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, Runnable logMessage, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                return supplier.get();
            }
            throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
        } catch (Exception e) {
            logMessage.run();
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param waitTime     等待时间
     * @param leaseTime    租赁时间
     * @param unit         单元
     * @param lockName     锁名称
     * @param supplier     供应商
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     * @param args         args
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(long waitTime, long leaseTime, TimeUnit unit, String lockName, Supplier<T> supplier, HttpStatusEnum httpStatusEnum, String errorMessage, Object... args) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(waitTime, leaseTime, unit)) {
                return supplier.get();
            }
            throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
        } catch (Exception e) {
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param unit         时间单位
     * @param lockName     锁名称
     * @param supplier     供应商
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     * @param time         时间
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(long time, TimeUnit unit, String lockName, Supplier<T> supplier, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(time, unit)) {
                return supplier.get();
            }
            throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
        } catch (Exception e) {
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }


    /**
     * redisson分布式锁
     *
     * @param lockName  锁名称
     * @param supplier  供应商
     * @param httpStatusEnum 错误代码
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, HttpStatusEnum httpStatusEnum) {
        return redissonDistributedLocks(lockName, supplier, httpStatusEnum, httpStatusEnum.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName  锁名称
     * @param supplier  供应商
     * @param httpStatusEnum 错误代码
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, Runnable logMessage, HttpStatusEnum httpStatusEnum) {
        return redissonDistributedLocks(lockName, supplier, logMessage, httpStatusEnum, httpStatusEnum.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param supplier     供应商
     * @param errorMessage 错误消息
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, String errorMessage) {
        return redissonDistributedLocks(lockName, supplier, HttpStatusEnum.FAIL, errorMessage);
    }

    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param supplier 供应商
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier) {
        return redissonDistributedLocks(lockName, supplier, HttpStatusEnum.FAIL);
    }


    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param supplier 供应商
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, String errorLogTitle, Supplier<T> supplier) {
        return redissonDistributedLocks(lockName, supplier, errorLogTitle, HttpStatusEnum.FAIL, HttpStatusEnum.FAIL.getMessage());
    }


    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param supplier 供应商
     * @return {@link T}
     */
    public <T> T redissonDistributedLocks(String lockName, Supplier<T> supplier, Runnable logMessage) {
        return redissonDistributedLocks(lockName, supplier, logMessage, HttpStatusEnum.FAIL);
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                runnable.run();
            } else {
                throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
            }
        } catch (Exception e) {
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("lockName:{},unLockId:{} ", lockName, Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, String errorLogTitle, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                runnable.run();
            } else {
                throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
            }
        } catch (Exception e) {
            log.error(errorLogTitle, e.getMessage());
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("lockName:{},unLockId:{} ", lockName, Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, Runnable logMessage, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MILLISECONDS)) {
                runnable.run();
            } else {
                throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
            }
        } catch (Exception e) {
            logMessage.run();
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("lockName:{},unLockId:{} ", lockName, Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁
     *
     * @param lockName  锁名称
     * @param runnable  可运行
     * @param httpStatusEnum 错误代码
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, HttpStatusEnum httpStatusEnum) {
        redissonDistributedLocks(lockName, runnable, httpStatusEnum, httpStatusEnum.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, String errorMessage) {
        redissonDistributedLocks(lockName, runnable, HttpStatusEnum.FAIL, errorMessage);
    }

    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param runnable 可运行
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable) {
        redissonDistributedLocks(lockName, runnable, HttpStatusEnum.FAIL, HttpStatusEnum.FAIL.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param runnable 可运行
     */
    public void redissonDistributedLocks(String lockName, Runnable runnable, Runnable logMessage) {
        redissonDistributedLocks(lockName, runnable, logMessage, HttpStatusEnum.FAIL, HttpStatusEnum.FAIL.getMessage());
    }

    /**
     * redisson分布式锁
     *
     * @param lockName 锁名称
     * @param runnable 可运行
     */
    public void redissonDistributedLocks(String lockName, String errorLogTitle, Runnable runnable) {
        redissonDistributedLocks(lockName, runnable, errorLogTitle, HttpStatusEnum.FAIL, HttpStatusEnum.FAIL.getMessage());
    }

    /**
     * redisson分布式锁 可自定义 waitTime 、leaseTime、TimeUnit
     *
     * @param waitTime     等待时间
     * @param leaseTime    租赁时间
     * @param unit         时间单位
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(long waitTime, long leaseTime, TimeUnit unit, String lockName, Runnable runnable, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(waitTime, leaseTime, unit)) {
                runnable.run();
            } else {
                throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
            }
        } catch (Exception e) {
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }

    /**
     * redisson分布式锁 可自定义 time 、unit
     *
     * @param time         时间
     * @param unit         时间单位
     * @param lockName     锁名称
     * @param runnable     可运行
     * @param httpStatusEnum    错误代码
     * @param errorMessage 错误消息
     */
    public void redissonDistributedLocks(long time, TimeUnit unit, String lockName, Runnable runnable, HttpStatusEnum httpStatusEnum, String errorMessage) {
        RLock rLock = redissonClient.getLock(lockName);
        try {
            if (rLock.tryLock(time, unit)) {
                runnable.run();
            } else {
                throw new HorApiException(httpStatusEnum.getCode(), errorMessage);
            }
        } catch (Exception e) {
            throw new HorApiException(httpStatusEnum);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                log.info("unLock: " + Thread.currentThread().getId());
                rLock.unlock();
            }
        }
    }
}