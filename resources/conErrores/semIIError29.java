///[Error:+|11]

//el tipo de y es int y se usa el operador + entre string y int
class B {

    public int y;

    B m() {
        var x = new A();
        var y = x.f().m().y;
        var z = "a" + y;
    }

    static void main() {}
}

class A  {

    B f() {

    }
}