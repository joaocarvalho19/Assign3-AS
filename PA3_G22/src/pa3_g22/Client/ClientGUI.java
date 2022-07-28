/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pa3_g22.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import pa3_g22.Communication.SClient;
import pa3_g22.Communication.Message;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author joaoc
 */
public class ClientGUI extends javax.swing.JFrame {
    
    // Client ID
    private final long clientId;
    
    private String LBhost = null;
    
    private int LBport = 0;
    
    private SClient socketClient;
    
    private Client client;
    
    private int increment;
    
    /**
     * Creates new form ClientGUI
     */
    public ClientGUI() {
        initComponents();
        this.clientId = ProcessHandle.current().pid();
        title.setText("Client: "+clientId);
        increment = 0;
    }
    
    public void appendRequest(long requestId, int num_iterations, int deadline){
        DefaultTableModel model = (DefaultTableModel) requestsTable.getModel();
        model.addRow(new Object[]{requestId, num_iterations,deadline});
        int curr_num_req = Integer.parseInt(numberReq.getText());
        numberReq.setText(String.valueOf(curr_num_req + 1));
    }
    
    public void appendReplay(long requestId, int num_iterations, int deadline, String pi_value){
        DefaultTableModel model = (DefaultTableModel) repliesTable.getModel();
        model.addRow(new Object[]{requestId, num_iterations, deadline, pi_value});
        int curr_num_rep = Integer.parseInt(numberRep.getText());
        numberRep.setText(String.valueOf(curr_num_rep + 1));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jPanel1 = new javax.swing.JPanel();
        LBHost = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        LBPort = new javax.swing.JTextField();
        clientStart = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel3 = new javax.swing.JLabel();
        numberIter = new javax.swing.JSpinner();
        jSeparator2 = new javax.swing.JSeparator();
        newRequest = new javax.swing.JButton();
        tabbledPlane = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        requestsTable = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        repliesTable = new javax.swing.JTable();
        title = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        deadlineSlide = new javax.swing.JSlider();
        slideValue = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        numberReq = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        numberRep = new javax.swing.JLabel();

        jMenu1.setText("jMenu1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        LBHost.setBackground(new java.awt.Color(255, 255, 255));
        LBHost.setForeground(new java.awt.Color(0, 0, 0));
        LBHost.setText("localhost");
        LBHost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LBHostActionPerformed(evt);
            }
        });

        jLabel1.setBackground(new java.awt.Color(51, 51, 51));
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("LB Host:");

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("LB Port:");

        LBPort.setBackground(new java.awt.Color(255, 255, 255));
        LBPort.setForeground(new java.awt.Color(0, 0, 0));
        LBPort.setText("1000");
        LBPort.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LBPortActionPerformed(evt);
            }
        });

        clientStart.setBackground(new java.awt.Color(0, 0, 0));
        clientStart.setForeground(new java.awt.Color(204, 204, 204));
        clientStart.setText("START CLIENT");
        clientStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientStartActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Number of Iterations:");

        numberIter.setEnabled(false);

        newRequest.setBackground(new java.awt.Color(0, 0, 0));
        newRequest.setForeground(new java.awt.Color(204, 204, 204));
        newRequest.setText("NEW REQUEST");
        newRequest.setEnabled(false);
        newRequest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newRequestActionPerformed(evt);
            }
        });

        tabbledPlane.setBackground(new java.awt.Color(255, 255, 255));
        tabbledPlane.setForeground(new java.awt.Color(51, 51, 51));

        requestsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Req ID", "Nº Iterations", "Deadline"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(requestsTable);

        tabbledPlane.addTab("Requests", jScrollPane1);

        repliesTable.setForeground(new java.awt.Color(0, 0, 0));
        repliesTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Req ID", "Nº Iterations", "Deadline", "Value"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(repliesTable);

        tabbledPlane.addTab("Replies", jScrollPane2);

        title.setForeground(new java.awt.Color(0, 0, 0));
        title.setText("Client");

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("Deadline:");

        deadlineSlide.setBackground(new java.awt.Color(0, 0, 0));
        deadlineSlide.setForeground(new java.awt.Color(255, 255, 255));
        deadlineSlide.setEnabled(false);
        deadlineSlide.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                deadlineSlideStateChanged(evt);
            }
        });

        slideValue.setForeground(new java.awt.Color(0, 0, 0));
        slideValue.setText("50 sec");

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Nº Requests: ");

        numberReq.setForeground(new java.awt.Color(0, 0, 0));
        numberReq.setText("0");

        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Nº Replies: ");

        numberRep.setForeground(new java.awt.Color(0, 0, 0));
        numberRep.setText("0");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LBHost, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LBPort, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                        .addComponent(clientStart))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator3))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(deadlineSlide, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(slideValue, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(6, 6, 6))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(numberIter, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(newRequest, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(178, 178, 178)
                        .addComponent(title)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(tabbledPlane, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numberReq)
                .addGap(81, 81, 81)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(numberRep)
                .addGap(93, 93, 93))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addComponent(title)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LBHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(LBPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clientStart))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(tabbledPlane, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(numberReq)
                    .addComponent(jLabel8)
                    .addComponent(numberRep))
                .addGap(4, 4, 4)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addGap(5, 5, 5))
                            .addComponent(deadlineSlide, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(7, 7, 7)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(numberIter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(slideValue)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(newRequest)))
                .addGap(77, 77, 77))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 384, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void LBHostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LBHostActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LBHostActionPerformed

    private void LBPortActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LBPortActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LBPortActionPerformed

    private void clientStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientStartActionPerformed
        LBhost = LBHost.getText();
        LBport = Integer.parseInt(LBPort.getText());
        System.out.println("Starting Client");
        
        try {
            //socketClient = new SClient(LBhost, LBport);
            socketClient = new SClient(new Socket(LBhost, LBport), LBhost, LBport);
            socketClient.createSocket();
            socketClient.writeObject(new Message("NEW_CLIENT", clientId));
            //socketClient.end();
        } catch (IOException ex) {
            System.err.println(ex);
        }

        this.client = new Client(clientId, socketClient, this);
        this.client.start();
        
        clientStart.setEnabled(false);
        deadlineSlide.setEnabled(true);
        numberIter.setEnabled(true);
        newRequest.setEnabled(true);
    }//GEN-LAST:event_clientStartActionPerformed

    private void newRequestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newRequestActionPerformed
        int NoIter = (Integer) numberIter.getValue();
        long reqId = 1000*(clientId) + increment;
        int deadline_value = (Integer) deadlineSlide.getValue();
        //socketClient = new SClient(new Socket(LBhost, LBport));
        
        //socketClient.createSocket();
        socketClient.writeObject(new Message("REQ",clientId, reqId, "01", NoIter, deadline_value));
        //socketClient.end();
        //socketClient.writeObject(new Message("REQ",clientId, reqId, "01", NoIter, deadline_value));
        
        increment++;
        
        // Append tyo interface
        appendRequest(reqId, NoIter, deadline_value);
    }//GEN-LAST:event_newRequestActionPerformed

    private void deadlineSlideStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_deadlineSlideStateChanged
        slideValue.setText(String.valueOf(deadlineSlide.getValue())+" sec");
    }//GEN-LAST:event_deadlineSlideStateChanged

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ClientGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClientGUI().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField LBHost;
    private javax.swing.JTextField LBPort;
    private javax.swing.JButton clientStart;
    private javax.swing.JSlider deadlineSlide;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JButton newRequest;
    private javax.swing.JSpinner numberIter;
    private javax.swing.JLabel numberRep;
    private javax.swing.JLabel numberReq;
    private javax.swing.JTable repliesTable;
    private javax.swing.JTable requestsTable;
    private javax.swing.JLabel slideValue;
    private javax.swing.JTabbedPane tabbledPlane;
    private javax.swing.JLabel title;
    // End of variables declaration//GEN-END:variables
}
