package com.example.congestioncontrol.NBPOutRouter;

import com.example.congestioncontrol.Info.*;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class OutRouterView extends Thread implements Initializable, Runnable {

    public OutRouterView() {
//        t3=new Thread(this,"Mon");
//        t3.start();
    }
    public JFXListView<InfoModel> packetsListView;

    public static ObservableList<InfoModel> infoObservableList = FXCollections.observableArrayList();
    public Label monitoringText;

    String Monlab="Monitoring";
    int count=0;
    //    Thread t3;
    Socket soc;

    static String instring=" ";
    static int length=0;
    static int length1=0;
    static String s=" ";
    static String s1=" ";
    static String dest=" ";
    static String sou=" ";
    int intfirst,first;
    boolean header=false;
    long tim;
    long  avg_rate;
    Vector sour=new Vector();
    Vector des=new Vector();
    Vector leng=new Vector();
    Vector virtual_time=new Vector();
    String instring1="";
    static TSW ts;
    String find,sub1;
    int fir,sec;
    static long CT;
    int last,check;
    Tim1 ti=new Tim1();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        packetsListView.setFocusTraversable(false);

        packetsListView.setItems(infoObservableList);

        packetsListView.setCellFactory(infoModelListView -> new InfoCellCard<>());

        start();
    }

    public void displayMsg(MessageCardModel msg) {
        Platform.runLater(() -> {
            infoObservableList.add(
                    0,
                    msg
            );
            System.out.println("run1: " + Thread.currentThread().getName());
        });
    }

    public void run1()
    {
        try
        {
            ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
            instring=(String) ois.readObject();
            length=instring.length();
            header=true;
            CT=ti.calculateTime();
            int fin=instring.indexOf('|');
            if(fin>0)
            {
                int last=instring.lastIndexOf('|');
                fir=instring.indexOf('|');
                sec=instring.indexOf('|',fir+1);
                int thi=instring.indexOf('|',sec+1);
                if(thi<0)
                {
                    Thread.sleep(10000);
                    System.out.println("---------------------------------------------------------------\n");
                    System.out.println(instring+"\n");
                    System.out.println("---------------------------------------------------------------"+"\n");
                    displayMsg(new MessageCardModel(instring));
                    Back b=new Back(instring,fir,sec,this);
                }
                if(thi>0)
                {
                    Thread.sleep(10000);
                    System.out.println("---------------------------------------------------------------\n");
                    System.out.println(instring);
                    System.out.println("\n---------------------------------------------------------------"+"\n");
                    displayMsg(new MessageCardModel(instring));
                    instring1=instring.substring(0,sec)+"|";
                    long LT=Long.parseLong(instring.substring(last+1,instring.length()));
                    tim=(CT-LT);
                    ts=new TSW(tim);
                    avg_rate=length/tim;
                    find=instring.substring(sec,thi+1);
                    if(find.equals("|f|"))
                    {
                        String sub=instring.substring(thi+1,instring.length());
                        do
                        {
                            int beg=sub.indexOf('~');
                            if(beg>0)
                            {
                                intfirst=sub.indexOf('~',beg+1);
                                check=sub.indexOf('|',first+1);
                                if(check<0)
                                {
                                    last=sub.length();
                                }
                                else
                                {
                                    last=check;
                                }
                                sour.add(sub.substring(0,beg));
                                des.add(sub.substring(beg+1,first));												leng.add(sub.substring(first+1,last));
                                instring1+=sub.substring(0,beg)+"~"+sub.substring(beg+1,first)+"~"+sub.substring(first+1,last)+"|";
                                if(check>0)
                                {
                                    sub=sub.substring(check+1,sub.length());
                                }
                                if(!leng.isEmpty())
                                {
                                    long tim1=(Long.parseLong((leng.get(leng.size()-1)).toString()))*tim;
                                    virtual_time.add(tim1+"");									instring1+=virtual_time.get(virtual_time.size()-1)+"|";								}
                            }
                            else
                            {
                                check=-1;
                            }
                        }while(check>0);
                        Back b=new Back(instring1,fir,sec,this);
                    }
                    else
                    {
                    }
                }

            }
            else
            {
                header=false;
            }
            if(length>0 && (!header))
            {
                try
                {
                        ts.TimeSlide(length,CT);
                }
                catch(Exception e){e.printStackTrace();}
                for(int l=0;l<length;l++)
                {
                    if((instring.charAt(l))=='`')
                    {
                        dest=instring.substring(0,l);
                        s1=instring.substring(l+1,length);
                        l=length+1;
                        length1=s1.length();
                    }
                }
                for(int l=0;l<length1;l++)
                {
                    if((s1.charAt(l))=='`')
                    {
                        sou=s1.substring(0,l);
                        if((s1.substring((s1.length()-4),s1.length())).equals("null"))
                        {
                            s=s1.substring(l+1,length1-4);
                        }
                        else
                        {
                            s=s1.substring(l+1,length1);
                        }
                        l=length1+1;
                    }
                }
                add();
                Socket soc1=new Socket(dest,7711);
                ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
                oos.writeObject(instring);
                oos.flush();
            }
        }
        catch(ClassNotFoundException e)
        {	  }
        catch(IOException e1)
        {	  }
        catch(Exception e)
        {	  }
    }

    public void add() {
//        text+="Source :"+sou+"\nMessage :"+s.trim()+"\nDestination :"+dest+"\n";

        Platform.runLater(() -> {
            infoObservableList.add(
                    0,
                    new PacketCardModel(sou, s.trim(), dest)
            );
            System.out.println("run1: " + Thread.currentThread().getName());
        });
    }

    @Override
    public void run() {
        System.out.println("*********************Out Router********************************************");
        try
        {
            ServerSocket socket = new ServerSocket(7700);
            while(true)
            {
                System.out.println("waiting : " + Thread.currentThread().getName());
                soc = socket.accept();
                System.out.println("connected: " + Thread.currentThread().getName());
                run1();
            }
        }
        catch(Exception e)
        {
            System.out.println("UHE");
        }
//        while(true)
//        {
//            Monlab=Monlab+".";
//            monitoringText.setText(Monlab);
//            count++;
//            if(count==11)
//            {
//                Monlab="Monitoring";
//                count=0;
//            }
//            else
//            {
//                try
//                {
//                    t3.sleep(500);
//                }
//                catch(InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        }
    }
}


//package com.example.conjestioncontrol.NBPOutRouter;
//
//import com.example.conjestioncontrol.Info.*;
//import com.jfoenix.controls.JFXListView;
//import javafx.application.Platform;
//import javafx.collections.FXCollections;
//import javafx.collections.ObservableList;
//import javafx.concurrent.Task;
//import javafx.fxml.Initializable;
//import javafx.scene.control.Label;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.ListView;
//import javafx.util.Callback;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.net.URL;
//import java.net.UnknownHostException;
//import java.util.ResourceBundle;
//import java.util.Vector;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//public class OutRouterView implements Initializable {
//
//    public OutRouterView() {
////        t3=new Thread(this,"Mon");
////        t3.start();
//    }
//    public JFXListView<InfoModel> packetsListView;
//
//    public static ObservableList<InfoModel> infoObservableList = FXCollections.observableArrayList();
//    public Label monitoringText;
//
//    String Monlab="Monitoring";
//    int count=0;
//    //    Thread t3;
//    static Socket soc;
//
//    static String instring=" ";
//    static int length=0;
//    static int length1=0;
//    static String s=" ";
//    static String s1=" ";
//    static String dest=" ";
//    static String sou=" ";
//    static int intfirst;
//    static int first;
//    static boolean header=false;
//    static long tim;
//    static long  avg_rate;
//    static Vector sour=new Vector();
//    static Vector des=new Vector();
//    static Vector leng=new Vector();
//    static Vector virtual_time=new Vector();
//    static String instring1="";
//    static TSW ts;
//    static String find;
//    String sub1;
//    static int fir;
//    static int sec;
//    static long CT;
//    int last;
//    static int check;
//    static Tim1 ti=new Tim1();
//
//    static {
//        Task task = new Task() {
//            @Override
//            protected Object call() throws Exception {
//                run2();
//                return null;
//            }
//        };
//        Thread t = new Thread(task);
////        t.setDaemon(true);
//        t.start();
//    }
//
//
//    @Override
//    public void initialize(URL url, ResourceBundle resourceBundle) {
//
////        packetsListView.setMouseTransparent(true);
//        packetsListView.setFocusTraversable(false);
//
//        packetsListView.setItems(infoObservableList);
//
//        packetsListView.setCellFactory(infoModelListView -> new InfoCellCard<>());
//        System.out.println("out router: " + Thread.currentThread().getName());
////        Runnable runnable = () -> {
////            run2();
////        };
////        Thread t = new Thread(runnable);
////        t.start();
//
//
////        infoObservableList.add(
////                new PacketCardModel("Jeffrey's PC", "Hey there", "127.0.0.1")
////        );
////
////        infoObservableList.add(
////                new MessageCardModel("Jeffrey's PC", "", "", "", false)
////        );
//
//
//    }
//
//    public static void displayMsg(MessageCardModel msg) {
//        infoObservableList.add(
//                0,
//                msg
//        );
//    }
//
//    public static void run1()
//    {
//        try
//        {
//            ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
//            instring=(String) ois.readObject();
//            length=instring.length();
//            header=true;
//            CT=ti.calculateTime();
//            int fin=instring.indexOf('|');
//            if(fin>0)
//            {
//                int last=instring.lastIndexOf('|');
//                fir=instring.indexOf('|');
//                sec=instring.indexOf('|',fir+1);
//                int thi=instring.indexOf('|',sec+1);
//                if(thi<0)
//                {
//                    Thread.sleep(10000);
//                    System.out.println("connected 1: " + Thread.currentThread().getName());
//                    System.out.println("---------------------------------------------------------------\n");
//                    System.out.println(instring+"\n");
//                    System.out.println("---------------------------------------------------------------"+"\n");
//                    displayMsg(new MessageCardModel(instring));
//                    Back b=new Back(instring,fir,sec);
//                }
//                if(thi>0)
//                {
//                    Thread.sleep(10000);
//                    System.out.println("connected 2: " + Thread.currentThread().getName());
//                    System.out.println("---------------------------------------------------------------\n");
//                    System.out.println(instring);
//                    System.out.println("\n---------------------------------------------------------------"+"\n");
//                    displayMsg(new MessageCardModel(instring));
//                    instring1=instring.substring(0,sec)+"|";
//                    long LT=Long.parseLong(instring.substring(last+1,instring.length()));
//                    tim=(CT-LT);
//                    ts=new TSW(tim);
//                    avg_rate=length/tim;
//                    find=instring.substring(sec,thi+1);
//                    if(find.equals("|f|"))
//                    {
//                        String sub=instring.substring(thi+1,instring.length());
//                        do
//                        {
//                            int beg=sub.indexOf('~');
//                            if(beg>0)
//                            {
//                                intfirst=sub.indexOf('~',beg+1);
//                                check=sub.indexOf('|',first+1);
//                                if(check<0)
//                                {
//                                    last=sub.length();
//                                }
//                                else
//                                {
//                                    last=check;
//                                }
//                                sour.add(sub.substring(0,beg));
//                                des.add(sub.substring(beg+1,first));												leng.add(sub.substring(first+1,last));
//                                instring1+=sub.substring(0,beg)+"~"+sub.substring(beg+1,first)+"~"+sub.substring(first+1,last)+"|";
//                                if(check>0)
//                                {
//                                    sub=sub.substring(check+1,sub.length());
//                                }
//                                if(!leng.isEmpty())
//                                {
//                                    long tim1=(Long.parseLong((leng.get(leng.size()-1)).toString()))*tim;
//                                    virtual_time.add(tim1+"");									instring1+=virtual_time.get(virtual_time.size()-1)+"|";								}
//                            }
//                            else
//                            {
//                                check=-1;
//                            }
//                        }while(check>0);
//                        Back b=new Back(instring1,fir,sec);
//                    }
//                    else
//                    {
//                    }
//                }
//
//            }
//            else
//            {
//                header=false;
//            }
//            if(length>0 && (!header))
//            {
//                try
//                {
//                    ts.TimeSlide(length,CT);
//                }
//                catch(Exception e){e.printStackTrace();}
//                for(int l=0;l<length;l++)
//                {
//                    if((instring.charAt(l))=='`')
//                    {
//                        dest=instring.substring(0,l);
//                        s1=instring.substring(l+1,length);
//                        l=length+1;
//                        length1=s1.length();
//                    }
//                }
//                for(int l=0;l<length1;l++)
//                {
//                    if((s1.charAt(l))=='`')
//                    {
//                        sou=s1.substring(0,l);
//                        if((s1.substring((s1.length()-4),s1.length())).equals("null"))
//                        {
//                            s=s1.substring(l+1,length1-4);
//                        }
//                        else
//                        {
//                            s=s1.substring(l+1,length1);
//                        }
//                        l=length1+1;
//                    }
//                }
//                add();
//                Socket soc1=new Socket(dest,7711);
//                ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
//                oos.writeObject(instring);
//                oos.flush();
//            }
//        }
//        catch(ClassNotFoundException e)
//        {	  }
//        catch(IOException e1)
//        {	  }
//        catch(Exception e)
//        {	  }
//    }
//
//    public static void add()
//    {
////        text+="Source :"+sou+"\nMessage :"+s.trim()+"\nDestination :"+dest+"\n";
//        infoObservableList.add(
//                0,
//                new PacketCardModel(sou, s.trim(), dest)
//        );
//    }
//
//    //    @Override
//    public static void run2() {
//        System.out.println("*********************Out Router********************************************");
//        try
//        {
//            ServerSocket socket = new ServerSocket(7700);
//            while(true)
//            {
//                System.out.println("waiting : " + Thread.currentThread().getName());
//                soc = socket.accept();
//                System.out.println("connected: " + Thread.currentThread().getName());
//                Platform.runLater(() -> {
//                    run1();
//                });
//                Thread.sleep(100);
//            }
//        }
//        catch(Exception e)
//        {
//            System.out.println("UHE");
//        }
////        while(true)
////        {
////            Monlab=Monlab+".";
////            monitoringText.setText(Monlab);
////            count++;
////            if(count==11)
////            {
////                Monlab="Monitoring";
////                count=0;
////            }
////            else
////            {
////                try
////                {
////                    t3.sleep(500);
////                }
////                catch(InterruptedException e)
////                {
////                    e.printStackTrace();
////                }
////            }
////        }
//    }
//}
