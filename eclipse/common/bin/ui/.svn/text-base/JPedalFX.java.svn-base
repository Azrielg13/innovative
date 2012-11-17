///**
// * ===========================================
// * Java Pdf Extraction Decoding Access Library
// * ===========================================
// *
// * Project Info:  http://www.jpedal.org
// * (C) Copyright 1997-2008, IDRsolutions and Contributors.
// *
// * 	This file is part of JPedal
// *
//    This library is free software; you can redistribute it and/or
//    modify it under the terms of the GNU Lesser General Public
//    License as published by the Free Software Foundation; either
//    version 2.1 of the License, or (at your option) any later version.
//
//    This library is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
//    Lesser General Public License for more details.
//
//    You should have received a copy of the GNU Lesser General Public
//    License along with this library; if not, write to the Free Software
//    Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
//
//
// *
// * ---------------
// * JPedalFX.java
// * ---------------
// */
//package ui;
//
//import java.awt.Dimension;
//import java.awt.Toolkit;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//import javafx.animation.*;
//import javafx.application.Application;
//import javafx.application.Platform;
//import javafx.event.ActionEvent;
//import javafx.event.Event;
//import javafx.event.EventHandler;
//import javafx.event.EventType;
//import javafx.geometry.Insets;
//import javafx.geometry.Pos;
//import javafx.scene.Group;
//import javafx.scene.Scene;
//import javafx.scene.control.Button;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.input.MouseEvent;
//import javafx.scene.layout.AnchorPane;
//import javafx.scene.layout.BorderPane;
//import javafx.scene.layout.HBox;
//import javafx.scene.text.Text;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle;
//import javafx.util.Duration;
//import org.jpedal.PdfDecoder;
//
///**
// *
// * @author Sam Howard
// */
//public class JPedalFX extends Application {
//    
//    PdfDecoder pdf;
//    ImageView imageView;
//    Stage stage;
//    Scene scene;
//    Button open,back,next,quit,min;
//    List<Button> buttonGroup;
//    int pageNumber = 1;
//    File lastSuccessfullyLoadedFile = null;
//    
//    //Variables for making buttons disappear
//    long scheduleDisappearTime = System.currentTimeMillis();
//    Thread visibilityThread;
//
//    /**
//     * @param args Filename to open
//     */
//    public static void main(String[] args) {
//        Application.launch(args);
//    }
//    
//    /**
//     * Set up GUI and do opening logic.
//     * @param primaryStage 
//     */
//    @Override
//    public void start(Stage primaryStage) {
//        //Set up stage
//        stage = new Stage(StageStyle.UNDECORATED);
//        stage.setTitle("JPedalFX");
//        
//        final Group root = new Group();
//        scene = new Scene(root, 300, 250);
//        
//        stage.setScene(scene);
//        
//        pdf = new PdfDecoder();
//        
//        
//        
//        AnchorPane anchor = new AnchorPane();
//        root.getChildren().add(anchor);
//        anchor.setStyle("-fx-border-color: black; -fx-border-width: 1; -fx-background-color: black;");
//        
//        imageView = new ImageView();
//        AnchorPane.setLeftAnchor(imageView, 0.0);
//        AnchorPane.setTopAnchor(imageView, 0.0);
//        AnchorPane.setRightAnchor(imageView, 1.0);
//        AnchorPane.setBottomAnchor(imageView, 1.0);
//        
//        
//        
//        //Create buttons
//        open = new Button("Open");
//        open.setOnAction(new EventHandler<ActionEvent>(){
//            public void handle(ActionEvent e) {
//                File f = showFileChooser();
//                if (f == null)
//                    return;
//                boolean fail = openFile(f);
//                if (fail)
//                    showOpenFailedDialog();
//                else
//                    showPage(1);
//            }
//        });
//        open.setPrefSize(72,35);
//        AnchorPane.setLeftAnchor(open, 3.0);
//        AnchorPane.setTopAnchor(open, 3.0);
//        
//        
//        quit = new Button("X");
//        quit.setOnAction(new EventHandler(){
//            public void handle(Event t) {
//                pdf.closePdfFile();
//                
//                //called at end
//                EventHandler onFinish = new EventHandler<ActionEvent>(){
//                    public void handle(ActionEvent e) {
//                        System.exit(0);
//                    }
//                };
//                
//                //called on each render
//                AnimationTimer timer = new AnimationTimer() {
//                    public void handle(long l) {
//                        stage.setWidth(root.getBoundsInParent().getWidth());
//                        stage.setHeight(root.getBoundsInParent().getHeight());
//                        stage.centerOnScreen();
//                    }
//                };
//                
//                Timeline tl = new Timeline();
//                KeyValue kVX = new KeyValue(root.scaleXProperty(), 0.75);
//                KeyValue kVY = new KeyValue(root.scaleYProperty(), 0.75);
//                KeyValue kVTX = new KeyValue(root.translateXProperty(), -(stage.getWidth()/8));
//                KeyValue kVTY = new KeyValue(root.translateYProperty(), -(stage.getHeight()/8));
//                KeyValue kVO = new KeyValue(stage.opacityProperty(), 0);
//                
//                KeyFrame frame = new KeyFrame(Duration.millis(350), onFinish, kVX, kVY, kVTX, kVTY, kVO);
//                tl.getKeyFrames().add(frame);
//                
//                tl.play();
//                timer.start();                
//            }
//        });
//        quit.setPrefSize(35, 35);
//        AnchorPane.setRightAnchor(quit, 4.0);
//        AnchorPane.setTopAnchor(quit, 3.0);
//        
//    
//        min = new Button("_");
//        min.setOnAction(new EventHandler(){
//            public void handle(Event t) {
//                stage.setIconified(true);
//            }
//        });
//        min.setPrefSize(35, 35);
//        AnchorPane.setRightAnchor(min, 41.0);
//        AnchorPane.setTopAnchor(min, 3.0);
//        
//                      
//        
//        back = new Button("<");
//        back.setOnAction(new EventHandler(){
//            public void handle(Event t) {
//                showPage(pageNumber - 1);
//            }
//        });
//        back.setPrefSize(100, 100);
//        AnchorPane.setLeftAnchor(back, 3.0);
//        AnchorPane.setBottomAnchor(back, 4.0);
//        
//        
//        next = new Button(">");
//        next.setOnAction(new EventHandler(){
//            public void handle(Event t) {
//                showPage(pageNumber + 1);
//            }
//        });
//        next.setPrefSize(100, 100);
//        AnchorPane.setRightAnchor(next, 4.0);
//        AnchorPane.setBottomAnchor(next, 4.0);
//        
//        //Add buttons to list
//        buttonGroup = new ArrayList<Button>();
//        buttonGroup.add(next);
//        buttonGroup.add(back);
//        buttonGroup.add(min);
//        buttonGroup.add(quit);
//        buttonGroup.add(open);
//
//        //Set buttons to transparent
//        setButtonOpacities(0.0);
//        
//        //Set up mouse listener & add to components
//        MouseListener listener = new MouseListener();
//        imageView.addEventHandler(MouseEvent.ANY, listener);
//        back.addEventHandler(MouseEvent.ANY, listener);
//        next.addEventHandler(MouseEvent.ANY, listener);
//        quit.addEventHandler(MouseEvent.ANY, listener);
//        min.addEventHandler(MouseEvent.ANY, listener);
//        open.addEventHandler(MouseEvent.ANY, listener);
//        
//        //Add components
//        anchor.getChildren().addAll(imageView, next, back, min, quit, open);
//        
//        //Check parameters for file to open - if not show chooser
//        List<String> params = getParameters().getRaw();
//        boolean fail;
//        if (params.size() >= 1) {
//            String filename = params.get(0);
//            
//            fail = openFile(new File(filename));
//        } else {
//            File f = showFileChooser();
//            if (f == null)
//                System.exit(0);
//            fail = openFile(f);
//        }
//        
//        //Deal with files which can't be opened
//        if (fail) {
//            showOpenFailedDialog();
//        } else {
//            showPage(1);
//            stage.show();
//        }
//    }
//    
//    double lastOpacity = 0.0;   //Stores the last opacity value
//    long dueToEnd = -1;         //Stores the time when the last started 
//                                //transition was due to end
//    /**
//     * Fade the buttons opacities to a given level.
//     * @param op 
//     */
//    private void setButtonOpacities(double op) {
//        final int DUR = 250;
//        
//        //Avoid starting new transition during old transition
//        if (dueToEnd != -1 && System.currentTimeMillis() < dueToEnd)
//            return;
//        
//        for (Button n : buttonGroup) {
//            FadeTransition f = new FadeTransition(Duration.millis(250), n);
//            f.setFromValue(lastOpacity);
//            f.setToValue(op);
//            f.play();
//        }
//        
//        //Store when transition should end so we know when to allow new ones
//        dueToEnd = System.currentTimeMillis() + DUR;
//        
//        lastOpacity = op;
//    }
//    
//    /**
//     * Show a file chooser, returning a File.
//     * @return File or null if canceled
//     */
//    private File showFileChooser() {
//        FileChooser fc = new FileChooser();
//        fc.setTitle("Open PDF file...");
//        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));     
//        return fc.showOpenDialog(stage.getOwner());
//    }
//    
//    /**
//     * Open a file
//     * @param file
//     * @return true if failed
//     */
//    private boolean openFile(File file) {
//        if (file == null)
//            return true;
//        
//        String filename = file.getAbsolutePath();
//        
//        try {
//            pdf.openPdfFile(filename);
//        }catch(Exception e) {
//            return true;
//        }
//        
//        lastSuccessfullyLoadedFile = file;
//        return false;
//    }
//    
//    /**
//     * Show a dialog asking whether a user wants to try another file or not, and
//     * handle their decision.
//     */
//    private void showOpenFailedDialog() {
//
//        final Stage dialog = new Stage(StageStyle.UTILITY);
//        dialog.setTitle("File open failed");
//        Group dRoot = new Group();
//        Scene dScene = new Scene(dRoot);
//        dialog.setScene(dScene);
//        
//        BorderPane bPane = new BorderPane();
//        
//        Text text = new Text("Could not load file. Would you like to try loading another file?");
//        
//        HBox buttonBox = new HBox();
//        buttonBox.setPadding(new Insets(15,12,15,12));
//        buttonBox.setSpacing(10);
//        buttonBox.setAlignment(Pos.CENTER_RIGHT);
//        
//        Button yButton = new Button("Yes");
//        yButton.setOnAction(new EventHandler<ActionEvent>() {
//            public void handle(ActionEvent e) {
//                //Make dialog invisible (not using hide() because in some 
//                //situations JFX will decide (despite the fact Platform.runLater
//                //is in use) that the application is finished and exit
//                dialog.setOpacity(0.0);
//
//                //Show chooser. If cancel, reload last file, or if there isn't 
//                //one, quit. Otherwise, try to open file.
//                File f = showFileChooser();
//                boolean fail = false;;
//                if (f == null) {
//                    if (lastSuccessfullyLoadedFile != null) 
//                        fail = openFile(lastSuccessfullyLoadedFile);
//                    else 
//                        System.exit(0);
//                } else 
//                    fail = openFile(f);
//
//                //Handle fail
//                if (fail) 
//                    showOpenFailedDialog();
//
//                //If opened successfully, show page and stage
//                if (!fail && f != null) {
//                    Platform.runLater(new Runnable() {
//                        public void run() {
//                            stage.show();
//                        }
//                    });
//                    showPage(1);
//                }
//
//                //Now hide dialog properly
//                dialog.hide();
//            }
//        });
//        
//        Button nButton = new Button("No");
//        EventHandler noHandler = new EventHandler() {
//            public void handle(Event e) {
//                dialog.hide();
//                
//                //Open last file or quit if no files previously opened
//                boolean fail = false;
//                if (lastSuccessfullyLoadedFile != null) 
//                    fail = openFile(lastSuccessfullyLoadedFile);
//                else
//                    System.exit(0);
//
//                //Handle fail
//                if (fail) 
//                    showOpenFailedDialog();
//            }
//        };
//        nButton.setOnAction(noHandler);
//        buttonBox.getChildren().addAll(yButton,nButton);
//        
//        dialog.setOnCloseRequest(noHandler);
//        
//        bPane.setCenter(text);
//        bPane.setBottom(buttonBox);
//        dRoot.getChildren().add(bPane);
//        
//        dialog.sizeToScene();
//        dialog.show();
//        
//    }
//    
//    /**
//     * Update the GUI to show a specified page.
//     * @param page 
//     */
//    private void showPage(int page) {
//        
//        //Check in range
//        if (page > pdf.getPageCount())
//            return;
//        if (page < 1)
//            return;
//        
//        //Store
//        pageNumber = page;
//        
//        
//        //Show/hide buttons as neccessary
//        if (page == pdf.getPageCount())
//            next.setVisible(false);
//        else
//            next.setVisible(true);
//        
//        if (page == 1)
//            back.setVisible(false);
//        else
//            back.setVisible(true);
//        
//        
//        //Calculate scale
//        int pW = pdf.getPdfPageData().getCropBoxWidth(page);
//        int pH = pdf.getPdfPageData().getCropBoxHeight(page);
//        
//        Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
//        
//        s.width -= 100;
//        s.height -= 100;
//        
//        double xScale = (double)s.width / pW;
//        double yScale = (double)s.height / pH;
//        double scale = xScale < yScale ? xScale : yScale;
//        
//        //Work out target size
//        pW *= scale;
//        pH *= scale;
//
//        //Get image and set
//        Image i = getPageAsImage(page,pW,pH);
//        imageView.setImage(i);
//
//        //Set size of components
//        imageView.setFitWidth(pW);
//        imageView.setFitHeight(pH);
//        stage.setWidth(imageView.getFitWidth()+2);
//        stage.setHeight(imageView.getFitHeight()+2);
//        stage.centerOnScreen();
//    }
//    
//    /**
//     * Wrapper for usual method since JFX has no BufferedImage support.
//     * @param page
//     * @param width
//     * @param height
//     * @return 
//     */
//    private Image getPageAsImage(int page, int width, int height) {
//        
//        BufferedImage img;
//        try {
//            img = pdf.getPageAsImage(page);
//            
//            //Use deprecated method since there's no real alternative
//            if (Image.impl_isExternalFormatSupported(BufferedImage.class))
//                return javafx.scene.image.Image.impl_fromExternalImage(img);
//            
//        } catch(Exception e) {
//            e.printStackTrace();
//        }
//        
//        return null;
//    }
//
//    
//    
//    private class MouseListener implements EventHandler<MouseEvent> {
//
//        public MouseListener() {
//        }
//
//        public void handle(MouseEvent e) {
//            EventType type = e.getEventType();
//            if (type == MouseEvent.MOUSE_MOVED) {
//                
//                //Set time to disappear
//                scheduleDisappearTime = System.currentTimeMillis()+1700;
//                
//                //Make buttons visible
//                setButtonOpacities(0.75);
//                
//                //If thread doesn't exist, create it
//                if (visibilityThread == null) {
//                    visibilityThread = new Thread() {
//
//                        public void run() {
//                            //Keep comparing to scheduled time (which is updated outside of thread)
//                            while (System.currentTimeMillis() < scheduleDisappearTime 
//                                    || next.isHover() || back.isHover() || quit.isHover() || min.isHover() || open.isHover()) {
//                                try {
//                                    Thread.sleep(50);
//                                } catch (InterruptedException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                            
//                            //Make buttons disappear
//                            setButtonOpacities(0.0);
//                            
//                            //Destroy thread
//                            visibilityThread = null;
//
//                        }
//                    };
//                    visibilityThread.start();
//                }
//            }
//        }
//    }
//}
