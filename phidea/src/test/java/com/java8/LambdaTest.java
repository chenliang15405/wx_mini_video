package com.java8;

import org.junit.Test;

import java.util.Comparator;
import java.util.function.Consumer;

/**
 *
 * Lambda表达式的基础语法： java8中引入了新的操作符："->"
 *
 * 左侧是lambda的参数列表
 * 右侧是lambda的lambada体
 *
 * 语法格式一：无参数 无返回值
 *      () -> System.out.println("xxxx")
 *
 * 语法格式二：有一个参数，无返回值
 *      (e) -> System.out.println(x)
 *      e -> System.out.println(x)
 *
 * 语法格式三：有两个以上的参数，有返回值，并且lambda体中有多条语句
 *      Comparator<Interger> com = (x, y) -> {
 *          return Integer.compare(x, y);
 *      };
 *
 * 语法格式四：如果lambda体中只有一条语句，打括号和return都可以省略
 *
 * 语法格式五：Lambda表达式的参数列表中的参数类型可以省略，JVM可以通过上下文推断出，数据类型 即"类型推断"
 *       (x, y) -> Integer.compare(x, y)
 *       (Integer x, Integer y) -> Integer.compare(x, y)
 *
 *
 */
public class LambdaTest {


    @Test
    public void test1() {
        // 原始写法
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello word");
            }
        };
        runnable.run();

        // lambda表达式
        System.out.println("-------------------");

        Runnable runnable1 = () -> System.out.println("hello lambda");
        runnable1.run();
    }


    @Test
    public void test2() {
        Consumer<String> con = (x) -> System.out.println(x);
        con.accept("123");
    }

    @Test
    public void test3() {
        Comparator<Integer> comparator = (x, y) -> {
            return Integer.compare(x, y);
        };

    }



}
