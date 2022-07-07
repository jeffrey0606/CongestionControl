package com.example.congestioncontrol.NBPInRouter;

import com.example.congestioncontrol.Toaster;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class RateControl  extends Thread
{
    long currentRTT,currentTime,Ptimestamp,EbaseRTT,deltaRTT,QF,FOutRouterRate,Fphase;
    long RTTElapsed,ElastFeedbackTime,rateQuantum,MSS,EhopCount,FInRouterRate,flow;
    InRouterView Fra1;
    Date d=new Date();
    Tim1 tt=new Tim1();

    long ct;
    String eg;
    long pTimeStamp;
    ServerSocket soc;

    long deliveryTimeDifference = 0;
    long o;
    RateControl()
    {

    }


    RateControl(InRouterView Fra1)
    {
        this.Fra1=Fra1;
        currentRTT=0;
        EbaseRTT=510;
        RTTElapsed=0;
        pTimeStamp=0;
        QF=10;
        FInRouterRate=0;
        FOutRouterRate=0;
        ct=d.getTime();


        //ElastFeedbackTime=0;
        rateQuantum=0;
        Fphase=0;
        EhopCount=1;
        MSS=61;
        ElastFeedbackTime=0;

        flow=0;
        try
        {
            soc=new ServerSocket(3333);
            start();
        }
        catch(Exception e){}

    }

    public void run()
    {
        int count = 0;
        while(true)
        {
            try
            {
                Socket s=soc.accept();
                BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
                String []data = br.readLine().split("-");

                eg = data[0];
                pTimeStamp = Long.parseLong(data[1]);
                MSS = Long.parseLong(data[2]);
                System.out.println("eg: " + eg + " pTimeStamp: " + pTimeStamp);
                currentTime =new Tim1().calculateTime();
                if(count == 0) {
                    FInRouterRate=Long.parseLong(eg);
                    ElastFeedbackTime = currentTime;
                }

                count++;

                FOutRouterRate=Long.parseLong(eg);
            }

            catch(Exception e){}
            long cTS = pTimeStamp;//d.getTime();
            long tSPacketSend = Fra1.l.remove(0);

            controlRate(cTS, tSPacketSend);

            double latency = (double) cTS - tSPacketSend;

            double transferRate = ((double)(MSS * EhopCount) / latency) ;

            if(count > 1) {
                double difference = (double)((cTS - deliveryTimeDifference) / 1000);
                System.out.println("difference: " + difference);
                RateControlMonitor.add(new RateSeriesDataModel(count, transferRate), false);
                deliveryTimeDifference = cTS;
            } else {
                deliveryTimeDifference = cTS;
            }

            System.out.println("transferRate: " + transferRate + "Bytes/ms" + " latency: " + latency);
        }
    }

    void controlRate(long cTS, long tSPacketSend) {
        System.out.println("currentTime on outRouter feedback: " + cTS + " Time Packet Send at inRouter: " + tSPacketSend);
        System.out.println("currentTime before: " + currentTime);
        System.out.println("MSS: " + MSS);
        currentRTT= currentTime - cTS;

        if(currentRTT<EbaseRTT)
            EbaseRTT=currentRTT;

        deltaRTT=currentRTT-EbaseRTT;

        RTTElapsed= currentRTT <= 0 ? 0 : (( currentTime - ElastFeedbackTime ) / currentRTT);

        System.out.println("currentTime: " + currentTime + " ElastFeedbackTime: " + ElastFeedbackTime + " currentRTT:" + currentRTT);

        ElastFeedbackTime=currentTime;

        rateQuantum= Math.min( currentRTT <= 0 ? 0 : (MSS/currentRTT),FOutRouterRate/QF);

        System.out.println("rateQuantum: " + rateQuantum);
        System.out.println("RTTElapsed: " + RTTElapsed);
        if(Fphase == 0) {
            if(deltaRTT*FInRouterRate < MSS * EhopCount) {
                FInRouterRate = FInRouterRate * (long) (Math.pow(2, RTTElapsed / 1000));
                System.out.println("Slow Start rate: " + FInRouterRate);
                new Toaster().showToast("Entering a SLOW START RATE");
                if(Leaky.t1>=300) Leaky.t1-=300;
            } else {
                Fphase = 1;
                System.out.println("Avoid Congestion Now: " + FInRouterRate);
                new Toaster().showToast("Avoiding Congestion NOW");
                Leaky.t1+=300;
            }
        } else {
            if(deltaRTT * FInRouterRate < MSS * EhopCount) {
                FInRouterRate = FInRouterRate + rateQuantum * RTTElapsed;
                Fphase = 0;
                new Toaster().showToast("Entering Back to SLOW START RATE");
                System.out.println("Back to Slow Start rate: " + FInRouterRate);
                if(Leaky.t1>=300) Leaky.t1-=300;
            } else {
                FInRouterRate = FInRouterRate - rateQuantum;
                new Toaster().showToast("Continue Avoiding Congestion");
                System.out.println("Continue Avoiding Congestion: " + FInRouterRate);
                Leaky.t1+=300;
            }
        }
    }
}
