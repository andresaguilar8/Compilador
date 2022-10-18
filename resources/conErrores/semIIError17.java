///[Error:+=|8]
// test de operador += entre tipos no primitivos
class A {

    //private int x;

    void met() {
        this.x() += new A();
    }
    B x(){
        return x();
    }
    static void main(){}
}
class B {
    public int x;
}
