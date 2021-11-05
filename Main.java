import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {

    static GraphicsContext gc;
    static double width = 1700;
    static double height = 1000;
    static double deltaX = 0;
    static double deltaY = 0;
    static double scale = 400;
    static int precision = 200;
    static boolean showSpeed = false;


    static void putDot(double x, double y){
        gc.strokeLine(x+0.5,y+0.5,x+0.5,y+0.5);
    }


    static double module(double a, double b){
        return Math.sqrt(a*a + b*b);
    }



    static double real_sqr(double a, double b){
        return a*a - b*b;
    }



    static double img_sqr(double a, double b){
        return 2*a*b;
    }



    static int isMandelbrot(double a0, double b0){
        double an = a0;
        double bn = b0;
        double old_a = a0;
        double old_b = b0;

        int speed = 0;

        for (int n = 0; n < precision; n++) {
            if(module(an,bn) > 2) return speed;
            an = real_sqr(old_a,old_b);
            bn = img_sqr(old_a,old_b);
            an += a0;
            bn += b0;
            old_a = an;
            old_b = bn;

            speed++;
        }

        return -1;

    }



    public static void main(String[] args) {
        launch();
    }



    @Override
    public void start(Stage myStage) throws Exception {
        myStage.setTitle("MANDELBROT");

        Pane root = new Pane();
        Scene scene = new Scene(root,width,height);
        myStage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        //myStage.getIcons().add(new Image("file:icon.png"));
        myStage.setScene(scene);

        Canvas canvas = new Canvas(width,height);
        gc = canvas.getGraphicsContext2D();

        canvas.setOnMousePressed(event -> {
            deltaX = event.getX() - width/2 + deltaX;
            deltaY = event.getY() - height/2 + deltaY;
            gc.setFill(Color.rgb(0,0,0));
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            canvas.requestFocus();
            drawMaldenbrot();
        });

        canvas.setOnScroll(event -> {
            double old_scale = scale;
            if (event.getDeltaY() > 0) scale *= 1.1;
            else scale *= 0.9;
            deltaX *= scale/old_scale;
            deltaY *= scale/old_scale;

            gc.setFill(Color.rgb(0,0,0));
            gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            drawMaldenbrot();
        });


        TextField precisionField = new TextField();
        precisionField.setLayoutX(25);
        precisionField.setLayoutY(25);
        precisionField.setOnAction((event)->{
            try{
                precision = Integer.parseInt(precisionField.getText());
                drawMaldenbrot();
            } catch (Exception exc){
                precisionField.setText("ERROR");
            }
        });

        Label speedLabel = new Label("HELLO!!!!");
        speedLabel.setTextFill(Color.rgb(255,255,255));
        speedLabel.setVisible(false);

        canvas.setOnKeyPressed((event)->{
            if(event.getText().equals("s"))
                if(!showSpeed){
                    showSpeed = true;
                    speedLabel.setVisible(true);
                }
                else {
                    showSpeed = false;
                    speedLabel.setVisible(false);
                }
        });

        canvas.setOnMouseMoved((event)->{
            if(showSpeed) {
                speedLabel.setLayoutX(event.getX() + 15);
                speedLabel.setLayoutY(event.getY() + 5);
                int speed = isMandelbrot(((event.getX() - width / 2) + deltaX) / scale, ((event.getY() - height / 2) + deltaY) / scale);
                speedLabel.setText(Integer.toString(speed));
            }
        });


        root.getChildren().addAll(canvas,precisionField,speedLabel);
        myStage.show();

        drawMaldenbrot();

    }



    static private void drawMaldenbrot() {

        //new Thread(() -> {

        double speed;
        for (double y = -(int)(height/2); y < (int)(height/2); y++) {
            for (double x = -(int)(width/2); x < (int)(width/2); x++) {

                speed = isMandelbrot((x+deltaX)/scale,(y+deltaY)/scale);
                if (speed == -1)
                    gc.setStroke(Color.rgb(0,0,0));
                else
                    //gc.setStroke(Color.rgb(   (int)(255*Math.pow(speed/precision,(double)(1)/4)),
                    //                          (int)(125*Math.pow(speed/precision,(double)(1)/4)),
                    //                          (int)(150-150*Math.pow(speed/precision,(double)(1)/4))   ));
                    if(speed % 5 == 0)
                        gc.setStroke(Color.rgb(100,150,255));
                    else if(speed % 4 == 0)
                        gc.setStroke(Color.rgb(0,0,255));
                    else if(speed % 3 == 0)
                        gc.setStroke(Color.rgb(0,50,175));
                    else if (speed % 2 == 0)
                        gc.setStroke(Color.rgb(0,50,105));
                    else
                        gc.setStroke(Color.rgb(0,0,100));

                putDot(x + width/2, y + height/2);

            }
        }
        //}).start();


    }


}