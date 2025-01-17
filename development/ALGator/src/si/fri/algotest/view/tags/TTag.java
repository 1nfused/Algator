/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package si.fri.algotest.view.tags;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;

/**
 *
 * @author tomaz
 */
public class TTag extends javax.swing.JPanel {

  Object tagObject;
  TTagEditor editor;
  
  /**
   * Creates new form NewJPanel
   */
  public TTag(Object tagObject, TTagEditor editor) {
    initComponents();
    
    this.tagObject = tagObject;
    this.editor = editor;
    
    label.setText(tagObject.toString());
    
    setLabelSize();
  }

  private void setLabelSize() {
    String text = label.getText();
    AffineTransform affinetransform = new AffineTransform();     
    FontRenderContext frc = new FontRenderContext(affinetransform,true,true);     
    Font font = label.getFont();
    int textwidth = (int)(font.getStringBounds(text, frc).getWidth());
    int textheight = (int)(font.getStringBounds(text, frc).getHeight());
     
    Dimension dim = new Dimension(textwidth + 8 + 18 + 5, textheight + 4);
    setMinimumSize(dim);setMaximumSize(dim);setPreferredSize(dim);
  }
  
  public Object getTagObject() {
    return tagObject;
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

    label = new javax.swing.JLabel();
    jButton1 = new javax.swing.JButton();

    setBackground(new java.awt.Color(250, 250, 250));
    setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
    setMaximumSize(new java.awt.Dimension(2147483647, 22));
    setMinimumSize(new java.awt.Dimension(79, 22));
    setPreferredSize(new java.awt.Dimension(400, 22));
    setLayout(new java.awt.GridBagLayout());

    label.setText("jLabel1");
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gridBagConstraints.weightx = 1.0;
    gridBagConstraints.insets = new java.awt.Insets(1, 1, 1, 0);
    add(label, gridBagConstraints);

    jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/delete.png"))); // NOI18N
    jButton1.setMaximumSize(new java.awt.Dimension(16, 16));
    jButton1.setMinimumSize(new java.awt.Dimension(16, 16));
    jButton1.setPreferredSize(new java.awt.Dimension(16, 16));
    jButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        jButton1ActionPerformed(evt);
      }
    });
    gridBagConstraints = new java.awt.GridBagConstraints();
    gridBagConstraints.insets = new java.awt.Insets(1, 3, 1, 1);
    add(jButton1, gridBagConstraints);
  }// </editor-fold>//GEN-END:initComponents

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
    if (editor != null)
      editor.untagItem(this);
  }//GEN-LAST:event_jButton1ActionPerformed


  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton jButton1;
  private javax.swing.JLabel label;
  // End of variables declaration//GEN-END:variables
}
