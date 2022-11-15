//size = 13
//1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13

class A{
    public String at1;
    static void main(){
        var list = new List();
        var i = 4;
        while(i < 15){
            list.addLast(i);
            i = i + 3;
        }
        var node = list.getNode(7);
        list.addBefore(node, 5);
        list.addBefore(node, 6);
        node = list.getNode(10);
        node = list.addAfter(node, 11);
        list.addAfter(node, 12);
        i = 3;
        while(i > 0){
            list.addFirst(i);
            i -= 1;
        }
        list.addAfter(list.getNext(list.getNext(list.getNext(list.getNext(list.getNext(list.getNext(list.getFirst())))))), 8);
        list.addBefore(list.getPrevious(list.getPrevious(list.getPrevious(list.getLast()))), 9);
        list.print();
    }
}

class List{
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

class Node{
    public int element;
    public Node next;
}