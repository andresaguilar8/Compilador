//[Error:m3|8]

class A {
    public B a1;
    public int v1;

    void m1(B p1) {
        v1 = (a1).m3();
        }
}

class B extends A{
    int m2(){
        return 10;
    }
}
