///[Error:==|8]

//comparacion entre dos tipos no compatibles
class B implements Y{
    public A a;
    public B b;
    void m() {
        if (a == b) ;
    }

    static void main() {}
}

class A implements Y {}
