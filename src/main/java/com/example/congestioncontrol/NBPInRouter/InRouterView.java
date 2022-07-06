package com.example.congestioncontrol.NBPInRouter;

import com.example.congestioncontrol.Info.*;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

public class InRouterView extends Thread implements Initializable, Runnable {
    public JFXListView<InfoModel> packetsListView;

    public static ObservableList<InfoModel> infoObservableList = FXCollections.observableArrayList();
    public Label outPacket;
    public Label inPacket;
    public JFXButton showRateControlMonitor;

    BufferedReader in1;
    boolean sta=true;
    OutputStream out;
    static String dest="";
    static String sou="";
    static String s="";
    int readcnt=0;
    char chstr[]=new char[512];
    String instring="";
    static Vector msg=new Vector();
    static int I=0;
    static int length=0;
    static int length1=0;
    static String s1="";
    static int inp=0;
    static int outp =0;
    static Vector len=new Vector();
    static Vector des=new Vector();
    static Vector sour=new Vector();
    int tot;
    String egg="";
    ArrayList<Long> l = new ArrayList<Long>();

    Send sen;
    private Thread t;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        packetsListView.setFocusTraversable(false);


        packetsListView.setItems(infoObservableList);

        packetsListView.setCellFactory(infoModelListView -> new InfoCellCard<>());
        sen=new Send(this);
        showRateControlMonitor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println("dcdcd");
                Platform.runLater(() -> {
                    Application application = new RateControlMonitor();

                    Stage prStage = new Stage();
                    try {
                        application.start(prStage);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        });

        start();
    }

    public void dis_ing_data()
    {
        try
        {
            ServerSocket ss=new ServerSocket(7788);
            while(true)
            {
                System.out.println("waiting");
                Socket soc=ss.accept();
                System.out.println("Connected");
                sta=true;
                while(sta)
                {
                    in1 = new BufferedReader(new InputStreamReader(soc.getInputStream()));
                    out=soc.getOutputStream();
                    display();
                }
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public void add()
    {
//        text+="Source :"+sou+"\nMessage :"+s.trim()+"\nDestination :"+dest+"\n";

        Platform.runLater(() -> {
            infoObservableList.add(
                    0,
                    new PacketCardModel(sou, s.trim(), dest)
            );
            System.out.println("run1: " + Thread.currentThread().getName());
        });
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

    public void incoming(int in)
    {
        System.out.println("in: "+in+"");

        Platform.runLater(() -> {
            inPacket.setText(in+"");
            System.out.println("run1: " + Thread.currentThread().getName());
        });
    }

    public void outgoing(int out)
    {
        System.out.println("out: "+out+"");

        Platform.runLater(() -> {
            outPacket.setText(out+"");
            System.out.println("run1: " + Thread.currentThread().getName());
        });
    }

    public void display()
    {
        try
        {
            while(true)
            {
                readcnt=in1.read(chstr);
                if(readcnt <=0)
                {
                    continue;
                }
                else
                {
                    break;
                }
            }
            instring =new String(chstr, 0, readcnt);
            msg.add(instring);
            I++;
            if(!instring.endsWith("null"))
            {
                length=instring.length();
                length1=0;
                len.add(length+"");

                for(int l=0;l<length;l++)
                {
                    if((instring.charAt(l))=='`')
                    {
                        dest=instring.substring(0,l);
                        des.add(dest);
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
                        sour.add(sou);
                        s=s1.substring(l+1,length1);
                        l=length1+1;
                    }
                }
                add();
                inp++;
                incoming(inp);
            }
            else
            {
                sta=false;
                length=instring.length()-4;
                length1=0;
                len.add(length+"");
                for(int l=0;l<length;l++)
                {
                    if((instring.charAt(l))=='`')
                    {
                        dest=instring.substring(0,l);
                        des.add(dest);
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
                        sour.add(sou);
                        s=s1.substring(l+1,length1);
                        l=length1+1;
                    }

                }
                add();
                inp++;
                incoming(inp);
            }
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try
        {
            //Fin f=new Fin();
            A a = new A(this);
            Back b = new Back(this);
            RateControl rc = new RateControl(this);
            dis_ing_data();
        }
        catch(Exception e)
        {

        }
    }
}

class A extends Thread
{
    InRouterView i;
    A(InRouterView obj1)
    {
        i=obj1;
        java.util.Timer t=new java.util.Timer();
        if((i.msg.size())>0)
        {
            t.schedule(i.sen,10000);
        }
        else
        {
            t.schedule(i.sen,10000,30000);
        }
        try
        {
            Thread.sleep(1000);
        }
        catch(InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

class B extends Thread
{
    InRouterView i1;
    B(InRouterView obj1)
    {
        i1=obj1;
        try
        {
            Leaky l=new Leaky(i1);
            java.util.Timer t1=new java.util.Timer();
            t1.schedule(l,10000,1000);
        }
        catch(Exception e){}
    }
}