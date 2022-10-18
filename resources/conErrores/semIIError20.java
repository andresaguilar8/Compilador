///[Error:a1|13]
//condicion del while de tipo int
class A {

    public int a1;
    static void main() {}
}

class B extends A {

    int metodo(){

        while (true) {
            this.a1 = false + 1;
        }
    }
}