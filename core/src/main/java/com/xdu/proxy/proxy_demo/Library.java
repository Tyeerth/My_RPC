package com.xdu.proxy.proxy_demo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author tyeerth
 * @date 2023/11/3 - 下午3:53
 * @description
 */
public class Library implements BookService {

    private Map<String, Book> books = new HashMap<>();

    public void addBook(Book book) {
        String name = book.getBookName();
        System.out.println("这里是 " + getClass().getName() + " 的getAllBooks方法");
        if (this.books.containsKey(name)) {
            this.books.put(name, book);
        }
    }


}
