package idcardgeneratingsystem;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.PrintJob;
import java.awt.Toolkit;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

public class Home extends javax.swing.JFrame {
    // Variables declaration
    private javax.swing.JButton btnSelectImage;
    private javax.swing.JButton btnGenerateID;
    private javax.swing.JButton btnPrintID;
    private javax.swing.JButton btnStoreData; // New button for storing data
    private javax.swing.JComboBox<String> cmbGender;
    private javax.swing.JLabel lblImage;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCountry;
    private javax.swing.JTextField txtBirthday; // TextField for Birthday
    private javax.swing.JTextField txtIssueDate; // TextField for Issue Date
    private IDCardPanel cardPanel; // Panel to display the ID card

    private ImageIcon format = null;
    private String fname = null;
    private byte[] pimage = null;
    private int lvalue = 1000;

    public Home() {
        initComponents(); // Initialize components
    }

    // Custom JPanel for ID Card display
    public class IDCardPanel extends JPanel {
        private String name;
        private String address;
        private String country;
        private String gender;
        private String birthday;
        private String nic;
        private Image image;

        public IDCardPanel(String name, String address, String country, String gender, String birthday, String nic, Image image) {
            this.name = name;
            this.address = address;
            this.country = country;
            this.gender = gender;
            this.birthday = birthday;
            this.nic = nic;
            this.image = image;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Set background
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());
            // Draw a border
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, getWidth() - 20, getHeight() - 20);
            // Set font and positions for text
            g.setFont(new Font("Arial", Font.BOLD, 14));
            if (image != null) {
                g.drawImage(image, 20, 30, 100, 100, this);
            }
            g.drawString("Name: " + name, 150, 50);
            g.drawString("Address: " + address, 150, 80);
            g.drawString("Country: " + country, 150, 110);
            g.drawString("Gender: " + gender, 150, 140);
            g.drawString("Birthday: " + birthday, 150, 170);
            g.drawString("NIC: " + nic, 150, 200);
        }
    }

    // Button action to select an image
    private void btnSelectImageActionPerformed(java.awt.event.ActionEvent evt) {
        JFileChooser fchoser = new JFileChooser();
        fchoser.showOpenDialog(null);
        File f = fchoser.getSelectedFile();
        fname = f.getAbsolutePath();
        ImageIcon micon = new ImageIcon(fname);
        try {
            File image = new File(fname);
            FileInputStream fis = new FileInputStream(image);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            for (int readnum; (readnum = fis.read(buf)) != -1;) {
                baos.write(buf, 0, readnum);
            }
            pimage = baos.toByteArray();
            lblImage.setIcon(resizeImage(fname, buf));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button action to generate ID card
    private void btnGenerateIDActionPerformed(java.awt.event.ActionEvent evt) {
        String name = txtName.getText();
        String address = txtAddress.getText();
        String country = txtCountry.getText();
        String gender = cmbGender.getSelectedItem().toString();
        String birthday = txtBirthday.getText(); // Get text from birthday field
        // ID number generation
        String year = birthday.substring(2, 4);
        int genvalue = (gender.equals("Male")) ? 100 : 500;
        lvalue++;
        String nic = year + genvalue + lvalue + "V";
        // Create the image object from the selected image (pimage is byte array)
        Image img = Toolkit.getDefaultToolkit().createImage(pimage);
        // Create and display ID card panel
        cardPanel = new IDCardPanel(name, address, country, gender, birthday, nic, img);
        cardPanel.setBounds(50, 350, 400, 300);
        getContentPane().add(cardPanel);
        cardPanel.repaint();
    }

    // Button action to print the ID card
    private void btnPrintIDActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            Toolkit tkp = cardPanel.getToolkit();
            PrintJob pjp = tkp.getPrintJob(this, "Print ID Card", null);
            Graphics g = pjp.getGraphics();
            cardPanel.print(g);
            g.dispose();
            pjp.end();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Button action to store data
    private void btnStoreDataActionPerformed(java.awt.event.ActionEvent evt) {
        String name = txtName.getText();
        String address = txtAddress.getText();
        String country = txtCountry.getText();
        String gender = cmbGender.getSelectedItem().toString();
        String birthday = txtBirthday.getText();
        String nic = generateRandomNumber(); // Generate a random number for access
        // Store image details (you can extend this logic to save to a file/database)
        System.out.println("Stored Data:");
        System.out.println("Name: " + name);
        System.out.println("Address: " + address);
        System.out.println("Country: " + country);
        System.out.println("Gender: " + gender);
        System.out.println("Birthday: " + birthday);
        System.out.println("NIC: " + nic);
        System.out.println("Access Number: " + generateRandomNumber());
    }

    // Generate a random number for access
    private String generateRandomNumber() {
        Random rand = new Random();
        int randomNumber = rand.nextInt(100000); // Generates a random number between 0 and 99999
        return String.format("%05d", randomNumber); // Format to 5 digits
    }

    // Method to resize image
    public ImageIcon resizeImage(String imagePath, byte[] pic) {
        ImageIcon myImage = null;
        if (imagePath != null) {
            myImage = new ImageIcon(imagePath);
        } else {
            myImage = new ImageIcon(pic);
        }
        Image img = myImage.getImage();
        Image img2 = img.getScaledInstance(lblImage.getHeight(), lblImage.getWidth(), Image.SCALE_SMOOTH);
        ImageIcon image = new ImageIcon(img2);
        return image;
    }

    // Initialize GUI components
    private void initComponents() {
        setTitle("ID Card Generating System");
        setLayout(null);
        setSize(500, 700);

        javax.swing.JLabel lblName = new javax.swing.JLabel("Name:");
        lblName.setBounds(30, 30, 80, 25);
        add(lblName);

        txtName = new javax.swing.JTextField();
        txtName.setBounds(120, 30, 150, 25);
        add(txtName);

        javax.swing.JLabel lblAddress = new javax.swing.JLabel("Address:");
        lblAddress.setBounds(30, 70, 80, 25);
        add(lblAddress);

        txtAddress = new javax.swing.JTextField();
        txtAddress.setBounds(120, 70, 150, 25);
        add(txtAddress);

        javax.swing.JLabel lblCountry = new javax.swing.JLabel("Country:");
        lblCountry.setBounds(30, 110, 80, 25);
        add(lblCountry);

        txtCountry = new javax.swing.JTextField();
        txtCountry.setBounds(120, 110, 150, 25);
        add(txtCountry);

        javax.swing.JLabel lblGender = new javax.swing.JLabel("Gender:");
        lblGender.setBounds(30, 150, 80, 25);
        add(lblGender);

        cmbGender = new javax.swing.JComboBox<>(new String[]{"Male", "Female"});
        cmbGender.setBounds(120, 150, 150, 25);
        add(cmbGender);

        javax.swing.JLabel lblBirthday = new javax.swing.JLabel("Birthday:");
        lblBirthday.setBounds(30, 190, 80, 25);
        add(lblBirthday);

        txtBirthday = new javax.swing.JTextField();
        txtBirthday.setBounds(120, 190, 150, 25);
        add(txtBirthday);

        lblImage = new javax.swing.JLabel();
        lblImage.setBounds(30, 230, 150, 150);
        add(lblImage);

        btnSelectImage = new javax.swing.JButton("Select Image");
        btnSelectImage.setBounds(200, 230, 120, 25);
        btnSelectImage.addActionListener(this::btnSelectImageActionPerformed);
        add(btnSelectImage);

        btnGenerateID = new javax.swing.JButton("Generate ID");
        btnGenerateID.setBounds(30, 400, 120, 25);
        btnGenerateID.addActionListener(this::btnGenerateIDActionPerformed);
        add(btnGenerateID);

        btnPrintID = new javax.swing.JButton("Print ID");
        btnPrintID.setBounds(200, 400, 120, 25);
        btnPrintID.addActionListener(this::btnPrintIDActionPerformed);
        add(btnPrintID);

        btnStoreData = new javax.swing.JButton("Store Data");
        btnStoreData.setBounds(120, 450, 120, 25);
        btnStoreData.addActionListener(this::btnStoreDataActionPerformed);
        add(btnStoreData);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Home().setVisible(true));
    }
}