///[Error:=|8]
// test de ponerle null a un tipo primitivo
class A {

    //private int x;

    void met() {
        this.x().x = null;
    }
    B x(){
        return this.x();
    }
    static void main(){}
}
class B {
    public int x;
}
