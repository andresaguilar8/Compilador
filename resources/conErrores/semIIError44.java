///[Error:m3|8]

class A extends C {



    int m2(int y) {
        return this.m3(new A(), new B());
    }

}

class B extends C{

}

class C extends D {
    static void main() {}
    int m3(C a, C b) {

    }
}

class D {

}
