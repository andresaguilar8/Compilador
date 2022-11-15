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
        a.init(b).m1().m1().m1();
        a.init(6).m1().m1().m1();
    }

    A init(int i){
        at1 = i;
        return this;
    }

    A m1(){
        System.printIln(at1);
        return this;
    }
}