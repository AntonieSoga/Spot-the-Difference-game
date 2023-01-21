package com.mycompany.proiectip;

import com.mycompany.proiectip.design.adaper.KeyboardAdapter;
import com.mycompany.proiectip.design.adaper.QButton;
import com.mycompany.proiectip.design.adaper.QuitButton;
import com.mycompany.proiectip.design.command.ContinuousMouseMovementCommand;
import com.mycompany.proiectip.design.command.Invoker;
import com.mycompany.proiectip.design.singleton.Singleton;
import com.mycompany.proiectip.design.strategy.CreditCardStrategy;
import com.mycompany.proiectip.design.strategy.Item;
import com.mycompany.proiectip.design.strategy.ShoppingCart;
import com.mycompany.proiectip.design.memento.JInputFieldMemento;
import com.mycompany.proiectip.design.memento.JPasswordFieldMemento;
import com.mycompany.proiectip.design.memento.JInputField1;
import com.mycompany.proiectip.design.memento.JPasswordField1;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 *
 * @author antonie
 */
public class MainFrame extends javax.swing.JFrame {

    public void timerSetup(javax.swing.JLabel label, javax.swing.JPanel Panel, int seconds) {
        Timer timer = new Timer(); //new timer

        TimerTask task = new TimerTask() {
            int s = seconds;

            public void run() {
                label.setText("Time left: " + Integer.toString(s)); //the timer lable to counter.
                s--;
                if (GameWon(1)) {
                    timer.cancel();
                } else if (s == -1) {
                    //game lost 
                    outOfTime();

                } else if (s == -5) {
                    OutOfTime.setVisible(false);
                    timer.cancel();
                    Levels.setVisible(true);

                } else if (giveup1Btn.isSelected()) {
                    timer.cancel();
                }

                if (Levels.isVisible()) {
                    timer.cancel();
                }

            }
        };
        timer.scheduleAtFixedRate(task,
                0, 1000);
    }

    public void outOfTime() {
        Nivel1.setVisible(false);
        Nivel2.setVisible(false);
        Nivel3.setVisible(false);
        Won.setVisible(false);
        OutOfTime.setVisible(true);
        resetLevel();
    }

    public void levelSetup(javax.swing.JPanel Panel, javax.swing.JLabel label, int level, int time, javax.swing.JLabel timeLeft) {
        Levels.setVisible(false);
        Panel.setVisible(true);
        timerSetup(timeLeft, Panel, time);

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("graphics/imagini/" + String.valueOf(level) + "q.jpg"));
        } catch (IOException e) {
        }
        Image resultingImage = img.getScaledInstance(label.getWidth(), label.getHeight(), Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(resultingImage));
    }

    private void hintsSetup() {
        hintsLevel1.setText("Hints left: " + String.valueOf(Singleton.getInstance().getHints()));
        hintsLevels.setText("Current Hints: " + String.valueOf(Singleton.getInstance().getHints()));
        storeHints.setText("Currents Hints: " + String.valueOf(Singleton.getInstance().getHints()));
        gasit.setText("Gasit " + String.valueOf(Singleton.getInstance().getGasitValue()) + "/7");

    }
//test git

    public void buttonSound() {
        Music bs = new Music();
        bs.setFile("graphics/sounds/mixkit-modern-technology-select-3124.wav");
        bs.play(false);
    }

    ShoppingCart cart = new ShoppingCart();
    Item hint5 = new Item("5hint-uri", 5);
    Item hint10 = new Item("10hint-uri", 10);
    Item hint15 = new Item("15hint-uri", 15);

    Invoker invoker = new Invoker();

    public MainFrame() throws IOException {
        initComponents();   
        this.setLocationRelativeTo(null);
        hintsSetup();
        Singleton.getInstance().bgMusic.setFile("graphics/sounds/TremLoadingloopl.wav");
        Singleton.getInstance().bgMusic.play(true);

    }

    public Boolean isLevel1Completed() {

        List<JButton> exclude = new ArrayList<>(Arrays.asList(useHint1, giveup1Btn, soundBtn1));

        for (Component c : jLayeredPane2.getComponents()) {

            if (c instanceof JButton && !(exclude.contains(c))) {
                JButton button = (JButton) c;
                if (!button.isBorderPainted()) {
                    return false;
                }

            }
        }
        win();
        return true;
    }

    public void imageSetup(JButton button, String path, int width, int height) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(path));
        } catch (IOException e) {
        }
        Image resultingImage = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        button.setIcon(new ImageIcon(resultingImage));
    }

    public void imageSetup(JLabel label, String path, int width, int height) {
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(new File(path));
        } catch (IOException e) {
        }
        Image resultingImage = bi.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        label.setIcon(new ImageIcon(resultingImage));
    }

    public void resetLevel() {
        Singleton.getInstance().setGasitValue(0);
        gasit.setText("Gasit 0/7");
        List<JButton> exclude = new ArrayList<>(Arrays.asList(useHint1, giveup1Btn, soundBtn1));

        for (Component c : jLayeredPane2.getComponents()) {

            if (c instanceof JButton && !exclude.contains(c)) {
                JButton button = (JButton) c;
                button.setEnabled(true);
                button.setBorderPainted(false);

            }
        }

    }

    /**
     *
     * @param anuleaza
     * @param confirma
     * @param ST
     * @param text
     */
    public void confirma(JButton anuleaza, JButton confirma, JLabel ST, String text) {

        backToStoreBtn.setEnabled(false);
        anuleaza.setEnabled(false);
        paymentCombo.setEnabled(false);
        confirma.setEnabled(false);
        cart.pay(new CreditCardStrategy(nume.getText(), cardNumber.getText(), cvv.getText(), expDate.getText()));
        ST.setText("Status Tranzacite: ai platit: " + String.valueOf(cart.calculateTotal()) + " lei folosind " + text);
        Timer timer = new Timer(); //new timer
        jLabel16.setVisible(true);
        jLabel15.setVisible(true);

        TimerTask task = new TimerTask() {
            int s = 2;

            public void run() {

                s--;
                if (s == -1) {
                    jLabel16.setVisible(false);
                    jLabel15.setVisible(false);
                    ST.setText("");
                    Singleton.getInstance().setHints(Singleton.getInstance().getHints() + cart.calculateTotal());
                    hintsSetup();
                    Menu.setVisible(true);
                    cart.removeItem(hint5);
                    cart.removeItem(hint15);
                    cart.removeItem(hint10);
                    Cart.setVisible(false);
                    hints5.setBackground(Color.white);
                    hints10.setBackground(Color.white);
                    hints15.setBackground(Color.white);
                    backToStoreBtn.setEnabled(true);
                    anuleaza.setEnabled(true);
                    paymentCombo.setEnabled(true);
                    confirma.setEnabled(true);
                }

            }
        };
        timer.scheduleAtFixedRate(task,
                0, 900);

    }

    public Music musicSetup(Music music) {
        music = new Music();
        music.setFile("graphics/sounds/TremLoadingloopl.wav");
        return music;
    }

    public void buttonSetup(javax.swing.JButton jButton1, javax.swing.JButton jButton2) {
        jButton1.setBorderPainted(true);
        jButton2.setBorderPainted(true);
        jButton1.setEnabled(false);
        jButton2.setEnabled(false);

        buttonSound();
        Singleton.getInstance().setGasitValue(Singleton.getInstance().getGasitValue() + 1);
        gasit.setText("Gasit " + String.valueOf(Singleton.getInstance().getGasitValue()) + "/7");
        if (GameWon(1)) {
            win();
        }
    }

    public void win() {
        Nivel1.setVisible(false);
        Nivel2.setVisible(false);
        Nivel3.setVisible(false);
        Won.setVisible(true);

    }

    public Boolean GameWon(int level) {
        if (level == 1) {
            return (isLevel1Completed());
        }

        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                              
    private void initComponents() {

        //command
        //end
        jLayeredPane1 = new javax.swing.JLayeredPane();
        Menu = new javax.swing.JPanel();
        playBtn = new javax.swing.JButton();
        quitBtn = new javax.swing.JButton();
        storeBtn = new javax.swing.JButton();
        soundBtn = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        Store = new javax.swing.JPanel();
        cartBtn = new javax.swing.JButton();
        backbtn = new javax.swing.JButton();
        hints15 = new javax.swing.JPanel();
        hints15Btn = new javax.swing.JButton();
        hints5 = new javax.swing.JPanel();
        hints5Btn = new javax.swing.JButton();
        hints10 = new javax.swing.JPanel();
        hints10Btn = new javax.swing.JButton();
        storeHints = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        Cart = new javax.swing.JPanel();
        jLayeredPane4 = new javax.swing.JLayeredPane();
        backToStoreBtn = new javax.swing.JButton();
        paymentCombo = new javax.swing.JComboBox<>();
        labelForPayment = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLayeredPane5 = new javax.swing.JLayeredPane();
        redirect = new javax.swing.JLabel();
        PayPalPanel = new javax.swing.JPanel();
        password = new javax.swing.JPasswordField();
        email = new javax.swing.JTextField();
        confirmaPP = new javax.swing.JButton();
        STPP = new javax.swing.JLabel();
        anuleazaPP = new javax.swing.JButton();
        imgPP = new javax.swing.JLabel();
        CreditCardPanel = new javax.swing.JPanel();
        nume = new javax.swing.JTextField();
        cardNumber = new javax.swing.JTextField();
        cvv = new javax.swing.JTextField();
        expDate = new javax.swing.JTextField();
        confirmaCC = new javax.swing.JButton();
        STCC = new javax.swing.JLabel();
        anuleazaCC = new javax.swing.JButton();
        imgCC = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        dePlatit = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        Levels = new javax.swing.JPanel();
        backBtn = new javax.swing.JButton();
        nivel3Btn = new javax.swing.JButton();
        nivel2Btn = new javax.swing.JButton();
        nivel1Btn = new javax.swing.JButton();
        hintsLevels = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Nivel1 = new javax.swing.JPanel();
        jLayeredPane2 = new javax.swing.JLayeredPane();
        filler1 = new javax.swing.Box.Filler(new java.awt.Dimension(0, 720), new java.awt.Dimension(0, 720), new java.awt.Dimension(32767, 720));
        gasit = new javax.swing.JLabel();
        soundBtn1 = new javax.swing.JButton();
        stanga1 = new javax.swing.JButton();
        dreapta2 = new javax.swing.JButton();
        stanga3 = new javax.swing.JButton();
        dreapta4 = new javax.swing.JButton();
        stanga5 = new javax.swing.JButton();
        dreapta6 = new javax.swing.JButton();
        dreapta7 = new javax.swing.JButton();
        dreapta1 = new javax.swing.JButton();
        stanga2 = new javax.swing.JButton();
        dreapta3 = new javax.swing.JButton();
        stanga4 = new javax.swing.JButton();
        dreapta5 = new javax.swing.JButton();
        stanga6 = new javax.swing.JButton();
        stanga7 = new javax.swing.JButton();
        imgLvl1 = new javax.swing.JLabel();
        giveup1Btn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        timeLeft1 = new javax.swing.JLabel();
        hintsLevel1 = new javax.swing.JLabel();
        useHint1 = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        Nivel2 = new javax.swing.JPanel();
        jLayeredPane3 = new javax.swing.JLayeredPane();
        soundBtn2 = new javax.swing.JButton();
        timeLeft2 = new javax.swing.JLabel();
        giveup2Btn = new javax.swing.JButton();
        imgLvl2 = new javax.swing.JLabel();
        Jlabelnivel2 = new javax.swing.JLabel();
        Nivel3 = new javax.swing.JPanel();
        giveup3Btn = new javax.swing.JButton();
        imgLvl3 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        timeLeft3 = new javax.swing.JLabel();
        soundBtn3 = new javax.swing.JButton();
        Won = new javax.swing.JPanel();
        jLayeredPane7 = new javax.swing.JLayeredPane();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        OutOfTime = new javax.swing.JPanel();
        jLayeredPane6 = new javax.swing.JLayeredPane();
        jLabel14 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(400, 200));
        setPreferredSize(new java.awt.Dimension(1296, 759));
        setResizable(false);

        java.awt.FlowLayout flowLayout1 = new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0);
        flowLayout1.setAlignOnBaseline(true);
        jLayeredPane1.setLayout(flowLayout1);

        Menu.setAlignmentX(600);
        Menu.setAlignmentY(400);
        Menu.setPreferredSize(new java.awt.Dimension(1280, 720));
        Menu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        playBtn.setBackground(new java.awt.Color(204, 0, 153));
        playBtn.setBorderPainted(false);
        playBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                playBtnMouseClicked(evt);
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                playBtnMouseEntered(evt);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                playBtnMouseExited(evt);
            }
        });
        playBtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            playBtnActionPerformed(evt);
        });
        Menu.add(playBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(490, 200, 240, 70));
        imageSetup(playBtn, "graphics/imagini/UI/PNG/blue_button04.png", 240, 70);

        quitBtn.setBackground(new java.awt.Color(51, 51, 255));
        quitBtn.setBorderPainted(false);
        quitBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                quitBtnMouseEntered(evt);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                quitBtnMouseExited(evt);
            }
        });
        quitBtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            quitBtnActionPerformed(evt);
        });
        Menu.add(quitBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(520, 330, 180, 60));
        imageSetup(quitBtn, "graphics/imagini/UI/PNG/red_button01.png", 180, 60);

        storeBtn.setBackground(new java.awt.Color(204, 204, 255));
        storeBtn.setBorderPainted(false);
        storeBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                storeBtnMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                storeBtnMouseExited(evt);
            }
        });
        storeBtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            storeBtnActionPerformed(evt);
        });
        Menu.add(storeBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 270, 220, 60));
        imageSetup(storeBtn, "graphics/imagini/UI/PNG/yellow_button00.png", 220, 60);

        soundBtn.setBorderPainted(false);
        soundBtn.setContentAreaFilled(false);
        soundBtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            soundBtnActionPerformed(evt);
        });
        Menu.add(soundBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(54, 37, 70, 70));
        imageSetup(soundBtn, "graphics/imagini/soundON.png", 70, 70);

        Menu.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        imageSetup(jLabel2, "graphics/imagini/background.png", 1280, 720);
        jLayeredPane1.add(Menu);

        Store.setBackground(new java.awt.Color(255, 255, 0));
        Store.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 153, 0), 4, true));
        Store.setPreferredSize(new java.awt.Dimension(1280, 720));
        Store.setVisible(false);

        cartBtn.setBackground(new java.awt.Color(51, 0, 255));
        cartBtn.setFont(new java.awt.Font("Showcard Gothic", 3, 36)); // NOI18N
        cartBtn.setForeground(new java.awt.Color(255, 255, 255));
        cartBtn.setText("Cart");
        cartBtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 0), 3, true));
        cartBtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            cartBtnActionPerformed(evt);
        });

        backbtn.setBackground(new java.awt.Color(255, 51, 0));
        backbtn.setFont(new java.awt.Font("Stencil", 3, 36)); // NOI18N
        backbtn.setForeground(new java.awt.Color(255, 255, 255));
        backbtn.setText("Back");
        backbtn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 102, 0), 3, true));
        backbtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            backbtnActionPerformed(evt);
        });

        hints15Btn.setIcon(new javax.swing.ImageIcon("graphics/imagini/hints15img.png")); // NOI18N
        hints15Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            hints15BtnActionPerformed(evt);
        });

        javax.swing.GroupLayout hints15Layout = new javax.swing.GroupLayout(hints15);
        hints15.setLayout(hints15Layout);
        hints15Layout.setHorizontalGroup(
                hints15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(hints15Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(hints15Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(26, Short.MAX_VALUE))
        );
        hints15Layout.setVerticalGroup(
                hints15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(hints15Layout.createSequentialGroup()
                                .addGap(35, 35, 35)
                                .addComponent(hints15Btn)
                                .addContainerGap(34, Short.MAX_VALUE))
        );

        hints5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                hints5MouseEntered(evt);
            }
        });

        if (hints5Btn.hasFocus()) {
            hints5Btn.setBackground(Color.red);
        }
        hints5Btn.setIcon(new javax.swing.ImageIcon("graphics/imagini/hnts5img.png")); // NOI18N
        hints5Btn.addActionListener(new ActionListenerImpl());

        javax.swing.GroupLayout hints5Layout = new javax.swing.GroupLayout(hints5);
        hints5.setLayout(hints5Layout);
        hints5Layout.setHorizontalGroup(
                hints5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(hints5Layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(hints5Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(23, Short.MAX_VALUE))
        );
        hints5Layout.setVerticalGroup(
                hints5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(hints5Layout.createSequentialGroup()
                                .addGap(34, 34, 34)
                                .addComponent(hints5Btn)
                                .addContainerGap(35, Short.MAX_VALUE))
        );

        hints10Btn.setIcon(new javax.swing.ImageIcon("graphics/imagini/hints10img.png")); // NOI18N
        hints10Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            hints10BtnActionPerformed(evt);
        });

        javax.swing.GroupLayout hints10Layout = new javax.swing.GroupLayout(hints10);
        hints10.setLayout(hints10Layout);
        hints10Layout.setHorizontalGroup(
                hints10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(hints10Layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(hints10Btn, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(23, Short.MAX_VALUE))
        );
        hints10Layout.setVerticalGroup(
                hints10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, hints10Layout.createSequentialGroup()
                                .addContainerGap(40, Short.MAX_VALUE)
                                .addComponent(hints10Btn)
                                .addGap(29, 29, 29))
        );

        storeHints.setFont(new java.awt.Font("Stencil", 3, 24)); // NOI18N
        storeHints.setText("Current Hints : 0");
        storeHints.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            storeHintsPropertyChange(evt);
        });

        jLabel4.setFont(new java.awt.Font("Snap ITC", 0, 18)); // NOI18N
        jLabel4.setText("5 lei");

        jLabel5.setFont(new java.awt.Font("Snap ITC", 0, 18)); // NOI18N
        jLabel5.setText("10 lei");

        jLabel6.setFont(new java.awt.Font("Snap ITC", 0, 18)); // NOI18N
        jLabel6.setText("15 lei");

        javax.swing.GroupLayout StoreLayout = new javax.swing.GroupLayout(Store);
        Store.setLayout(StoreLayout);
        StoreLayout.setHorizontalGroup(
                StoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(StoreLayout.createSequentialGroup()
                                .addGap(146, 146, 146)
                                .addComponent(hints5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                                .addComponent(hints10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(120, 120, 120)
                                .addComponent(hints15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(123, 123, 123))
                        .addGroup(StoreLayout.createSequentialGroup()
                                .addGap(246, 246, 246)
                                .addComponent(jLabel4)
                                .addGap(318, 318, 318)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(212, 212, 212))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StoreLayout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(StoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StoreLayout.createSequentialGroup()
                                                .addComponent(cartBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(34, 34, 34)
                                                .addComponent(backbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(38, 38, 38))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StoreLayout.createSequentialGroup()
                                                .addComponent(storeHints, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(14, 14, 14))))
        );
        StoreLayout.setVerticalGroup(
                StoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(StoreLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(storeHints, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(73, 73, 73)
                                .addGroup(StoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(hints5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(hints15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(hints10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(StoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(StoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addComponent(jLabel6))
                                .addGroup(StoreLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(StoreLayout.createSequentialGroup()
                                                .addGap(94, 94, 94)
                                                .addComponent(cartBtn)
                                                .addContainerGap(105, Short.MAX_VALUE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StoreLayout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(backbtn, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(65, 65, 65))))
        );

        hints15.setBackground(Color.white);
        hints5.setBackground(Color.white);
        hints10.setBackground(Color.white);

        jLayeredPane1.add(Store);

        Cart.setVisible(false);
        Cart.setBackground(new java.awt.Color(102, 204, 255));
        Cart.setPreferredSize(new java.awt.Dimension(1280, 720));

        backToStoreBtn.setFont(new java.awt.Font("Showcard Gothic", 3, 24)); // NOI18N
        backToStoreBtn.setForeground(new java.awt.Color(51, 153, 255));
        backToStoreBtn.setText("Back to Store");
        backToStoreBtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            backToStoreBtnActionPerformed(evt);
        });

        paymentCombo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"PayPal", "Credit Card"}));
        paymentCombo.addActionListener((java.awt.event.ActionEvent evt) -> {
            paymentComboActionPerformed(evt);
        });

        labelForPayment.setFont(new java.awt.Font("Segoe UI Semibold", 0, 24)); // NOI18N
        labelForPayment.setText("Pay by:");

        jLayeredPane5.setLayout(new javax.swing.OverlayLayout(jLayeredPane5));

        redirect.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLayeredPane5.add(redirect);

        PayPalPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        password.setText("password");
        password.setToolTipText("");
        password.setName(""); // NOI18N
        PayPalPanel.add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 100, -1));

        email.setText("email");
        PayPalPanel.add(email, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 224, -1));

        confirmaPP.setText("Confirma");
        confirmaPP.addActionListener((java.awt.event.ActionEvent evt) -> {
            confirmaPPActionPerformed(evt);
        });
        PayPalPanel.add(confirmaPP, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 320, 90, 30));

        STPP.setText("Status Tranzactie: ");
        PayPalPanel.add(STPP, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 530, 40));

        anuleazaPP.setText("Anuleaza");
        anuleazaPP.addActionListener((java.awt.event.ActionEvent evt) -> {
            anuleazaPPActionPerformed(evt);
        });
        PayPalPanel.add(anuleazaPP, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 320, 80, 30));
        PayPalPanel.add(imgPP, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1110, 580));
        imageSetup(imgPP, "graphics/imagini/PPimg.png", 1280, 720);

        jLayeredPane5.add(PayPalPanel);

        CreditCardPanel.setVisible(false);
        CreditCardPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        nume.setText("Nume complet");
        nume.addActionListener((java.awt.event.ActionEvent evt) -> {
            numeActionPerformed(evt);
        });
        CreditCardPanel.add(nume, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 290, 150, 30));

        cardNumber.setText("CARD NUMBER");
        CreditCardPanel.add(cardNumber, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 330, 240, 30));

        cvv.setText("CVV");
        CreditCardPanel.add(cvv, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 370, -1, 30));

        expDate.setText("EXP DATE");
        CreditCardPanel.add(expDate, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, 80, 30));

        confirmaCC.setText("Confirma");
        confirmaCC.addActionListener((java.awt.event.ActionEvent evt) -> {
            confirmaCCActionPerformed(evt);
        });
        CreditCardPanel.add(confirmaCC, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 410, 150, 30));

        STCC.setText("Status Tranzactie: ");
        CreditCardPanel.add(STCC, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 450, 480, 40));

        anuleazaCC.setText("Anuleaza");
        anuleazaCC.addActionListener((java.awt.event.ActionEvent evt) -> {
            anuleazaCCActionPerformed(evt);
        });
        CreditCardPanel.add(anuleazaCC, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 410, 140, 30));
        CreditCardPanel.add(imgCC, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1160, 580));
        imageSetup(imgCC, "graphics/imagini/CCimg.png", 1280, 720);
        jLayeredPane5.add(CreditCardPanel);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 796, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane5)
        );

        jLabel15.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel15.setText("Veti fi redirectionat");

        dePlatit.setFont(new java.awt.Font("Sitka Text", 2, 24)); // NOI18N
        dePlatit.setText("Aveti de platit: 0 lei");

        jLabel16.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel16.setText("catre meniul principal ");

        jLayeredPane4.setLayer(backToStoreBtn, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(paymentCombo, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(labelForPayment, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(jPanel1, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(jLabel15, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(dePlatit, javax.swing.JLayeredPane.DEFAULT_LAYER);
        jLayeredPane4.setLayer(jLabel16, javax.swing.JLayeredPane.DEFAULT_LAYER);

        javax.swing.GroupLayout jLayeredPane4Layout = new javax.swing.GroupLayout(jLayeredPane4);
        jLayeredPane4.setLayout(jLayeredPane4Layout);
        jLayeredPane4Layout.setHorizontalGroup(
                jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                .addGroup(jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(backToStoreBtn)
                                        .addGroup(jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                                        .addGap(207, 207, 207)
                                                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                                        .addGap(135, 135, 135)
                                                        .addComponent(labelForPayment, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(paymentCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(176, 176, 176)
                                                        .addComponent(dePlatit))))
                                .addGroup(jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                                .addGap(64, 64, 64)
                                                .addComponent(jLabel15))
                                        .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                                .addGap(50, 50, 50)
                                                .addComponent(jLabel16)))
                                .addContainerGap(54, Short.MAX_VALUE))
        );
        jLayeredPane4Layout.setVerticalGroup(
                jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                .addGap(69, 69, 69)
                                .addGroup(jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(labelForPayment)
                                        .addComponent(paymentCombo, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dePlatit))
                                .addGroup(jLayeredPane4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                                .addGap(22, 22, 22)
                                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jLayeredPane4Layout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addComponent(jLabel15)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(backToStoreBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(24, Short.MAX_VALUE))
        );

        jPanel1.setLocation(jPanel1.getX() + 50, jPanel1.getY());
        jLabel15.setVisible(false);
        jLabel16.setVisible(false);

        javax.swing.GroupLayout CartLayout = new javax.swing.GroupLayout(Cart);
        Cart.setLayout(CartLayout);
        CartLayout.setHorizontalGroup(
                CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane4)
        );
        CartLayout.setVerticalGroup(
                CartLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane4)
        );

        jLayeredPane1.add(Cart);

        Levels.setVisible(false);
        Levels.setBackground(new java.awt.Color(204, 255, 204));
        Levels.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        backBtn.setBackground(new java.awt.Color(153, 153, 255));
        backBtn.setFont(new java.awt.Font("Showcard Gothic", 1, 48)); // NOI18N
        backBtn.setForeground(new java.awt.Color(255, 255, 255));
        backBtn.setText("MENU");
        backBtn.setAlignmentY(-0.5F);
        backBtn.addActionListener((java.awt.event.ActionEvent evt) -> {
            backBtnActionPerformed(evt);
        });
        Levels.add(backBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(1034, 615, 240, 80));

        nivel3Btn.setBackground(new java.awt.Color(204, 255, 255));
        nivel3Btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 10, true));
        nivel3Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            nivel3BtnActionPerformed(evt);
        });
        Levels.add(nivel3Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(880, 200, 340, 339));
        imageSetup(nivel3Btn, "graphics/imagini/3q.jpg", 340, 340);
        nivel2Btn.setBackground(new java.awt.Color(204, 255, 255));
        nivel2Btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 10, true));
        nivel2Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            nivel2BtnActionPerformed(evt);
        });
        Levels.add(nivel2Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 130, 340, 339));
        imageSetup(nivel2Btn, "graphics/imagini/2q.jpg", 340, 340);
        nivel1Btn.setBackground(new java.awt.Color(204, 255, 255));
        nivel1Btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 10, true));
        nivel1Btn.setPreferredSize(new java.awt.Dimension(261, 339));
        nivel1Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            nivel1BtnActionPerformed(evt);
        });
        Levels.add(nivel1Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 190, 340, 340));
        imageSetup(nivel1Btn, "graphics/imagini/1q.jpg", 340, 340);
        hintsLevels.setText("Current Hints : 0");
        Levels.add(hintsLevels, new org.netbeans.lib.awtextra.AbsoluteConstraints(1103, 28, 131, 34));

        jLabel7.setFont(new java.awt.Font("Showcard Gothic", 1, 36)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 204, 255));
        jLabel7.setText("Nivel 1");
        Levels.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 150, -1, -1));

        jLabel8.setFont(new java.awt.Font("Showcard Gothic", 1, 36)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(204, 204, 255));
        jLabel8.setText("Nivel 2");
        Levels.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 80, 160, 60));

        jLabel9.setFont(new java.awt.Font("Showcard Gothic", 1, 36)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(204, 204, 255));
        jLabel9.setText("Nivel 3");
        Levels.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 160, 150, 50));
        Levels.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        imageSetup(jLabel10, "graphics/imagini/bgLevels.jpg", 1280, 720);
        jLayeredPane1.add(Levels);

        Nivel1.setBackground(new java.awt.Color(102, 255, 204));
        Nivel1.setPreferredSize(new java.awt.Dimension(1280, 720));
        Nivel1.setVisible(false);

        jLayeredPane2.setPreferredSize(new java.awt.Dimension(1280, 720));
        jLayeredPane2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        filler1.setBackground(new java.awt.Color(255, 255, 255));
        filler1.setOpaque(true);
        jLayeredPane2.add(filler1, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 5, 684));

        gasit.setFont(new java.awt.Font("Showcard Gothic", 1, 18)); // NOI18N
        gasit.setForeground(new java.awt.Color(255, 255, 255));
        gasit.setText("GaSit 0/7");
        jLayeredPane2.add(gasit, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 200, 120, 30));

        soundBtn1.setBorderPainted(false);
        soundBtn1.setContentAreaFilled(false);
        soundBtn1.setPreferredSize(new java.awt.Dimension(70, 70));
        soundBtn1.addActionListener((java.awt.event.ActionEvent evt) -> {
            soundBtn1ActionPerformed(evt);
        });
        jLayeredPane2.add(soundBtn1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 390, 70, 70));

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("graphics/imagini/soundON.png"));
        } catch (IOException e) {
        }
        soundBtn1.setIcon(new ImageIcon(img));

        stanga1.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(102, 255, 204)));
        stanga1.setBorderPainted(false);
        stanga1.setContentAreaFilled(false);
        stanga1.addActionListener((java.awt.event.ActionEvent evt) -> {
            stanga1ActionPerformed(evt);
        });
        jLayeredPane2.add(stanga1, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 160, 30, 30));

        dreapta2.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 0)));
        dreapta2.setBorderPainted(false);
        dreapta2.setContentAreaFilled(false);
        dreapta2.addActionListener(this::dreapta2ActionPerformed);
        jLayeredPane2.add(dreapta2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 270, 40, 40));

        stanga3.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(255, 0, 51)));
        stanga3.setBorderPainted(false);
        stanga3.setContentAreaFilled(false);
        stanga3.addActionListener((java.awt.event.ActionEvent evt) -> {
            stanga3ActionPerformed(evt);
        });
        jLayeredPane2.add(stanga3, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 360, 50, 40));

        dreapta4.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 153, 153)));
        dreapta4.setBorderPainted(false);
        dreapta4.setContentAreaFilled(false);
        dreapta4.addActionListener((java.awt.event.ActionEvent evt) -> {
            dreapta4ActionPerformed(evt);
        });
        jLayeredPane2.add(dreapta4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 370, 40, 40));

        stanga5.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 255)));
        stanga5.setBorderPainted(false);
        stanga5.setContentAreaFilled(false);
        stanga5.addActionListener(this::stanga5ActionPerformed);
        jLayeredPane2.add(stanga5, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 580, 40, 40));

        dreapta6.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 153, 153)));
        dreapta6.setBorderPainted(false);
        dreapta6.setContentAreaFilled(false);
        dreapta6.addActionListener(this::dreapta6ActionPerformed);
        jLayeredPane2.add(dreapta6, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 600, 50, 70));

        dreapta7.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(102, 0, 102)));
        dreapta7.setBorderPainted(false);
        dreapta7.setContentAreaFilled(false);
        dreapta7.addActionListener(this::dreapta7ActionPerformed);
        jLayeredPane2.add(dreapta7, new org.netbeans.lib.awtextra.AbsoluteConstraints(900, 610, 40, 40));

        dreapta1.setBorder(javax.swing.BorderFactory.createMatteBorder(5, 5, 5, 5, new java.awt.Color(102, 255, 204)));
        dreapta1.setBorderPainted(false);
        dreapta1.setContentAreaFilled(false);
        dreapta1.addActionListener((java.awt.event.ActionEvent evt) -> {
            dreapta1ActionPerformed(evt);
        });
        jLayeredPane2.add(dreapta1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 150, 40, 40));

        stanga2.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 0)));
        stanga2.setBorderPainted(false);
        stanga2.setContentAreaFilled(false);
        stanga2.addActionListener((java.awt.event.ActionEvent evt) -> {
            stanga2ActionPerformed(evt);
        });
        jLayeredPane2.add(stanga2, new org.netbeans.lib.awtextra.AbsoluteConstraints(660, 260, 40, 40));

        dreapta3.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(255, 0, 51)));
        dreapta3.setBorderPainted(false);
        dreapta3.setContentAreaFilled(false);
        dreapta3.addActionListener(this::dreapta3ActionPerformed);
        jLayeredPane2.add(dreapta3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 360, 50, 40));

        stanga4.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 153, 153)));
        stanga4.setBorderPainted(false);
        stanga4.setContentAreaFilled(false);
        stanga4.addActionListener((java.awt.event.ActionEvent evt) -> {
            stanga4ActionPerformed(evt);
        });
        jLayeredPane2.add(stanga4, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 360, 30, 40));

        dreapta5.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 0, 255)));
        dreapta5.setBorderPainted(false);
        dreapta5.setContentAreaFilled(false);
        dreapta5.addActionListener((java.awt.event.ActionEvent evt) -> {
            dreapta5ActionPerformed(evt);
        });
        jLayeredPane2.add(dreapta5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1020, 570, 40, 40));

        stanga6.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(0, 153, 153)));
        stanga6.setBorderPainted(false);
        stanga6.setContentAreaFilled(false);
        stanga6.addActionListener((java.awt.event.ActionEvent evt) -> {
            stanga6ActionPerformed(evt);
        });
        jLayeredPane2.add(stanga6, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 590, 50, 70));

        stanga7.setBorder(javax.swing.BorderFactory.createMatteBorder(4, 4, 4, 4, new java.awt.Color(102, 0, 102)));
        stanga7.setBorderPainted(false);
        stanga7.setContentAreaFilled(false);
        stanga7.addActionListener((java.awt.event.ActionEvent evt) -> {
            stanga7ActionPerformed(evt);
        });
        jLayeredPane2.add(stanga7, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 610, 40, 40));

        imgLvl1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imgLvl1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        imgLvl1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(255, 255, 255), 4, true));
        jLayeredPane2.add(imgLvl1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, 1024, 683));

        giveup1Btn.setText("Give up");
        giveup1Btn.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(153, 153, 255), 4, true));
        giveup1Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            giveup1BtnActionPerformed(evt);
        });
        jLayeredPane2.add(giveup1Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 530, 70, 30));

        jLabel1.setBackground(new java.awt.Color(204, 255, 255));
        jLabel1.setFont(new java.awt.Font("Serif", 3, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Level 1");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLayeredPane2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 70));

        timeLeft1.setFont(new java.awt.Font("Showcard Gothic", 2, 24)); // NOI18N
        timeLeft1.setForeground(new java.awt.Color(255, 255, 255));
        timeLeft1.setText("time left :");
        timeLeft1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jLayeredPane2.add(timeLeft1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 200, 40));

        hintsLevel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        hintsLevel1.setForeground(new java.awt.Color(255, 255, 255));
        hintsLevel1.setText("Hints Left: 0");
        hintsLevel1.addPropertyChangeListener((java.beans.PropertyChangeEvent evt) -> {
            hintsLevel1PropertyChange(evt);
        });
        jLayeredPane2.add(hintsLevel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 180, 50));

        useHint1.setText("Use Hint");
        useHint1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(102, 102, 255), 4, true));
        useHint1.addActionListener((java.awt.event.ActionEvent evt) -> {
            useHint1ActionPerformed(evt);
        });
        jLayeredPane2.add(useHint1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 140, 80, 40));
        jLayeredPane2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        imageSetup(jLabel11, "graphics/imagini/bglvl1.jpg", 1280, 720);

        javax.swing.GroupLayout Nivel1Layout = new javax.swing.GroupLayout(Nivel1);
        Nivel1.setLayout(Nivel1Layout);
        Nivel1Layout.setHorizontalGroup(
                Nivel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Nivel1Layout.createSequentialGroup()
                                .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        Nivel1Layout.setVerticalGroup(
                Nivel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, Nivel1Layout.createSequentialGroup()
                                .addComponent(jLayeredPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLayeredPane1.add(Nivel1);

        Nivel2.setBackground(new java.awt.Color(102, 255, 204));
        Nivel2.setPreferredSize(new java.awt.Dimension(1280, 720));
        Nivel1.setVisible(false);

        jLayeredPane3.setPreferredSize(new java.awt.Dimension(1280, 720));
        jLayeredPane3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        soundBtn2.setBorderPainted(false);
        soundBtn2.setContentAreaFilled(false);
        soundBtn2.setPreferredSize(new java.awt.Dimension(70, 70));
        soundBtn2.addActionListener((java.awt.event.ActionEvent evt) -> {
            soundBtn2ActionPerformed(evt);
        });
        jLayeredPane3.add(soundBtn2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1210, 0, 70, 70));
        soundBtn2.setIcon(new ImageIcon(img));

        timeLeft2.setBackground(new java.awt.Color(255, 255, 204));
        timeLeft2.setOpaque(true);
        jLayeredPane3.add(timeLeft2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 160, 180, 50));

        giveup2Btn.setText("Give up");
        giveup2Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            giveup2BtnActionPerformed(evt);
        });
        jLayeredPane3.add(giveup2Btn, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 610, -1, -1));
        jLayeredPane3.add(imgLvl2, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 30, 920, 660));

        Jlabelnivel2.setBackground(new java.awt.Color(204, 255, 255));
        Jlabelnivel2.setFont(new java.awt.Font("Serif", 2, 36)); // NOI18N
        Jlabelnivel2.setForeground(new java.awt.Color(0, 0, 204));
        Jlabelnivel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Jlabelnivel2.setText("Level 2");
        Jlabelnivel2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Jlabelnivel2.setOpaque(true);
        jLayeredPane3.add(Jlabelnivel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 140, 70));

        javax.swing.GroupLayout Nivel2Layout = new javax.swing.GroupLayout(Nivel2);
        Nivel2.setLayout(Nivel2Layout);
        Nivel2Layout.setHorizontalGroup(
                Nivel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Nivel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLayeredPane3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addContainerGap())
        );
        Nivel2Layout.setVerticalGroup(
                Nivel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Nivel2Layout.createSequentialGroup()
                                .addComponent(jLayeredPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 714, Short.MAX_VALUE)
                                .addContainerGap())
        );

        jLayeredPane1.add(Nivel2);

        Nivel3.setBackground(new java.awt.Color(102, 255, 204));
        Nivel3.setPreferredSize(new java.awt.Dimension(1280, 720));
        Nivel1.setVisible(false);

        giveup3Btn.setText("Give up");
        giveup3Btn.addActionListener((java.awt.event.ActionEvent evt) -> {
            giveup3BtnActionPerformed(evt);
        });

        imgLvl3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel3.setBackground(new java.awt.Color(204, 255, 255));
        jLabel3.setFont(new java.awt.Font("Serif", 2, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 204));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Level 3");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel3.setOpaque(true);

        soundBtn3.setBorderPainted(false);
        soundBtn3.setContentAreaFilled(false);
        soundBtn3.setPreferredSize(new java.awt.Dimension(70, 70));
        soundBtn3.addActionListener((java.awt.event.ActionEvent evt) -> {
            soundBtn3ActionPerformed(evt);
        });

        javax.swing.GroupLayout Nivel3Layout = new javax.swing.GroupLayout(Nivel3);
        Nivel3.setLayout(Nivel3Layout);
        Nivel3Layout.setHorizontalGroup(
                Nivel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Nivel3Layout.createSequentialGroup()
                                .addGroup(Nivel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(Nivel3Layout.createSequentialGroup()
                                                .addGap(41, 41, 41)
                                                .addComponent(giveup3Btn))
                                        .addGroup(Nivel3Layout.createSequentialGroup()
                                                .addGap(23, 23, 23)
                                                .addGroup(Nivel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(timeLeft3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                                .addComponent(imgLvl3, javax.swing.GroupLayout.PREFERRED_SIZE, 909, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(soundBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
        );
        Nivel3Layout.setVerticalGroup(
                Nivel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(Nivel3Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(Nivel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(soundBtn3, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(93, 93, 93)
                                .addComponent(timeLeft3, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(giveup3Btn)
                                .addGap(210, 210, 210))
                        .addGroup(Nivel3Layout.createSequentialGroup()
                                .addGap(56, 56, 56)
                                .addComponent(imgLvl3, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(85, Short.MAX_VALUE))
        );

        soundBtn3.setIcon(new ImageIcon(img));

        jLayeredPane1.add(Nivel3);

        jLayeredPane7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton2.setText("Quit");
        jButton2.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButton2ActionPerformed(evt);
        });
        jLayeredPane7.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 190, 30));

        jButton3.setText("Menu");
        jButton3.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButton3ActionPerformed(evt);
        });
        jLayeredPane7.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 30, 190, 30));

        jButton1.setText("Levels");
        jButton1.addActionListener((java.awt.event.ActionEvent evt) -> {
            jButton1ActionPerformed(evt);
        });
        jLayeredPane7.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 30));
        jLayeredPane7.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1280, 720));
        imageSetup(jLabel17, "graphics/imagini/win.jpg", 1280, 720);

        javax.swing.GroupLayout WonLayout = new javax.swing.GroupLayout(Won);
        Won.setLayout(WonLayout);
        WonLayout.setHorizontalGroup(
                WonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane7)
        );
        WonLayout.setVerticalGroup(
                WonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane7)
        );

        jLayeredPane1.add(Won);

        OutOfTime.setBackground(new java.awt.Color(255, 255, 0));

        jLayeredPane6.setBackground(new java.awt.Color(255, 255, 0));
        jLayeredPane6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel14.setFont(new java.awt.Font("Segoe UI Historic", 3, 18)); // NOI18N
        jLabel14.setText("You ran out of time .... :((");
        jLayeredPane6.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 640, 260, 30));

        jLabel12.setFont(new java.awt.Font("Segoe UI Black", 3, 24)); // NOI18N
        jLabel12.setText("Game Over!");
        jLayeredPane6.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 610, 160, 90));
        jLayeredPane6.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, -20, 1280, 720));
        imageSetup(jLabel13, "graphics/imagini/OutofTimebg.jpg", 1280, 720);

        javax.swing.GroupLayout OutOfTimeLayout = new javax.swing.GroupLayout(OutOfTime);
        OutOfTime.setLayout(OutOfTimeLayout);
        OutOfTimeLayout.setHorizontalGroup(
                OutOfTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane6)
        );
        OutOfTimeLayout.setVerticalGroup(
                OutOfTimeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane6)
        );

        jLayeredPane1.add(OutOfTime);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 3860, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLayeredPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                              


    private void playBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_playBtnActionPerformed
        // TODO add your handling code here:
        Menu.setVisible(false);
        Levels.setVisible(true);
        buttonSound();
    }//GEN-LAST:event_playBtnActionPerformed


    private void quitBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitBtnActionPerformed
        // TODO add your handling code here:
        System.exit(0);
        buttonSound();
    }//GEN-LAST:event_quitBtnActionPerformed

    private void backBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backBtnActionPerformed
        // TODO add your handling code here:
        Levels.setVisible(false);
        Menu.setVisible(true);
        buttonSound();

    }//GEN-LAST:event_backBtnActionPerformed

    private void nivel1BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nivel1BtnActionPerformed
        // TODO add your handling code here:
        levelSetup(Nivel1, imgLvl1, 1, 60, timeLeft1);
        buttonSound();

    }//GEN-LAST:event_nivel1BtnActionPerformed


    private void nivel2BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nivel2BtnActionPerformed
        // TODO add your handling code here:
        levelSetup(Nivel2, imgLvl2, 2, 45, timeLeft2);
        buttonSound();

    }//GEN-LAST:event_nivel2BtnActionPerformed

    private void nivel3BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nivel3BtnActionPerformed
        // TODO add your handling code here:

        levelSetup(Nivel3, imgLvl3, 3, 35, timeLeft3);
        buttonSound();
    }//GEN-LAST:event_nivel3BtnActionPerformed

    private void paymentComboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_paymentComboActionPerformed
        // TODO add your handling code here:
        buttonSound();
        if (paymentCombo.getSelectedItem().toString().equals("PayPal")) {
            PayPalPanel.setVisible(true);
            CreditCardPanel.setVisible(false);
        } else {
            CreditCardPanel.setVisible(true);
            PayPalPanel.setVisible(false);
        }
    }//GEN-LAST:event_paymentComboActionPerformed

    private void backToStoreBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backToStoreBtnActionPerformed
        // TODO add your handling code here:
        Cart.setVisible(false);
        Store.setVisible(true);
        buttonSound();
    }//GEN-LAST:event_backToStoreBtnActionPerformed

    private void storeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_storeBtnActionPerformed
        // TODO add your handling code here:
        Menu.setVisible(false);
        Store.setVisible(true);
        buttonSound();
    }//GEN-LAST:event_storeBtnActionPerformed

    private void giveup2BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_giveup2BtnActionPerformed
        // TODO add your handling code here:
        Nivel2.setVisible(false);
        Levels.setVisible(true);
        buttonSound();

    }//GEN-LAST:event_giveup2BtnActionPerformed

    private void giveup3BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_giveup3BtnActionPerformed
        // TODO add your handling code here:
        Nivel3.setVisible(false);
        Levels.setVisible(true);
        buttonSound();


    }//GEN-LAST:event_giveup3BtnActionPerformed

    private void stanga5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stanga5ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta5, stanga5);
    }//GEN-LAST:event_stanga5ActionPerformed


    private void dreapta6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dreapta6ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta6, stanga6);
    }//GEN-LAST:event_dreapta6ActionPerformed

    private void giveup1BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_giveup1BtnActionPerformed
        Nivel1.setVisible(false);
        Levels.setVisible(true);
        buttonSound();
        resetLevel();

    }//GEN-LAST:event_giveup1BtnActionPerformed

    private void dreapta1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dreapta1ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta1, stanga1);
    }//GEN-LAST:event_dreapta1ActionPerformed

    private void stanga4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stanga4ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta4, stanga4);
    }//GEN-LAST:event_stanga4ActionPerformed

    private void dreapta3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dreapta3ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta3, stanga3);
    }//GEN-LAST:event_dreapta3ActionPerformed

    private void stanga7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stanga7ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta7, stanga7);

    }//GEN-LAST:event_stanga7ActionPerformed

    private void backbtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backbtnActionPerformed
        // TODO add your handling code here:
        Store.setVisible(false);
        Menu.setVisible(true);
        buttonSound();
    }//GEN-LAST:event_backbtnActionPerformed

    private void cartBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cartBtnActionPerformed
        // TODO add your handling code here:
        Store.setVisible(false);
        int sum = cart.calculateTotal();
        dePlatit.setText("Aveti de platit: " + String.valueOf(sum) + " lei");
        Cart.setVisible(true);
        buttonSound();
        if (memento) {
            passwordField.restoreMemento(passwordMemento);
            inputField.restoreMemento(inputMemento);

            email.setText(inputField.getText());
            password.setText(passwordField.getText());
        }

    }//GEN-LAST:event_cartBtnActionPerformed

    private void hints5BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hints5BtnActionPerformed
        // TODO add your handling code here:
        buttonSound();
        if (hints5.getBackground().equals(Color.white)) {
            hints5.setBackground(Color.blue);
            cart.addItem(hint5);
        } else {
            hints5.setBackground(Color.white);
            cart.removeItem(hint5);
        }

    }//GEN-LAST:event_hints5BtnActionPerformed

    private void stanga2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stanga2ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta2, stanga2);
    }//GEN-LAST:event_stanga2ActionPerformed

    private void stanga6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stanga6ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta6, stanga6);
    }//GEN-LAST:event_stanga6ActionPerformed

    private void dreapta5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dreapta5ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta5, stanga5);
    }//GEN-LAST:event_dreapta5ActionPerformed

    private void stanga3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stanga3ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta3, stanga3);
    }//GEN-LAST:event_stanga3ActionPerformed

    private void dreapta7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dreapta7ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta7, stanga7);
    }//GEN-LAST:event_dreapta7ActionPerformed

    private void dreapta4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dreapta4ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta4, stanga4);
    }//GEN-LAST:event_dreapta4ActionPerformed

    private void dreapta2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dreapta2ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta2, stanga2);
    }//GEN-LAST:event_dreapta2ActionPerformed

    private void stanga1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stanga1ActionPerformed
        // TODO add your handling code here:
        buttonSetup(dreapta1, stanga1);

    }//GEN-LAST:event_stanga1ActionPerformed

    public void singletonSound(Boolean sv) {
        String text = "";
        if (sv == true) {
            text = "ON";
        } else {
            text = "OFF";
        }

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("graphics/imagini/sound" + String.valueOf(text) + ".png"));
        } catch (IOException e) {
        }
        try {
            if (Singleton.getInstance().getSoundValue() == false) {
                Singleton.getInstance().bgMusic.stop();
            } else {
                Singleton.getInstance().bgMusic.setFile("graphics/sounds/TremLoadingloopl.wav");
                Singleton.getInstance().bgMusic.play(true);

            }
        } catch (IOException ex) {
            Logger.getLogger(MainFrame.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        soundBtn.setIcon(new ImageIcon(img));
        soundBtn1.setIcon(new ImageIcon(img));
        soundBtn2.setIcon(new ImageIcon(img));
        soundBtn3.setIcon(new ImageIcon(img));

    }


    private void soundBtn2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundBtn2ActionPerformed
        // TODO add your handling code here:
        Singleton.getInstance().setSoundValue(!Singleton.getInstance().getSoundValue());
        singletonSound(Singleton.getInstance().getSoundValue());
        buttonSound();
    }//GEN-LAST:event_soundBtn2ActionPerformed

    private void soundBtn1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundBtn1ActionPerformed
        // TODO add your handling code here:
        Singleton.getInstance().setSoundValue(!Singleton.getInstance().getSoundValue());
        singletonSound(Singleton.getInstance().getSoundValue());
        buttonSound();
    }//GEN-LAST:event_soundBtn1ActionPerformed

    private void soundBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundBtnActionPerformed
        // TODO add your handling code here:
        Singleton.getInstance().setSoundValue(!Singleton.getInstance().getSoundValue());
        singletonSound(Singleton.getInstance().getSoundValue());
        buttonSound();


    }//GEN-LAST:event_soundBtnActionPerformed

    private void soundBtn3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soundBtn3ActionPerformed
        // TODO add your handling code here:
        Singleton.getInstance().setSoundValue(!Singleton.getInstance().getSoundValue());
        singletonSound(Singleton.getInstance().getSoundValue());
        buttonSound();
    }//GEN-LAST:event_soundBtn3ActionPerformed

    private void storeHintsPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_storeHintsPropertyChange
        // TODO add your handling code here:
        storeHints.setText("Current Hints: " + String.valueOf(Singleton.getInstance().getHints()));
    }//GEN-LAST:event_storeHintsPropertyChange

    public Boolean canHint() {
        if (Singleton.getInstance().getHints() > 0) {

            return true;
        }

        Music bs = new Music();
        bs.setFile("graphics/sounds/windows-error-sound-effect-35894.wav");
        bs.play(false);
        return false;
    }

    public void Hint(JButton stanga, JButton dreapta) {

        try {
            Singleton.getInstance().setHints(Singleton.getInstance().getHints() - 1);
            Singleton.getInstance().setGasitValue(Singleton.getInstance().getGasitValue() + 1);
            hintsSetup();
            GameWon(1);
            buttonSound();

            ContinuousMouseMovementCommand moveCommand = new ContinuousMouseMovementCommand((int) stanga.getLocationOnScreen().getX() + 15, (int) stanga.getLocationOnScreen().getY() + 15, 7);
            invoker.executeCommand(moveCommand);

            stanga.setBorderPainted(true);
            stanga.setEnabled(false);
            Thread.sleep(1000);
            ContinuousMouseMovementCommand moveCommand2 = new ContinuousMouseMovementCommand((int) dreapta.getLocationOnScreen().getX() + 15, (int) dreapta.getLocationOnScreen().getY() + 15, 7);
            invoker.executeCommand(moveCommand2);

            dreapta.setBorderPainted(true);
            dreapta.setEnabled(false);
        } catch (InterruptedException ex) {
            Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    private void useHint1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useHint1ActionPerformed
        // TODO add your handling code here:

        if (canHint()) {
            if (!stanga1.isBorderPainted() || !dreapta1.isBorderPainted()) {

                Hint(stanga1, dreapta1);

            } else if (!stanga2.isBorderPainted() || !dreapta2.isBorderPainted()) {

                Hint(stanga2, dreapta2);
            } else if (!stanga3.isBorderPainted() || !dreapta3.isBorderPainted()) {

                Hint(stanga3, dreapta3);
            } else if (!stanga4.isBorderPainted() || !dreapta4.isBorderPainted()) {

                Hint(stanga4, dreapta4);
            } else if (!stanga5.isBorderPainted() || !dreapta5.isBorderPainted()) {

                Hint(stanga5, dreapta5);
            } else if (!stanga6.isBorderPainted() || !dreapta6.isBorderPainted()) {

                Hint(stanga6, dreapta6);
            } else if (!stanga7.isBorderPainted() || !dreapta7.isBorderPainted()) {

                Hint(stanga7, dreapta7);
            } else {

                if (GameWon(1)) {
                    win();
                }
            }
        } else if (GameWon(1)) {
            win();
        }

    }//GEN-LAST:event_useHint1ActionPerformed

    private void hintsLevel1PropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_hintsLevel1PropertyChange
        // TODO add your handling code here:
    }//GEN-LAST:event_hintsLevel1PropertyChange

    private void hints10BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hints10BtnActionPerformed
        // TODO add your handling code here:
        if (hints10.getBackground().equals(Color.white)) {
            hints10.setBackground(Color.blue);
            cart.addItem(hint10);
        } else {
            hints10.setBackground(Color.white);
            cart.removeItem(hint10);
        }
        buttonSound();

    }//GEN-LAST:event_hints10BtnActionPerformed

    private void hints15BtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hints15BtnActionPerformed
        // TODO add your handling code here:
        if (hints15.getBackground().equals(Color.white)) {
            hints15.setBackground(Color.blue);
            cart.addItem(hint15);
        } else {
            hints15.setBackground(Color.white);
            cart.removeItem(hint15);
        }
        buttonSound();
    }//GEN-LAST:event_hints15BtnActionPerformed

    private void anuleazaPPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anuleazaPPActionPerformed
        // TODO add your handling code here:
        Menu.setVisible(true);
        cart.removeItem(hint5);
        cart.removeItem(hint15);
        cart.removeItem(hint10);
        Cart.setVisible(false);
        email.setText("email");
        password.setText("parola");
        hints5.setBackground(Color.white);
        hints10.setBackground(Color.white);
        hints15.setBackground(Color.white);
        buttonSound();

    }//GEN-LAST:event_anuleazaPPActionPerformed

    JPasswordField1 passwordField = new JPasswordField1("");
    JInputField1 inputField = new JInputField1("");
    JPasswordFieldMemento passwordMemento = passwordField.createMemento();
    JInputFieldMemento inputMemento = inputField.createMemento();
    Boolean memento = false;

    public void memento() {

        passwordField.setText(password.getText());
        inputField.setText(email.getText());

        // Create a memento for the current state of the fields
        passwordMemento = passwordField.createMemento();
        inputMemento = inputField.createMemento();

        // Restore the fields to their previous state using the mementos
    }


    private void confirmaPPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmaPPActionPerformed
        // TODO add your handling code here:
        confirma(anuleazaPP, confirmaPP, STPP, "Pay Pal");
        //valideaza():
        memento();
        memento = true;
        buttonSound();

    }//GEN-LAST:event_confirmaPPActionPerformed


    private void confirmaCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmaCCActionPerformed
        // TODO add your handling code here:
        confirma(anuleazaCC, confirmaCC, STCC, "Cardul de Credit");
        //valideaza();

        buttonSound();
    }//GEN-LAST:event_confirmaCCActionPerformed

    private void anuleazaCCActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_anuleazaCCActionPerformed
        // TODO add your handling code here:
        Menu.setVisible(true);
        cart.removeItem(hint5);
        cart.removeItem(hint15);
        cart.removeItem(hint10);
        Cart.setVisible(false);
        hints5.setBackground(Color.white);
        hints10.setBackground(Color.white);
        hints15.setBackground(Color.white);
        buttonSound();
    }//GEN-LAST:event_anuleazaCCActionPerformed

    private void playBtnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playBtnMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_playBtnMouseClicked

    private void playBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playBtnMouseEntered

        imageSetup(playBtn, "graphics/imagini/UI/PNG/blue_button01.png", 240, 70);

    }//GEN-LAST:event_playBtnMouseEntered

    private void playBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_playBtnMouseExited

        imageSetup(playBtn, "graphics/imagini/UI/PNG/blue_button04.png", 240, 70);
    }//GEN-LAST:event_playBtnMouseExited

    private void storeBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_storeBtnMouseEntered

        imageSetup(storeBtn, "graphics/imagini/UI/PNG/yellow_button03.png", 220, 60);

    }//GEN-LAST:event_storeBtnMouseEntered


    private void storeBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_storeBtnMouseExited

        imageSetup(storeBtn, "graphics/imagini/UI/PNG/yellow_button00.png", 220, 60);

    }//GEN-LAST:event_storeBtnMouseExited

    private void quitBtnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitBtnMouseExited
        imageSetup(quitBtn, "graphics/imagini/UI/PNG/red_button01.png", 180, 60);
    }//GEN-LAST:event_quitBtnMouseExited

    private void quitBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_quitBtnMouseEntered
        imageSetup(quitBtn, "graphics/imagini/UI/PNG/red_button02.png", 180, 60);
    }//GEN-LAST:event_quitBtnMouseEntered

    private void numeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_numeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_numeActionPerformed

    private void hints5MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hints5MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_hints5MouseEntered

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        System.exit(1);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        Menu.setVisible(true);
        Won.setVisible(false);
        resetLevel();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        Levels.setVisible(true);
        Won.setVisible(false);
        resetLevel();

    }//GEN-LAST:event_jButton1ActionPerformed
    public void hints5BtnmouseEntered(MouseEvent e) {
        //
        hints5Btn.setBackground(Color.red);
    }

    public void hints5BtnmouseExited(MouseEvent e) {
        //
        hints5Btn.setBackground(Color.green);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainFrame.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        }
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            try {
                
                new MainFrame().setVisible(true);
                
                //adapter
                QButton quitButton = new QuitButton();
                KeyboardAdapter adapter = new KeyboardAdapter(quitButton, KeyEvent.VK_Q);
                adapter.listenForKeyPress();
                //end
                
            } catch (IOException ex) {
                Logger.getLogger(MainFrame.class
                        .getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Cart;
    private javax.swing.JPanel CreditCardPanel;
    private javax.swing.JLabel Jlabelnivel2;
    private javax.swing.JPanel Levels;
    private javax.swing.JPanel Menu;
    private javax.swing.JPanel Nivel1;
    private javax.swing.JPanel Nivel2;
    private javax.swing.JPanel Nivel3;
    private javax.swing.JPanel OutOfTime;
    private javax.swing.JPanel PayPalPanel;
    private javax.swing.JLabel STCC;
    private javax.swing.JLabel STPP;
    private javax.swing.JPanel Store;
    private javax.swing.JPanel Won;
    private javax.swing.JButton anuleazaCC;
    private javax.swing.JButton anuleazaPP;
    private javax.swing.JButton backBtn;
    private javax.swing.JButton backToStoreBtn;
    private javax.swing.JButton backbtn;
    private javax.swing.JTextField cardNumber;
    private javax.swing.JButton cartBtn;
    private javax.swing.JButton confirmaCC;
    private javax.swing.JButton confirmaPP;
    private javax.swing.JTextField cvv;
    private javax.swing.JLabel dePlatit;
    private javax.swing.JButton dreapta1;
    private javax.swing.JButton dreapta2;
    private javax.swing.JButton dreapta3;
    private javax.swing.JButton dreapta4;
    private javax.swing.JButton dreapta5;
    private javax.swing.JButton dreapta6;
    private javax.swing.JButton dreapta7;
    private javax.swing.JTextField email;
    private javax.swing.JTextField expDate;
    private javax.swing.Box.Filler filler1;
    private javax.swing.JLabel gasit;
    private javax.swing.JButton giveup1Btn;
    private javax.swing.JButton giveup2Btn;
    private javax.swing.JButton giveup3Btn;
    private javax.swing.JPanel hints10;
    private javax.swing.JButton hints10Btn;
    private javax.swing.JPanel hints15;
    private javax.swing.JButton hints15Btn;
    private javax.swing.JPanel hints5;
    private javax.swing.JButton hints5Btn;
    private javax.swing.JLabel hintsLevel1;
    private javax.swing.JLabel hintsLevels;
    private javax.swing.JLabel imgCC;
    private javax.swing.JLabel imgLvl1;
    private javax.swing.JLabel imgLvl2;
    private javax.swing.JLabel imgLvl3;
    private javax.swing.JLabel imgPP;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLayeredPane jLayeredPane1;
    private javax.swing.JLayeredPane jLayeredPane2;
    private javax.swing.JLayeredPane jLayeredPane3;
    private javax.swing.JLayeredPane jLayeredPane4;
    private javax.swing.JLayeredPane jLayeredPane5;
    private javax.swing.JLayeredPane jLayeredPane6;
    private javax.swing.JLayeredPane jLayeredPane7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel labelForPayment;
    private javax.swing.JButton nivel1Btn;
    private javax.swing.JButton nivel2Btn;
    private javax.swing.JButton nivel3Btn;
    private javax.swing.JTextField nume;
    private javax.swing.JPasswordField password;
    private javax.swing.JComboBox<String> paymentCombo;
    private javax.swing.JButton playBtn;
    private javax.swing.JButton quitBtn;
    private javax.swing.JLabel redirect;
    private javax.swing.JButton soundBtn;
    private javax.swing.JButton soundBtn1;
    private javax.swing.JButton soundBtn2;
    private javax.swing.JButton soundBtn3;
    private javax.swing.JButton stanga1;
    private javax.swing.JButton stanga2;
    private javax.swing.JButton stanga3;
    private javax.swing.JButton stanga4;
    private javax.swing.JButton stanga5;
    private javax.swing.JButton stanga6;
    private javax.swing.JButton stanga7;
    private javax.swing.JButton storeBtn;
    private javax.swing.JLabel storeHints;
    private javax.swing.JLabel timeLeft1;
    private javax.swing.JLabel timeLeft2;
    private javax.swing.JLabel timeLeft3;
    private javax.swing.JButton useHint1;
    // End of variables declaration//GEN-END:variables

    private class ActionListenerImpl implements ActionListener {

        public ActionListenerImpl() {
        }

        public void actionPerformed(java.awt.event.ActionEvent evt) {
            hints5BtnActionPerformed(evt);
        }
    }
}
