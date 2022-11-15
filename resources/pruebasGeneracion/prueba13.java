//5
//5
//5
//6
//6
//6

class A{
    public int at1;
    static void main(){
        var a = new A();
        var b = 5;
        a.init(b);
        a.m1();
        var c = new A();
        a.m1();
        var d = 6;
        c.init(d);
        a.m1();
        c.m1();
        c.m1();
        c.m1();
    }

    void init(int i){
        at1 = i;
    }

    void m1(){
        System.printIln(at1);
    }
}