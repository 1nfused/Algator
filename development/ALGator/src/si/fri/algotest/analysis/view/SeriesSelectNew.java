package si.fri.algotest.analysis.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TreeSet;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JRadioButton;
import si.fri.algotest.entities.GraphType;

/**
 *
 * @author tomaz
 */
public class SeriesSelectNew extends javax.swing.JPanel {

  ArrayList<JCheckBox>    cb = new ArrayList<>();
  ArrayList<JRadioButton> rb = new ArrayList<>();
  
  ArrayList<String> fields;
  
  ActionListener onChange;
  
  boolean fromProgram = false; // was the change in ChackBox or Radio control triggered by a program command?
  
  /**
   * Creates new form SeriesSelect
   */
  public SeriesSelectNew(final ActionListener action) {
    initComponents();
    
    tTagEditor1.setLabel("Y");
    tTagEditor1.setPreferredSize(new Dimension(50,80));
    tTagEditor1.setOnChangeAction(action);
    
     
    onChange = new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (!fromProgram) action.actionPerformed(e);
      }
    };
    
    lineCB.setSelected(true);
    
    cloudCB.setVisible(false);
    histCB.setVisible(false);
  }

  
  public void addFields(ArrayList<String> fields) {
    this.fields = fields;
    
    Object izbranX = XCOmbo.getSelectedItem();
    
    ArrayList xFields = new ArrayList();
    for (String field : fields) {
      if (!field.startsWith("*"))
        xFields.add(field);
    }
    XCOmbo.setModel(new JComboBox(xFields.toArray()).getModel());
    if (izbranX != null)
      XCOmbo.setSelectedItem(izbranX);
       
    tTagEditor1.setItems(fields.toArray());
    
    revalidate();
    repaint();
  }
  
  public String getXField() {
    return XCOmbo.getSelectedItem().toString();
  }
  
  public void setXField(String xField) {
    XCOmbo.setSelectedItem(xField);
  }
  
  public String [] getYFieldsID() {
    Object [] tagedItems = tTagEditor1.getTagedItems();
    String [] result = new String[tagedItems.length];
    
    int i=0;  
    for (Object to : tagedItems) {
      result[i++] = to.toString();
    }
    return result;
  } 
  
  public void setYFieldsID(String [] yFields) {
    tTagEditor1.setTags(yFields);
  }
  
  
  public TreeSet<GraphType> getGraphType() {
    TreeSet<GraphType> result = new TreeSet<GraphType>();
    if (lineCB. isSelected()) result.add(GraphType.LINE);
    if (stairCB.isSelected()) result.add(GraphType.STAIR);
    if (barCB.  isSelected()) result.add(GraphType.BAR);
    if (boxCB.  isSelected()) result.add(GraphType.BOX);
    return result;
  }
  
  public void setGraphType(TreeSet<GraphType> gType) {
    lineCB. setSelected(gType.contains(GraphType.LINE));
    stairCB.setSelected(gType.contains(GraphType.STAIR));
    barCB.  setSelected(gType.contains(GraphType.BAR));
    boxCB.  setSelected(gType.contains(GraphType.BOX));
  }
  /**
   * This method is called from within the constructor to initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is always
   * regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {
    java.awt.GridBagConstraints gridBagConstraints;

    jPanel1 = new javax.swing.JPanel();
    jLabel1 = new javax.swing.JLabel();
    jLabel2 = new javax.swing.JLabel();
    jLabel4 = new javax.swing.JLabel();
    XCOmbo = new javax.swing.JComboBox();
    tTagEditor1 = new si.fri.algotest.view.tags.TTagEditor();
    jPanel2 = new javax.swing.JPanel();
    lineCB = new javax.swing.JCheckBox();
    stairCB = new javax.swing.JCheckBox();
    barCB = new javax.swing.JCheckBox();
    boxCB = new javax.swing.JCheckBox();
    cloudCB = new javax.swing.JCheckBox();
    histCB = new javax.swing.JCheckBox();

    javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
    jPanel1.setLayout(jPanel1Layout);
    jPanel1Layout.setHorizontalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );
    jPanel1Layout.setVerticalGroup(
      jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGap(0, 100, Short.MAX_VALUE)
    );

    jLabel1.setText("jLabel1");

    setBackground(new java.awt.Color(255, 255, 255));
    setLayout(new java.awt.GridBagLayout());
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 0;
    gridBagConstraints.gridy = 0;
    add(jLabel2, gridBagConstraints);

    jLabel4.setText("X");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
    add(jLabel4, gridBagConstraints);

    XCOmbo.setPreferredSize(new java.awt.Dimension(200, 27));
    XCOmbo.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        XCOmboItemStateChanged(evt);
      }
    });
    XCOmbo.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        XCOmboActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.insets = new java.awt.Insets(5, 0, 0, 0);
    add(XCOmbo, gridBagConstraints);
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridheight = 2;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.anchor = java.awt.GridBagConstraints.FIRST_LINE_START;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.weighty = 1.0;
    add(tTagEditor1, gridBagConstraints);

    jPanel2.setBackground(new java.awt.Color(255, 255, 255));
    jPanel2.setLayout(new java.awt.GridLayout(2, 3));

    lineCB.setText("Line");
    lineCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        barTypeChanged(evt);
      }
    });
    jPanel2.add(lineCB);

    stairCB.setText("Stair");
    stairCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        barTypeChanged(evt);
      }
    });
    jPanel2.add(stairCB);

    barCB.setText("Bar");
    barCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        barTypeChanged(evt);
      }
    });
    jPanel2.add(barCB);

    boxCB.setText("Box");
    boxCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        barTypeChanged(evt);
      }
    });
    jPanel2.add(boxCB);

    cloudCB.setText("Cloud");
    cloudCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        barTypeChanged(evt);
      }
    });
    jPanel2.add(cloudCB);

    histCB.setText("Hist");
    histCB.addItemListener(new java.awt.event.ItemListener() {
      public void itemStateChanged(java.awt.event.ItemEvent evt) {
        barTypeChanged(evt);
      }
    });
    jPanel2.add(histCB);

    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.gridx = 2;
    gridBagConstraints.gridy = 1;
    gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
    gridBagConstraints.weighty = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
    add(jPanel2, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void XCOmboActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XCOmboActionPerformed
    // TODO add your handling code here:
  }//GEN-LAST:event_XCOmboActionPerformed

  private void XCOmboItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_XCOmboItemStateChanged
    if (onChange != null)
      onChange.actionPerformed(null);

  }//GEN-LAST:event_XCOmboItemStateChanged

  private void barTypeChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_barTypeChanged
    if (onChange != null)
      onChange.actionPerformed(null);
  }//GEN-LAST:event_barTypeChanged


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JComboBox XCOmbo;
  private javax.swing.JCheckBox barCB;
  private javax.swing.JCheckBox boxCB;
  private javax.swing.JCheckBox cloudCB;
  private javax.swing.JCheckBox histCB;
  private javax.swing.JLabel jLabel1;
  private javax.swing.JLabel jLabel2;
  private javax.swing.JLabel jLabel4;
  private javax.swing.JPanel jPanel1;
  private javax.swing.JPanel jPanel2;
  private javax.swing.JCheckBox lineCB;
  private javax.swing.JCheckBox stairCB;
  private si.fri.algotest.view.tags.TTagEditor tTagEditor1;
  // End of variables declaration//GEN-END:variables
}
