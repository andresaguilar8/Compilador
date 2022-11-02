class A {
    public B a1;
    public int a2;
    void m1(B p1)
    {
        var v1 = new B();
        (m2().a3).a2 = 4;
    }
    B m2(){
        return a1;
    }
}
class B{
    public A a3;
    A m3(){
        return a3;
    }
}