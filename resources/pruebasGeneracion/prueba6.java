//111
//222
//333
//444
//333
//222
//111

class A{
    static void main(){
        var a = 111;
        {
            var b = 222;
            {
                var c = 333;
                A.print(a);
                A.print(b);
                A.print(c);
                A.print(a + c);
            }
            A.print(a + b);
        }
        var b = 111;
        A.print(a + b);
        A.print(a);
    }

    static void print(int i){
        var v1 = 5;
        var v2 = 7;
        System.printIln(i);
    }
}