//111
//222
//333
//444
//
//111
//222
//333
//444
//333
//
//111
//222
//333
//444
//333
//222
//
//111
//222
//333
//444
//333
//222
//111

class A{
    static void main(){
        A.m1(1);
        System.println();
        A.m1(2);
        System.println();
        A.m1(3);
        System.println();
        A.m1(4);
    }

    static void print(int i){
        var v1 = 5;
        var v2 = 7;
        System.printIln(i);
    }

    static void m1(int i){
        var a = 111;
        {
            var b = 222;
            {
                var c = 333;
                A.print(a);
                A.print(b);
                A.print(c);
                A.print(a + c);
                if(i == 1)
                    return ;
            }
            A.print(a + b);
            if(i == 2)
                return ;
        }
        var b = 111;
        A.print(a + b);
        if(i == 3)
            return ;
        A.print(a);
    }
}