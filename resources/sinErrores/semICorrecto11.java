

class A extends B implements C, D {
    public B x;
    public C y,z;
    D metodo(){}
    A metodoD(){}
    D metodoC(C x, D y){}
}

class B extends Object {
    static void main(){}
    C metodoB(){}
}

interface C {
    D metodoC(C x, D y);
}

interface D {
    A metodoD();
}