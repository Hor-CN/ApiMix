package cn.apimix.core.utils;

import cn.apimix.core.constant.StringConstants;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;


/**
 * 异常工具类
 * @Author: Hor
 * @Date: 2024/12/16 12:31
 * @Version: 1.0
 */
@Slf4j
public class ExceptionUtils {

    private ExceptionUtils() {
    }

;
    /**
     * 如果有异常，返回 null
     *
     * @param exSupplier 可能会出现异常的方法执行
     * @param <T>        /
     * @return /
     */
    public static <T> T exToNull(ExSupplier<T> exSupplier) {
        return exToDefault(exSupplier, null);
    }

    /**
     * 如果有异常，执行异常处理
     *
     * @param supplier   可能会出现异常的方法执行
     * @param exConsumer 异常处理
     * @param <T>        /
     * @return /
     */
    public static <T> T exToNull(ExSupplier<T> supplier, Consumer<Exception> exConsumer) {
        return exToDefault(supplier, null, exConsumer);
    }

    /**
     * 如果有异常，返回空字符串
     *
     * @param exSupplier 可能会出现异常的方法执行
     * @return /
     */
    public static String exToBlank(ExSupplier<String> exSupplier) {
        return exToDefault(exSupplier, StringConstants.EMPTY);
    }

    /**
     * 如果有异常，返回默认值
     *
     * @param exSupplier   可能会出现异常的方法执行
     * @param defaultValue 默认值
     * @param <T>          /
     * @return /
     */
    public static <T> T exToDefault(ExSupplier<T> exSupplier, T defaultValue) {
        return exToDefault(exSupplier, defaultValue, null);
    }

    /**
     * 如果有异常，执行异常处理，返回默认值
     *
     * @param exSupplier   可能会出现异常的方法执行
     * @param defaultValue 默认值
     * @param exConsumer   异常处理
     * @param <T>          /
     * @return /
     */
    public static <T> T exToDefault(ExSupplier<T> exSupplier, T defaultValue, Consumer<Exception> exConsumer) {
        try {
            return exSupplier.get();
        } catch (Exception e) {
            if (null != exConsumer) {
                exConsumer.accept(e);
            }
            return defaultValue;
        }
    }

    /**
     * 异常提供者
     *
     * @param <T> /
     */
    public interface ExSupplier<T> {
        /**
         * 获取返回值
         *
         * @return /
         * @throws Exception /
         */
        T get() throws Exception;
    }
}