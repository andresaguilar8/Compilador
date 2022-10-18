class A {

    int met() {
        var x = this.met2().x;
        return 2*3;
    }

    B met2() {
        return new B();
    }
}

class B {
    public boolean x;
    static void main(){}
}