package com.example.congestioncontrol.NBPRouter;

import com.example.congestioncontrol.Info.*;
import com.jfoenix.controls.JFXListView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class RouterView extends Thread implements Initializable, Runnable {
    public JFXListView<InfoModel> packetsListView;

    public static ObservableList<InfoModel> infoObservableList = FXCollections.observableArrayList();
    public Label outPackets;
    public Label inPackets;
    Socket soc;
    static String instring="";
    static int length=0;
    static int length1=0;
    static String s="";
    static String s1="";
    String fin="";
    static String dest="";
    static String sou="";
    static int inp=0,outp=0;
    int readcnt=0;
    boolean header=false;
    static String text;
    static String text1;
    char chstr[]=new char[512];


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        packetsListView.setFocusTraversable(false);

        packetsListView.setItems(infoObservableList);

        packetsListView.setCellFactory(infoModelListView -> new InfoCellCard<>());


//        for (Node node: packetsListView.lookupAll(".scroll-bar")) {
//            if (node instanceof ScrollBar) {
//                final ScrollBar bar = (ScrollBar) node;
//                bar.valueProperty().addListener(new ChangeListener<Number>() {
//                    @Override
//                    public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
//                        System.out.println(bar.getOrientation() + " " + newValue);
//                    }
//                });
//            }
//        }
        start();
    }

    public void dis_ing_data()
    {
        try
        {
            System.out.println("Waiting...");
            ServerSocket ss=new ServerSocket(7799);
            while(true)
            {
                soc=ss.accept();
                System.out.println("connected...");
                display();
            }
        }
        catch(Exception e)
        {
            System.out.println("err1: ");
            e.printStackTrace();
        }
    }

    public void display()
    {
        try
        {
            ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
            instring=(String) ois.readObject();
            length=instring.length();
            int fin=instring.indexOf('|');
            if(fin>0)
            {
                int last=instring.lastIndexOf('|');
                header=true;
                System.out.println("---------------------------------------------------------------\n");
                System.out.println(instring.substring(0,last)+"\n");
                System.out.println("---------------------------------------------------------------"+"\n");
                displayMsg(new MessageCardModel(instring.substring(0,last)));
                Socket soc1=new Socket("localhost",7700);
                ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
                oos.writeObject(instring);

            }
            else
            {
                header=false;
            }
            if(length>0 && (!header))
            {
                Socket soc1=new Socket("localhost",7700);
                ObjectOutputStream oos=new ObjectOutputStream(soc1.getOutputStream());
                oos.writeObject(instring);
                oos.flush();
                outp++;
                outgoing(outp);
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
                inp++;
                incoming(inp);
            }
        }

        catch(Exception e1)
        {
            System.out.println("err: ");
            e1.printStackTrace();
        }
    }

    public void add()
    {

        Platform.runLater(() -> {
            infoObservableList.add(
                    0,
                    new PacketCardModel(sou, s.trim(), dest)
            );
            System.out.println("run1: " + Thread.currentThread().getName());
        });
    }

    public void incoming(int in)
    {
        Platform.runLater(() -> {
            inPackets.setText(in+"");
            System.out.println("run1: " + Thread.currentThread().getName());
        });
    }

    public void outgoing(int out)
    {

        Platform.runLater(() -> {
            outPackets.setText(out+"");
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

    @Override
    public void run() {
        Back b=new Back(this);

        dis_ing_data();
    }
}
