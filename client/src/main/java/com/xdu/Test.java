//package com.xdu;
//public class Test {
//    public static void main(String[] args) {
//        //通过无参构造创建Zi对象并调用baby()
//        Zi xx = new Zi();
//        xx.baby();//结果为null
//    }
//}
//
//class Fu {
//    Fu(String name){
//        System.out.println(name);
//    }
//    //成员变量
//    String xxx = "hello";//默认值为null
//    public void baby() {
//        System.out.println("陆陆");
//    }
//}
//
//class Zi extends Fu {
//    Zi(String name) {
//        super(name);
//    }
//
//    //这里可以注释掉的，底层会隐式完成这段代码
//   /* public Zi() {
//        super();//在子类的构造方法里调用父类的构造方法来完成父类初始化
//    }*/
//
//    @Override//重写了父类的baby()
//    public void baby() {
//
//        System.out.println();//结果会为继承过来的xxx变量的默认值
//    }
//}
