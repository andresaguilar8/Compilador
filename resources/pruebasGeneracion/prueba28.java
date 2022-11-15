//size = 13
//1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13

class A{
    static IList getAsIList(List l){
        return l;
    }
    static IGet1 getAsIGet1(IList l){
        return l;
    }
    static IGet2 getAsIGet2(IList l){
        return l;
    }
    static IAdd getAsIAdd(IList l){
        return l;
    }
    static void main(){
        var list = new List();
        var iList = getAsIList(list);
        var iGet1 = getAsIGet1(iList);
        var iGet2 = getAsIGet2(iList);
        var iAdd = getAsIAdd(iList);
        var i = 4;
        while(i < 15){
            iAdd.addLast(i);
            i = i + 3;
        }
        var node = list.getNode(7);
        iAdd.addBefore(node, 5);
        iAdd.addBefore(node, 6);
        node = list.getNode(10);
        node = iAdd.addAfter(node, 11);
        iAdd.addAfter(node, 12);
        i = 3;
        while(i > 0){
            iAdd.addFirst(i);
            i -= 1;
        }
        iAdd.addAfter(iGet2.getNext(iGet2.getNext(iGet2.getNext(iGet2.getNext(iGet2.getNext(iGet2.getNext(iGet1.getFirst())))))), 8);
        iAdd.addBefore(iGet2.getPrevious(iGet2.getPrevious(iGet2.getPrevious(iGet1.getLast()))), 9);
        list.print();
    }
}

interface IAdd1{
    Node addFirst(int element);
    Node addLast(int element);
}

interface IAdd extends IAdd1{
    Node addAfter(Node node, int element);
    Node addBefore(Node node, int element);
}

interface IGet1{
    Node getFirst();
    Node getLast();
}

interface IGet2{
    Node getNext(Node node);
    Node getPrevious(Node node);
}

interface IList extends IAdd, IGet1, IGet2{}

class List implements IList{
    private int size;
    private Node head;

    void print(){
        var node = head;
        System.printS("size = ");
        System.printIln(size);
        var index = 0;
        while(index < size){
            System.printI(node.element);
            if(index < (size - 1)){
                System.printS(", ");
                node = node.next;
            }
            index = index + 1;
        }
        System.println();
    }

    Node addFirst(int element){
        var node = new Node();
        node.element = element;
        node.next = head;
        head = node;
        size += 1;
        return node;
    }

    Node addLast(int element){
        var node = new Node();
        node.element = element;
        if(head == null)
            head = node;
        else {
            var lastNode = head;
            while (lastNode.next != null)
                lastNode = lastNode.next;
            lastNode.next = node;
        }
        size += 1;
        return node;
    }

    Node addAfter(Node node, int element){
        var newNode = new Node();
        newNode.element = element;
        newNode.next = node.next;
        node.next = newNode;
        size += 1;
        return newNode;
    }

    Node addBefore(Node node, int element){
        var newNode = new Node();
        newNode.element = element;
        var nodeAux = head;
        while(nodeAux != null)
            if(nodeAux.next == node){
                nodeAux.next = newNode;
                newNode.next = node;
                size += 1;
                return newNode;
            }else
                nodeAux = nodeAux.next;
        return nodeAux;
    }

    Node getFirst(){
        return head;
    }

    Node getLast(){
        if(head == null)
            return null;
        else{
            var lastNode = head;
            while(lastNode.next != null)
                lastNode = lastNode.next;
            return lastNode;
        }
    }

    Node getNext(Node node){
        return node.next;
    }

    Node getPrevious(Node node){
        var previousNode = head;
        while(previousNode != null)
            if(previousNode.next == node)
                return previousNode;
            else
                previousNode = previousNode.next;
        return previousNode;
    }

    Node getNode(int element){
        var node = head;
        while(node != null)
            if (node.element == element)
                return node;
            else
                node = node.next;
        return node;
    }

    int getSize(){
        return size;
    }

}

class ListDummy implements IList{
    Node addFirst(int element){return null;}
    Node addLast(int element){return null;}
    Node addBefore(Node node, int element){return null;}
    Node addAfter(Node node, int element){return null;}
    Node getFirst(){return null;}
    Node getLast(){return null;}
    Node getNext(Node node){return null;}
    Node getPrevious(Node node){return null;}
    void m1(){}
    void m2(){}
    void m4(){}
    void m5(){}
    void m6(){}
}

class Node{
    public int element;
    public Node next;
}