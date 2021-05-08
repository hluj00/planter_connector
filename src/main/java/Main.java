import application.Application;

public class Main
{


    public static void main( String[] args )
    {
        Application app = new Application();
        app.start();
        long lastSec = -1;
        while(app.isRunning()){
            long sec = System.currentTimeMillis() / 1000;
            if(!app.isMqttConnected()){
                app.start();
            }
            if (sec >= lastSec+15 || sec < lastSec) {
                app.resubscribe();
                lastSec = sec;
                app.saveNewMessages();
                app.executeActions();
            }
        }
    }
}