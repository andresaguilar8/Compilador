

interface I {
    void met1();
    String met2();
    A met3();
}

class A {
    private Object atrObject; public String w,u,e;
    static void main(){}
    B metodoA(B x, B y, B z){}
}

class B extends A implements I, J {

    public Object x, y, z;
    private String atrString;

    void met1(){}
    String met2(){}
    A met3(){}
    char metj(int x){}
    void metW(String x, int y){}
}

interface J extends W{
    char metj(int x);
}

interface W {
    void metW(String x, int y);
}