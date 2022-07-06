package com.example.congestioncontrol.NBPInRouter;

import com.example.congestioncontrol.Info.MessageCardModel;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Back extends Thread {
    InRouterView rf;
    ServerSocket ss;
    int fir,sec,thir,i=0;

    //RateControl rc=new RateControl(this);



    Back(InRouterView obj1)
    {
        rf=obj1;
        start();
    }

    public void run()
    {
        try
        {
            ss=new ServerSocket(7777);
            System.out.println("In Route ServerSocket started at: 7777");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        while(true)
        {
//			if(ss == null) {
//				break;
//			}
            try
            {
//				System.out.println("back accept" + ss);

                Socket soc=ss.accept();
                ObjectInputStream ois=new ObjectInputStream(soc.getInputStream());
                String instring=(String) ois.readObject();
                System.out.println("---------------------------------------------------------------\n");
                System.out.println(instring+"\n");
                System.out.println("---------------------------------------------------------------"+"\n");
                int fir=instring.indexOf('|');
                int sec=instring.indexOf('|',fir+1);
                int thir=instring.indexOf('|',sec+1);
                rf.displayMsg(new MessageCardModel(instring));

                if(thir>0)
                {
                    String inside=instring.substring(sec+1,instring.length());
                    //System.out.println("inside1 :"+inside);
                    int len=0;
                    do
                    {
                        i++;
                        int las=inside.lastIndexOf('|');
                        //System.out.println("inside2 :"+las);
                        if(las>0)
                        {
                            int in=inside.indexOf('|');
                            int nex=inside.indexOf('|',in+1);
                            //System.out.println("inside2 :"+in);
                            //System.out.println("inside2 :"+nex);
                            //System.out.println("inside2 :"+las);
                            //System.out.println(inside.substring(in+1,nex));
                            rf.tot+=Integer.parseInt(inside.substring(in+1,nex));
                            if(i==1){rf.egg=rf.tot+"";}
                            inside=inside.substring(nex+1,las+1);
                            //System.out.println("inside21 :"+inside);
                            len=inside.length();
                            //System.out.println("inside22 :"+inside.length());
                        }
                        else
                        {
                            len=0;
                        }
                    }while(len>0);
                }
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

        }
    }
}
