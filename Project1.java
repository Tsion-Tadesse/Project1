import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
public class Project1{
    private static int getPoissonRandom(double mean) {
        Random x = new Random();
        double L = Math.exp(-mean);
        int i = 0;
        double p = 1.0;
        do {
            p = p * x.nextDouble();
            i++;
        } while (p > L);
        return i - 1;
    }

    public class listNode {
        int data;
        listNode next;

        public listNode(int data) {
            this.data = data;
            this.next = null;
        }
    }
    private final listNode head;

    public Project1() {
        this.head = null;
    }
    
    public boolean isEmpty() {
        return (this.head == null);
    }
    
    public int getFirst() {
        return head.data;
    }

    public static class LinkedQueue extends Project1 {
        int size;
        int totalTime;
        private class Node {
            int items;
            int waitTime;
            int dead;
            Node next;
            public Node(int items) {
                this.items = items;
                waitTime = 0;
                dead = 0;
                size++;
                this.next = null;
            }
        }
        private Node head;

        public void enqueue(int items) {
            if (head == null) {
                head = new Node(items);
            } else {
                Node tmp = head;
                while (tmp.next != null) {
                    tmp = tmp.next;
                }
                tmp.next = new Node(items);
            }
            this.size++;
        }

        public void dequeue() {
            if (this.head == null) {
                System.out.println("No customers in line");
            } else {
                this.head = head.next;
            }
            this.size--;
        }

        public void setWaitTime() {
            Node temp = head;
            while (temp != null) {
                temp.waitTime++;
                temp = temp.next;
            }
        }

        public void getWaitTime() {
            Node temp = head;
            if(temp != null){
                while (temp != null) {
                    totalTime = temp.waitTime + totalTime;
                    System.out.println("Total wait time: " + totalTime);
                    temp = temp.next;
                }
            }else{
                System.out.println("Tick");
            }
        }

        public void setTimeout() {
            Node temp = head;
            while (temp != null) {
                if (temp.waitTime >= 5) {
                    temp.dead = 1;
                    this.size--;
                } else if (temp.waitTime <= 5) {
                    temp.waitTime++;
                }
                temp = temp.next;
            }
        }
    }

    public static void oneServer(LinkedQueue line, LinkedQueue cashier) {
        int cost = 0;
        int profit = 0;
        int timedOut = 0;
        int inLine = 0;
        double mean = 0.2;
        int [] customersServiced = new int[50];
        int [] customerWaitTime = new int[50];
        int [] customersInLine = new int[50];
        int [] profitData = new int[50];
        int [] costData = new int[50];
        for (int i = 0; i < 50; i++) {
            cost = cost - 3;

            for (int j = 0; j < getPoissonRandom(mean); j++) {
                System.out.println("Customers Arriving");
                int items = (int) (Math.random() * 6 + 2);
                line.enqueue(items);

            }
            if (line.head != null && line.head.dead == 1) {
                timedOut++;
                line.dequeue();
                cost = cost - 10;
            }
            if (cashier.head == null && line.head != null && line.head.dead == 0) {
                System.out.println("Line Moving");
                cashier.head = line.head;
                line.head = line.head.next;
            }
            if (cashier.head != null && cashier.head.items > 0) {
                cashier.head.items--;
            } else if (cashier.head != null && cashier.head.items <= 0) {
                System.out.println("Available");
                profit++;
                cashier.head = null;
            }
            line.setWaitTime();
            line.setTimeout();
            System.out.println("# Customers in queue: " + line.size);
            customersInLine[i] = line.size; 
            line.getWaitTime();
            customerWaitTime[i] = line.totalTime;
            System.out.println("Customers serviced: " + profit);
            customersServiced[i] = profit;
            profitData[i] = profit;
            costData[i] = cost;
        }
        while (line.head != null) {
            line.head = line.head.next;
            inLine++;
        }
    }

    public static void twoServer(LinkedQueue line, LinkedQueue cashier, LinkedQueue cashier2) {
        int cost = 0;
        int profit = 0;
        int timedOut = 0;
        int inLine = 0;
        double mean = 0.2;
        int[] customersServiced = new int[50];
        int[] customerWaitTime = new int[50];
        int[] customersInLine = new int[50];
        int[] profitData = new int[50];
        int[] costData = new int[50];
        for (int i = 0; i < 50; i++) {
            cost = cost - 6;

            for (int j = 0; j < getPoissonRandom(mean); j++) {
                System.out.println("Customers Arriving");
                int items = (int) (Math.random() * 6 + 2);
                line.enqueue(items);

            }
            if (line.head != null && line.head.dead == 1) {
                timedOut++;
                line.dequeue();
                cost = cost - 10;
            }
            if (cashier.head == null && line.head != null && line.head.dead == 0) {
                System.out.println("Line Moving");
                cashier.head = line.head;
                line.head = line.head.next;
            }
            if (cashier2.head == null && line.head != null && line.head.dead == 0) {
                System.out.println("Line Moving");
                cashier2.head = line.head;
                line.head = line.head.next;
            }
            if (cashier.head != null && cashier.head.items > 0) {
                cashier.head.items--;
            } else if (cashier.head != null && cashier.head.items <= 0) {
                System.out.println("Available");
                profit++;
                cashier.head = null;
            }
            if (cashier2.head != null && cashier2.head.items > 0) {
                cashier2.head.items--;
            } else if (cashier2.head != null && cashier2.head.items <= 0) {
                System.out.println("Available");
                profit++;
                cashier2.head = null;
            }
            line.setWaitTime();
            line.setTimeout();
            System.out.println("# Customers in queue: " + line.size);
            customersInLine[i] = line.size;
            line.getWaitTime();
            customerWaitTime[i] = line.totalTime;
            System.out.println("# Customers with completed service: " + profit);
            customersServiced[i] = profit;
            profitData[i] = profit;
            costData[i] = cost;
        }
        while (line.head != null) {
            line.head = line.head.next;
            inLine++;
        }
    }
    public static void main(String[] args) {
        LinkedQueue line = new LinkedQueue();
        LinkedQueue line2 = new LinkedQueue();
        LinkedQueue cashier = new LinkedQueue();
        LinkedQueue cashier2 = new LinkedQueue();
        oneServer(line, cashier);
        System.out.println("-------------------------Double Server------------------------------------");
        twoServer(line2, cashier, cashier2);
    }
}