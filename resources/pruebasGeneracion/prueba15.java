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
        a.init(6);
    }

    void init(int i){
        at1 = i;
        this.m1();
        this.m1();
        this.m1();
    }

    void m1(){
        System.printIln(at1);
    }
}