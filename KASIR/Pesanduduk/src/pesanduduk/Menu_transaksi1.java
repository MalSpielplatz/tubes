/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pesanduduk;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import static sun.net.www.MimeTable.loadTable;

/**
 *
 * @author mohammadikmal
 */
public class Menu_transaksi1 extends javax.swing.JFrame {
    private DefaultTableModel model = null;
    private PreparedStatement stat;
    private ResultSet rs;
    Koneksi k = new Koneksi();
    private int totalHarga = 0;

    /**
     * Creates new form Menu_masakan
     */
    public Menu_transaksi1() {
        initComponents();
        k.getKoneksi();
        refreshTable();
        refreshCombo();
        
        
    }
    
    class transaksi extends Menu_transaksi1{
        int id_transaksi, id_masakan, kode_meja, harga_meja, jml_kursi, harga, jumlah_beli, total_bayar;
        String nama_pelanggan, tanggal, nama_masakan;
        int totalHarga = 0;
        public transaksi() {
            this.nama_pelanggan = text_nama_pelanggan.getText();
            String combo = combo_id_masakan.getSelectedItem().toString();
            String combo1 = combo_id_meja.getSelectedItem().toString();
            String[] arr = combo.split(":");
            String[] meja = combo1.split(":");
            this.id_masakan = Integer.parseInt(arr[0]);
            this.kode_meja = Integer.parseInt(meja[0]);
            try {
                Date date = text_tanggal.getDate();
                DateFormat  dateformat = new SimpleDateFormat("YYYY-MM-dd");
                this.tanggal = dateformat.format(date);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
            this.nama_masakan = arr[1];
            this.harga = Integer.parseInt(arr[2]);
            this.jml_kursi = Integer.parseInt(meja[1]);
            this.harga_meja = Integer.parseInt(meja[2]);
            
            this.total_bayar = (this.harga * this.jumlah_beli) + this.harga_meja;
        }
        
        
    }
    private void updateTotalHarga() {
        text_total_bayar.setText("Rp." + totalHarga);
        
        
    }
    
    public void refreshTable(){
        model = new DefaultTableModel();
        model.addColumn("ID Tranaksi");
        model.addColumn("Nama Pelanggan");
        model.addColumn("ID Masakan");
        model.addColumn("Kode Meja");
        model.addColumn("Jumlah Kursi");
        model.addColumn("Tanggal");
        model.addColumn("Nama Masakan");
        model.addColumn("Harga");
        model.addColumn("Harga Meja");
        model.addColumn("Jumlah Beli");
        model.addColumn("Total Bayar");
        
        table_transaksi.setModel(model);
        try {
            this.stat = k.getKoneksi().prepareStatement("SELECT * FROM transaksi");
            this.rs = this.stat.executeQuery();
            while(rs.next()){
                Object[] data ={
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getString(10),
                    rs.getString(11)
                    
                };
                model.addRow(data);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        text_id_transaksi.setText("");
        text_nama_pelanggan.setText("");
        
        text_total_bayar.setText("");
        
    }
    private void setHargaBasedOnMenu() {
        String menuTerpilih = (String) combo_id_masakan.getSelectedItem();

        try {
            String query = "SELECT harga FROM masakan WHERE nama_masakan = ?";
            PreparedStatement ps = k.getKoneksi().prepareStatement(query);
            ps.setString(1, menuTerpilih);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int harga = rs.getInt("harga");
                label_harga.setText("Rp."+ String.valueOf(harga));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void setHargaBasedOnMeja(){
        String mejaTerpilih = (String) combo_id_meja.getSelectedItem();

        try {
            String query = "SELECT harga_meja FROM meja WHERE kode_meja = ?";
            PreparedStatement ps = k.getKoneksi().prepareStatement(query);
            ps.setString(1, mejaTerpilih);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int harga = rs.getInt("harga_meja");
                text_hrg_meja.setText("Rp."+ String.valueOf(harga));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void setKursiBasedOnMeja(){
        String mejaTerpilih = (String) combo_id_meja.getSelectedItem();

        try {
            String query = "SELECT jml_kursi FROM meja WHERE kode_meja = ?";
            PreparedStatement ps = k.getKoneksi().prepareStatement(query);
            ps.setString(1, mejaTerpilih);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int kursi = rs.getInt("jml_kursi");
                text_jml_kursi.setText(String.valueOf(kursi));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void tambahItemKeList() {
        String menuTerpilih = (String) combo_id_masakan.getSelectedItem();
        String hargaMenu = label_harga.getText();
        int kuantitas = (int) spinner_kuantitas.getValue();
        if (kuantitas == 0) {
            JOptionPane.showMessageDialog(this, "Kuantitas tidak boleh 0", "Kesalahan", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int hargaPerItem = Integer.parseInt(label_harga.getText());
        int subtotal = hargaPerItem * kuantitas;
        totalHarga += subtotal;
        String item = menuTerpilih + ", " + " (" + kuantitas + ")\n";
        textArea_pesanan.append(item);
    }
    
//    private void updateTotalHargaDanDP() {
//        label_totalHarga.setText("Rp" + totalHarga);
//        int totalDP = totalHarga / 2;
//        label_totalDP.setText("Rp" + totalDP);
//    }
    
    public void refreshCombo(){
        try {
            this.stat = k.getKoneksi().prepareStatement("SELECT * FROM masakan WHERE status = 'Tersedia'");
            this.rs = this.stat.executeQuery();
            while (rs.next()) {                
                combo_id_masakan.addItem(rs.getString("nama_masakan"));
                                      
            }
                   
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
        
        try {
            this.stat = k.getKoneksi().prepareStatement("SELECT * FROM meja WHERE status_meja = 'Tersedia'");
            this.rs = this.stat.executeQuery();
            while (rs.next()) {                
                combo_id_meja.addItem(rs.getString("kode_meja"));
            }
        
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        text_id_transaksi = new javax.swing.JTextField();
        text_nama_pelanggan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        combo_id_masakan = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        table_transaksi = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        btn_input = new javax.swing.JButton();
        btn_update = new javax.swing.JButton();
        btn_delete = new javax.swing.JButton();
        btn_cetak_laporan = new javax.swing.JButton();
        btn_logout = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        text_total_bayar = new javax.swing.JTextField();
        btn_menu_masakan = new javax.swing.JButton();
        text_tanggal = new com.toedter.calendar.JDateChooser();
        btn_menu_meja = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        combo_id_meja = new javax.swing.JComboBox<>();
        text_hrg_meja = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        text_jml_kursi = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        btn_pesan = new javax.swing.JButton();
        btn_reset = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea_pesanan = new javax.swing.JTextArea();
        spinner_kuantitas = new javax.swing.JSpinner();
        jLabel15 = new javax.swing.JLabel();
        label_harga = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 0, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Menu Transaksi");

        jLabel2.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel2.setText("ID Transaksi");

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel3.setText("Nama Pelanggan");

        text_id_transaksi.setEnabled(false);

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel5.setText("Pilih Makanan");

        combo_id_masakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_id_masakanActionPerformed(evt);
            }
        });

        table_transaksi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table_transaksi.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_transaksiMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table_transaksi);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        btn_input.setText("INPUT");
        btn_input.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_inputActionPerformed(evt);
            }
        });

        btn_update.setText("UPDATE");
        btn_update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_updateActionPerformed(evt);
            }
        });

        btn_delete.setText("DELETE");
        btn_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_deleteActionPerformed(evt);
            }
        });

        btn_cetak_laporan.setText("Cetak Laporan");
        btn_cetak_laporan.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_input)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_update)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_delete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_cetak_laporan)
                .addGap(65, 65, 65))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_input)
                    .addComponent(btn_update)
                    .addComponent(btn_delete)
                    .addComponent(btn_cetak_laporan))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btn_logout.setText("LOGOUT");
        btn_logout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_logoutActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel7.setText("Tanggal");

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel9.setText("Total bayar");

        text_total_bayar.setEnabled(false);

        btn_menu_masakan.setText("Lihat Menu");
        btn_menu_masakan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_menu_masakanActionPerformed(evt);
            }
        });

        btn_menu_meja.setText("Lihat Meja");
        btn_menu_meja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_menu_mejaActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel6.setText("Harga");

        combo_id_meja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combo_id_mejaActionPerformed(evt);
            }
        });

        text_hrg_meja.setEnabled(false);
        text_hrg_meja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_hrg_mejaActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel10.setText("Harga Meja");

        text_jml_kursi.setEnabled(false);
        text_jml_kursi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                text_jml_kursiActionPerformed(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel11.setText("Kursi");

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel12.setText("List Pesanan");

        btn_pesan.setText("Pesan");
        btn_pesan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_pesanActionPerformed(evt);
            }
        });

        btn_reset.setText("RESET");

        textArea_pesanan.setColumns(20);
        textArea_pesanan.setRows(5);
        jScrollPane2.setViewportView(textArea_pesanan);

        jLabel15.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel15.setText("Kuantitas");

        label_harga.setText("Rp.0");

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 1, 18)); // NOI18N
        jLabel8.setText("Pilih Meja");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btn_logout))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel7))
                                .addGap(10, 10, 10)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(text_total_bayar)
                                    .addComponent(text_tanggal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(jScrollPane1)
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(2, 2, 2)
                                        .addComponent(jLabel15)
                                        .addGap(81, 81, 81)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(spinner_kuantitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addComponent(text_nama_pelanggan)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(combo_id_masakan, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabel6)
                                                .addGap(61, 61, 61)
                                                .addComponent(label_harga, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                                                .addGap(10, 10, 10)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btn_menu_masakan)
                                        .addGap(38, 38, 38))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel2)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel5))
                                        .addGap(6, 6, 6)
                                        .addComponent(text_id_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 368, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel8)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btn_menu_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12))
                                .addGap(15, 15, 15))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabel10))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(23, 23, 23)
                                .addComponent(jLabel11)))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(text_jml_kursi)
                            .addComponent(text_hrg_meja)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(combo_id_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(184, 184, 184)
                                        .addComponent(btn_pesan)
                                        .addGap(42, 42, 42)
                                        .addComponent(btn_reset)))
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btn_logout)
                .addGap(3, 3, 3)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(text_id_transaksi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(text_nama_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(combo_id_masakan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_menu_masakan)
                            .addComponent(label_harga)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel15)
                            .addComponent(spinner_kuantitas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btn_pesan)
                            .addComponent(btn_reset)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(combo_id_meja, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(btn_menu_meja))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_hrg_meja, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(text_jml_kursi, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(text_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(text_total_bayar, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_updateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_updateActionPerformed
        // TODO add your handling code here:
        try {
            transaksi tran = new transaksi();
            tran.id_transaksi = Integer.parseInt(text_id_transaksi.getText());
            this.stat = k.getKoneksi().prepareStatement("UPDATE transaksi SET nama_pelanggan=?, id_masakan=?, kode_meja=?, jml_kursi=?, tanggal=?, nama_masakan=?, harga=?, harga_meja=?, jumlah_beli=?, total_bayar=? where id_transaksi=?");
            this.stat.setString(1, tran.nama_pelanggan);
            this.stat.setInt(2, tran.id_masakan);
            this.stat.setInt(3, tran.kode_meja);
            this.stat.setInt(4, tran.jml_kursi);
            this.stat.setString(5, tran.tanggal);
            this.stat.setString(6, tran.nama_masakan);
            this.stat.setInt(7, tran.harga);
            this.stat.setInt(8, tran.harga_meja);
            this.stat.setInt(9, tran.jumlah_beli);
            this.stat.setInt(10, tran.total_bayar);
            this.stat.setInt(11, tran.id_transaksi);
            this.stat.executeUpdate();
            refreshTable();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }
    }//GEN-LAST:event_btn_updateActionPerformed

    private void btn_logoutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_logoutActionPerformed
        // TODO add your handling code here:
        int confirm = JOptionPane.showConfirmDialog(null, "Mau Logout?", "Konfirmasi Logout", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            Login l = new Login();
            l.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btn_logoutActionPerformed

    private void btn_inputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_inputActionPerformed
        // TODO add your handling code here:
        
            transaksi tran = new transaksi();
            text_total_bayar.setText(""+tran.total_bayar);
            String sql = "INSERT INTO tbl_transaksi(id_transaksi, id_meja, id_masakan, kuantitas, jumlah_beli, nama_pelanggan, tanggal) VALUES(?,?,?,?,?,?,?)";
            // Get the content of the text area
            try {
            this.stat = k.getKoneksi().prepareStatement("INSERT into transaksi Values(?,?,?,?,?,?,?,?,?,?,?)");

            stat.setString(1, text_id_transaksi.getText());
            stat.setString(2, combo_id_meja.getSelectedItem().toString());
            stat.setString(3, combo_id_masakan.getSelectedItem().toString());
            stat.setInt(4, Integer.parseInt(spinner_kuantitas.getValue().toString()));
            
            stat.setString(5, text_nama_pelanggan.getText());
            stat.setDate(6, new java.sql.Date(text_tanggal.getDate().getTime()));

            int res = stat.executeUpdate();

            if (res > 0) {
                JOptionPane.showMessageDialog(this, "Data Berhasil Ditambahkan");
            } else {
                JOptionPane.showMessageDialog(this, "Data Gagal Ditambahkan");
            }

            loadTable();

        } catch (SQLException ex) {
            Logger.getLogger(Menu_transaksi1.class.getName()).log(Level.SEVERE, null, ex);
        }
    
         
    }//GEN-LAST:event_btn_inputActionPerformed

    private void table_transaksiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_transaksiMouseClicked
        // TODO add your handling code here:
        text_id_transaksi.setText(model.getValueAt(table_transaksi.getSelectedRow(), 0).toString());
        text_nama_pelanggan.setText(model.getValueAt(table_transaksi.getSelectedRow(), 1).toString());
        
        text_total_bayar.setText(model.getValueAt(table_transaksi.getSelectedRow(), 8).toString());
    }//GEN-LAST:event_table_transaksiMouseClicked

    private void btn_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_deleteActionPerformed
        // TODO add your handling code here:
        try {
            transaksi tran = new transaksi();
            tran.id_transaksi = Integer.parseInt(text_id_transaksi.getText());
            this.stat = k.getKoneksi().prepareStatement("DELETE FROM transaksi where id_transaksi=?");
            this.stat.setInt(1, tran.id_transaksi);
            this.stat.executeUpdate();
            refreshTable();
        } catch (Exception e) {
            
        }
    }//GEN-LAST:event_btn_deleteActionPerformed

    private void btn_menu_masakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_menu_masakanActionPerformed
        // TODO add your handling code here:
        Menu_masakan masak = new Menu_masakan();
        masak.setVisible(true);
        this.setVisible(false);
        masak.btn_delete.setEnabled(true);
        masak.btn_input.setEnabled(true);
        masak.btn_update.setEnabled(true);
        masak.btn_transaksi.setEnabled(true);
    }//GEN-LAST:event_btn_menu_masakanActionPerformed

    private void combo_id_masakanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_id_masakanActionPerformed
        // TODO add your handling code here:
        setHargaBasedOnMenu();
        
    }//GEN-LAST:event_combo_id_masakanActionPerformed

    private void btn_menu_mejaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_menu_mejaActionPerformed
        // TODO add your handling code here:
        // TODO add your handling code here:
        Menu_Meja meju = new Menu_Meja();
        meju.setVisible(true);
        this.setVisible(false);
        meju.btn_delete.setEnabled(true);
        meju.btn_input.setEnabled(true);
        meju.btn_update.setEnabled(true);
        meju.btn_transaksi.setEnabled(true);
    }//GEN-LAST:event_btn_menu_mejaActionPerformed

    private void combo_id_mejaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combo_id_mejaActionPerformed
        // TODO add your handling code here:
        setHargaBasedOnMeja();
        setKursiBasedOnMeja();
    }//GEN-LAST:event_combo_id_mejaActionPerformed

    private void btn_pesanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_pesanActionPerformed
        // TODO add your handling code here:
        tambahItemKeList();
    }//GEN-LAST:event_btn_pesanActionPerformed

    private void text_hrg_mejaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_hrg_mejaActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_text_hrg_mejaActionPerformed

    private void text_jml_kursiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_text_jml_kursiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_text_jml_kursiActionPerformed

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
            java.util.logging.Logger.getLogger(Menu_transaksi1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu_transaksi1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu_transaksi1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu_transaksi1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Menu_transaksi1().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton btn_cetak_laporan;
    public javax.swing.JButton btn_delete;
    public javax.swing.JButton btn_input;
    public javax.swing.JButton btn_logout;
    private javax.swing.JButton btn_menu_masakan;
    private javax.swing.JButton btn_menu_meja;
    public javax.swing.JButton btn_pesan;
    private javax.swing.JButton btn_reset;
    public javax.swing.JButton btn_update;
    private javax.swing.JComboBox<String> combo_id_masakan;
    private javax.swing.JComboBox<String> combo_id_meja;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel label_harga;
    private javax.swing.JSpinner spinner_kuantitas;
    private javax.swing.JTable table_transaksi;
    private javax.swing.JTextArea textArea_pesanan;
    private javax.swing.JTextField text_hrg_meja;
    private javax.swing.JTextField text_id_transaksi;
    private javax.swing.JTextField text_jml_kursi;
    private javax.swing.JTextField text_nama_pelanggan;
    private com.toedter.calendar.JDateChooser text_tanggal;
    private javax.swing.JTextField text_total_bayar;
    // End of variables declaration//GEN-END:variables
}
