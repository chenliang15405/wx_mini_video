package com.wx.mini.controller;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author alan.chen
 * @date 2020/8/6 6:36 PM
 */
public class Main {

    public static void main(String[] args) {

        HashMap<Person, Integer> map = new HashMap<>();
        Person gay = new Person("gay伦", 18);
        Person yase = new Person("亚瑟", 10);
        Person timo = new Person("提莫", 11);

        // 保存
        map.put(gay, 1);
        map.put(yase, 2);
        map.put(timo, 3);

        // 第一次获取
        Integer x = map.get(gay);
        System.out.println(x);

        // 修改对象的name属性
        gay.setName("盖伦");

        // 第二次获取
        Integer y = map.get(gay);
        System.out.println(y);




//        HashMap<String, Integer> map1 = new HashMap<>();
//        map1.put(new String("123"), 1);
//
//        System.out.println(map1.get(new String("123")));


    }


    static class Person {
         String name;
        final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Person person = (Person) o;
            return age == person.age;

        }

        @Override
        public int hashCode() {
            return Objects.hash(age);
        }
    }

}
